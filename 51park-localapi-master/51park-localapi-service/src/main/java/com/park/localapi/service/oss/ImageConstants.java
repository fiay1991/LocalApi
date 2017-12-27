package com.park.localapi.service.oss;

import java.net.URL;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aliyun.oss.OSSClient;
import com.park.localapi.service.config.ImageURLConfig;
@Component("imageConstants")
public class ImageConstants {

	@Autowired
	private ImageURLConfig imageURLConfig;
	
	/**
	 * OSS加密图片路径处理
	 */
	public URL setBetaImage(String arg){
		
		String accessKeyId = imageURLConfig.getAccessKeyId();
		String accessKeySecret = imageURLConfig.getAccessKeySecret();
		String endpoint = imageURLConfig.getEndpoint();

		OSSClient client = new OSSClient(endpoint, accessKeyId, accessKeySecret);
		
		String bucketName = imageURLConfig.getBucketName();
		String key = arg;

		// 设置URL过期时间为1小时
		Date expiration = new Date(new Date().getTime() + 3600 * 1000);

		// 生成URL
		URL url = client.generatePresignedUrl(bucketName, key, expiration);
		
		return url;	
	}
}
