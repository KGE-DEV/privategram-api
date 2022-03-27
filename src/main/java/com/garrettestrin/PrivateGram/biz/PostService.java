package com.garrettestrin.PrivateGram.biz;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.garrettestrin.PrivateGram.api.ApiObjects.PostCountResponse;
import com.garrettestrin.PrivateGram.api.ApiObjects.PostResponse;
import com.garrettestrin.PrivateGram.app.Config.AWSConfig;
import com.garrettestrin.PrivateGram.app.PrivateGramConfiguration;
import com.garrettestrin.PrivateGram.data.Cache;
import com.garrettestrin.PrivateGram.data.DataObjects.Post;
import com.garrettestrin.PrivateGram.data.PostDao;
import com.garrettestrin.PrivateGram.data.UserDao;
import com.tinify.Options;
import com.tinify.Source;
import com.tinify.Tinify;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.jbosslog.JBossLog;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@JBossLog
public class PostService {

  private final PostDao postDao;
  private final UserDao userDao;
  private final AWSConfig awsConfig;
  private final Regions regions;

  private final String DELETED_POST_SUCCESS = "post was deleted";
  private final String DELETED_POST_FAIL = "post was not deleted";
  private final String EDITED_POST_SUCCESS = "post was edited";
  private final String EDITED_POST_FAIL = "post was not edited";

  private final int MAX_THREADS = 50;
  private final String tinifyKey;
  private final Executor executor = Executors.newFixedThreadPool(MAX_THREADS);
  private final BizUtilities bizUtilities;
  private final Cache cache;

  public PostService(PostDao postDao, AWSConfig awsConfig, String tinifyKey, PrivateGramConfiguration config, UserDao userDao, Cache cache) {

    this.postDao = postDao;
    this.userDao = userDao;
    this.awsConfig = awsConfig;
    this.regions = Regions.fromName(awsConfig.getS3Region());
    this.tinifyKey = tinifyKey;
    this.bizUtilities = new BizUtilities(config);
    this.cache = cache;
  }

  public PostResponse getAllPosts(String siteKey) {
    if (null == siteKey) return PostResponse.builder().success(false).message("No siteKey set").build();
    return new PostResponse(true, null, postDao.getAllPosts(siteKey));
  }

  public PostResponse getPaginatedPosts(Integer lower_limit, boolean isAdmin, String siteKey) throws IOException {
    String page = lower_limit.toString();
    if(lower_limit > 0) {
      lower_limit = (lower_limit - 1) * 10;
    }
    // check cache for data
    String cachedPosts = cache.getPost(cache.POST_PAGE + page + siteKey, isAdmin);
    // if data is cached
    if (null != cachedPosts) {
      return cache.decode(cachedPosts, PostResponse.class);
    }
    // if no data in cache, get from db and then cache
    // then return data
    PostResponse postResponse = new PostResponse(true, null, parsePostsForEmojis(postDao.getPaginatedPosts(lower_limit, isAdmin, siteKey)));
    cache.setPost(cache.POST_PAGE + page + siteKey, cache.encode(postResponse), isAdmin);
    return postResponse;
  }

  public PostResponse editPost(int postId, String postContent) {
    cache.clearPostCache();
    boolean wasPostEdited = postDao.editPost(postId, EmojiParser.parseToAliases(postContent));
    String postEditedMessage = wasPostEdited ? EDITED_POST_SUCCESS : EDITED_POST_FAIL;
    List<Post> updatedPost = postDao.getPost(postId);
    return new PostResponse(wasPostEdited, postEditedMessage, updatedPost);
  }

  public PostResponse deletePost(int postId) {
    cache.clearPostCache();
    boolean wasPostDeleted = postDao.deletePost(postId);
    String postDeletedMessage = wasPostDeleted ? DELETED_POST_SUCCESS : DELETED_POST_FAIL;
    return new PostResponse(wasPostDeleted, postDeletedMessage, null);
  }

  public PostCountResponse postCount(boolean isAdmin, String siteKey) {
    String postCountCached = cache.getPost(cache.POST_COUNT + siteKey, isAdmin);
    if (null != postCountCached) {
      return new PostCountResponse(Integer.parseInt(postCountCached));
    }
    int postCount = postDao.postCount(isAdmin, siteKey);
    cache.setPost(cache.POST_COUNT + siteKey, String.valueOf(postCount), isAdmin);
    return new PostCountResponse(postCount);
  }

  private String uploadToS3(String fileName, String type, String name) {

    try {
      BasicAWSCredentials awsCreds = new BasicAWSCredentials(awsConfig.getS3AccessKey(), awsConfig.getS3SecretKey());
      AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
              .withRegion(regions)
              .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
              .build();
      // Upload a file as a new object with ContentType and title specified.
      String urlName = System.currentTimeMillis() + "_" + name;
      PutObjectRequest request = new PutObjectRequest(awsConfig.getBucket(), urlName, new File(fileName));
      ObjectMetadata metadata = new ObjectMetadata();
      metadata.setContentType(type);
      request.setMetadata(metadata);
      s3Client.putObject(request);
      return urlName;
    } catch (AmazonServiceException e) {
      // The call was transmitted successfully, but Amazon S3 couldn't process
      // it, so it returned an error response.
      e.printStackTrace();
    } catch (SdkClientException e) {
      // Amazon S3 couldn't be contacted for a response, or the client
      // couldn't parse the response from Amazon S3.
      e.printStackTrace();
    }
    return fileName;
  }

  private String writeStreamToFile(InputStream inputStream, String name) throws IOException {
    File directory = new File("tmp");
    if (! directory.exists()) {
      directory.mkdir();
    }
    byte[] bytes = IOUtils.toByteArray(inputStream);
    String pathName = "tmp/" + name;
    File file = new File(pathName);
    BufferedImage img = ImageIO.read(new ByteArrayInputStream(bytes));
    String[] typeArray = name.split("\\.");
    String type = typeArray[1].toUpperCase();
    ImageIO.write(img, type, file);
    return pathName;
  }

  private String resizeAndCompressImage(String name, int postedHeight, int postedWidth) throws IOException, ImageProcessingException {
    Tinify.setKey(tinifyKey);
    String pathToFile = "tmp/" + name;
//    compare uploaded height and width to saved file
//    if different, the image needs to be rotated
//    if (postedHeight > 0 && postedWidth > 0) {
//      int actualHeight = 0;
//      int actualWidth = 0;
//      Metadata metadata = ImageMetadataReader.readMetadata(new File(pathToFile));
//      for (Directory directory : metadata.getDirectories()) {
//        for (Tag tag : directory.getTags()) {
//          if (tag.getTagName().equals("Image Height")) {
//            actualHeight = Integer.parseInt(tag.getDescription().split(" ")[0]);
//          }
//          if (tag.getTagName().equals("Image Width")) {
//            actualWidth = Integer.parseInt(tag.getDescription().split(" ")[0]);
//          }
//        }
//      }
//      if (actualHeight != postedHeight && actualWidth != postedWidth && actualHeight != 0 && actualWidth != 0) {
//        rotateImage(pathToFile);
//      }
//    }

    Source source = Tinify.fromFile(pathToFile);
    Options options = new Options()
            .with("method", "scale")
            .with("width", 500);
    Source resized = source.resize(options);
    resized.toFile(pathToFile);
    return pathToFile;
  }

  private void rotateImage(String pathToFile) throws IOException {
    BufferedImage image = ImageIO.read(new File(pathToFile));
    final double rads = Math.toRadians(90);
    final double sin = Math.abs(Math.sin(rads));
    final double cos = Math.abs(Math.cos(rads));
    final int w = (int) Math.floor(image.getWidth() * cos + image.getHeight() * sin);
    final int h = (int) Math.floor(image.getHeight() * cos + image.getWidth() * sin);
    final BufferedImage rotatedImage = new BufferedImage(w, h, image.getType());
    final AffineTransform at = new AffineTransform();
    at.translate(w / 2, h / 2);
    at.rotate(rads,0, 0);
    at.translate(-image.getWidth() / 2, -image.getHeight() / 2);
    final AffineTransformOp rotateOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
    rotateOp.filter(image,rotatedImage);
    String[] typeArray = pathToFile.split("\\.");
    String type = typeArray[1].toUpperCase();
    ImageIO.write(rotatedImage, type, new File(pathToFile));
  }

  private List<Post> parsePostsForEmojis(List<Post> posts) {
    List<Post> parsedPosts = new ArrayList<>();
    for(int i = 0; i < posts.size();i++) {
      Post post = posts.get(i);
      Post tempPost = Post.builder().post_content(EmojiParser.parseToUnicode(post.getPost_content())).id(post.getId()).post_image_url(post.getPost_image_url()).date_time_added(post.getDate_time_added()).build();
      parsedPosts.add(i, tempPost);
    }
    return parsedPosts;
  }

  public PostResponse getIndividualPost(Integer pageId, boolean admin) {
    return PostResponse.builder().posts(postDao.getIndividualPost(pageId, admin)).build();
  }

  public PostResponse handleMultiPost(String caption, boolean isPrivate, JSONArray filesData, FormDataMultiPart multiPart, String siteKey) throws IOException {
    if (null == siteKey) return PostResponse.builder().success(false).message("Missing siteKey").build();
    List<FormDataBodyPart> bodyParts =
            multiPart.getFields("file");
    List imageUrls = new ArrayList();
    int iterator = 0;
    for (FormDataBodyPart part : bodyParts) {
      imageUrls.add(null);
    }
    for (FormDataBodyPart part : bodyParts) {
      JSONObject fileData = (JSONObject) filesData.get(iterator);
      writeStreamToFile(part.getValueAs(InputStream.class), fileData.getString("name"));
      executor.execute(() -> {
        try {
          imageUrls.set(
                  fileData.getInt("order"),
                  addMultiPost(caption, fileData.getString("name"), fileData.getString("type"), fileData.getInt("height"), fileData.getInt("width")));
        } catch (IOException e) {
          e.printStackTrace();
        }
      });
      iterator++;
    }
    // new thread to handle saving to db
    executor.execute(() -> {
      while (doesArrayContainNull(imageUrls)) { }
      postDao.addPost(EmojiParser.parseToAliases(caption), String.join(",", imageUrls), isPrivate, siteKey);
      cache.clearPostCache();
      log.info("Post was successfully processed");
    });
    return PostResponse.builder().success(true).message("Your post is being processed").build();
  }

  private boolean doesArrayContainNull(List imageUrls) {
    for (Object url:imageUrls) {
      if (null == url) {
        return true;
      }
    }
    return false;
  }

  /**
   * Processes image upload, uploads to AWS and inserts into DB
   * Response is returned after image is uploaded to server, but before it is fully processed
   * @param caption
   * @param name
   * @param type
   * @return
   * @throws IOException
   */
  private String addMultiPost(String caption, String name, String type, int height, int width) throws IOException {
      String urlString;
      try {
        urlString = uploadToS3(resizeAndCompressImage(name, height, width), type, name);
      } catch (IOException | ImageProcessingException e) {
        e.printStackTrace();
        // if post failed
        // send an email to admins
        bizUtilities.sendPostErrorEmail(userDao.getAdminUsers(), caption);
        return "";
      }
      return awsConfig.getBucketUrl() + "/" + urlString;
    }

}
