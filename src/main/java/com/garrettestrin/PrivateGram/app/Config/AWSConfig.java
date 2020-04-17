package com.garrettestrin.PrivateGram.app.Config;

import lombok.Data;

/**
 * This class provides aws configuration.
 */
@Data
public class AWSConfig {
  private String s3AccessKey;
  private String s3SecretKey;
  private String s3Region;
  private String bucket;
  private String bucketUrl;
}
