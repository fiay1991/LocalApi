package com.park.localapi.service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("imageURLConfig")
public class ImageURLConfig {
	
	@Value("${Image.bucketName}")
	private String bucketName;
	@Value("${Image.endpoint}")
	private String endpoint;
	@Value("${Image.accessKeyId}")
	private String accessKeyId;
	@Value("${Image.accessKeySecret}")
	private String accessKeySecret;
	
	
	public String getEndpoint() {
		return endpoint;
	}
	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}
	public String getBucketName() {
		return bucketName;
	}
	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}
	public String getAccessKeyId() {
		return accessKeyId;
	}
	public void setAccessKeyId(String accessKeyId) {
		this.accessKeyId = accessKeyId;
	}
	public String getAccessKeySecret() {
		return accessKeySecret;
	}
	public void setAccessKeySecret(String accessKeySecret) {
		this.accessKeySecret = accessKeySecret;
	}
	
	
	
}
