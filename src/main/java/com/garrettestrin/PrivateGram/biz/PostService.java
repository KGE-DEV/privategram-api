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
import com.garrettestrin.PrivateGram.api.ApiObjects.PostCountResponse;
import com.garrettestrin.PrivateGram.api.ApiObjects.PostResponse;
import com.garrettestrin.PrivateGram.app.Config.AWSConfig;
import com.garrettestrin.PrivateGram.app.PrivateGramConfiguration;
import com.garrettestrin.PrivateGram.data.DataObjects.Post;
import com.garrettestrin.PrivateGram.data.PostDao;
import com.garrettestrin.PrivateGram.data.UserDao;
import com.tinify.Options;
import com.tinify.Source;
import com.tinify.Tinify;
import com.vdurmont.emoji.EmojiParser;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import javax.imageio.ImageIO;
import lombok.extern.jbosslog.JBossLog;

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
  private final int MAX_THREADS = 256;
  private final String tinifyKey;
  private final Executor executor = Executors.newFixedThreadPool(MAX_THREADS);
  private final BizUtilities bizUtilities;

  public PostService(PostDao postDao, AWSConfig awsConfig, String tinifyKey, PrivateGramConfiguration config, UserDao userDao) {

    this.postDao = postDao;
    this.userDao = userDao;
    this.awsConfig = awsConfig;
    this.regions = Regions.fromName(awsConfig.getS3Region());
    this.tinifyKey = tinifyKey;
    this.bizUtilities = new BizUtilities(config);
  }

  public PostResponse addPost(String caption, InputStream inputStream, String name, String type) throws IOException {

    writeStreamToFile(inputStream, name);
    executor.execute(() -> {
      String urlString;
      try {
        urlString = uploadToS3(resizeAndCompressImage(name), type, name);
      } catch (IOException e) {
        e.printStackTrace();
        // if post failed
        // send an email to admins
       bizUtilities.sendPostErrorEmail(userDao.getAdminUsers(), caption);
       return;
      }
      postDao.addPost(EmojiParser.parseToAliases(caption), awsConfig.getBucketUrl() + "/" + urlString);
      log.info("Post was successfully processed");
    });
    return PostResponse.builder().success(true).message("Your post is being processed").build();
  }

  public PostResponse getAllPosts() {

    return new PostResponse(true, null, postDao.getAllPosts());
  }

  public PostResponse getPaginatedPosts(Integer lower_limit) {

    if(lower_limit > 0) {
      lower_limit = (lower_limit - 1) * 10;
    }
    return new PostResponse(true, null, parsePostsForEmojis(postDao.getPaginatedPosts(lower_limit)));
  }

  public PostResponse editPost(int postId, String postContent) {
    boolean wasPostEdited = postDao.editPost(postId, EmojiParser.parseToAliases(postContent));
    String postEditedMessage = wasPostEdited ? EDITED_POST_SUCCESS : EDITED_POST_FAIL;
    List<Post> updatedPost = postDao.getPost(postId);
    return new PostResponse(wasPostEdited, postEditedMessage, updatedPost);
  }

  public PostResponse deletePost(int postId) {
    boolean wasPostDeleted = postDao.deletePost(postId);
    String postDeletedMessage = wasPostDeleted ? DELETED_POST_SUCCESS : DELETED_POST_FAIL;
    return new PostResponse(wasPostDeleted, postDeletedMessage, null);
  }

  public PostCountResponse postCount() {
    return new PostCountResponse(postDao.postCount());
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

  private String resizeAndCompressImage(String name) throws IOException {
    Tinify.setKey(tinifyKey);
    String pathToFile = "tmp/" + name;
    Source source = Tinify.fromFile(pathToFile);
    Options options = new Options()
            .with("method", "scale")
            .with("width", 500);
    Source resized = source.resize(options);
    resized.toFile(pathToFile);
    return pathToFile;
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
}
