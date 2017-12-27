package com.park.localapi.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObject;
import com.park.localapi.service.ImageUrl;
@Component("imageService")
public class ImageService {
	
	private Logger logger =LoggerFactory.getLogger(this.getClass());
	@Autowired
	private  ImageUrl imageUrl;
	
	/**
	 * OSS加密图片路径 
	 */
	public String setBetaImage(String name){
		
		String accessKeyId = imageUrl.getAccessKeyId();
		String accessKeySecret = imageUrl.getAccessKeySecret();
		String endpoint = imageUrl.getEndpoint();
		OSSClient client = new OSSClient(endpoint, accessKeyId, accessKeySecret);
		String bucketName = imageUrl.getBucketName();
		String key = name;
		// 设置URL过期时间为1小时
		Date expiration = new Date(new Date().getTime() + 3600 * 1000);

		// 生成URL
		URL url = client.generatePresignedUrl(bucketName, key, expiration);
		
		return url.toString();	
	}
	public InputStream getOSS2InputStream(String name){   
		String accessKeyId = imageUrl.getAccessKeyId();
		String accessKeySecret = imageUrl.getAccessKeySecret();
		String endpoint = imageUrl.getEndpoint();
		OSSClient client = new OSSClient(endpoint, accessKeyId, accessKeySecret);
		OSSObject ossObj = client.getObject(imageUrl.getBucketName(), name);  
		return ossObj.getObjectContent();     
	}    
	
	public void uploadFileStream(byte[] fileStream, String fileName){
		String endpoint = imageUrl.getEndpoint();
		String accessKeyId = imageUrl.getAccessKeyId();
		String accessKeySecret = imageUrl.getAccessKeySecret();
		OSSClient ossClient = new OSSClient(endpoint, accessKeyId,accessKeySecret);
		ossClient.putObject(imageUrl.getBucketName(), fileName, new ByteArrayInputStream(fileStream));
		ossClient.shutdown();
	}
	
	public void uploadFile(File file, String fileName){
		String endpoint = imageUrl.getEndpoint();
		String accessKeyId = imageUrl.getAccessKeyId();
		String accessKeySecret = imageUrl.getAccessKeySecret();
		OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
		ossClient.putObject(imageUrl.getBucketName(), fileName, file);
		ossClient.shutdown();
	}
	
	public File getOSS2File(String fileName){
		String endpoint = imageUrl.getEndpoint();
		String accessKeyId = imageUrl.getAccessKeyId();
		String accessKeySecret = imageUrl.getAccessKeySecret();
		String bucketName = imageUrl.getBucketName();
		OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String path = request.getSession().getServletContext().getRealPath("/WEB-INF/");
		String[] split = fileName.split("/");
		String fileName1 =split[3];
		logger.info("***fileName="+fileName);
		File file = new File(path +"/temporary/");
		if(!(file.exists()) && !(file.isDirectory())){
			boolean mkdirs = file.mkdirs();
			if(mkdirs){
				logger.info("创建文件夹成功 路径:"+path +"temporary/");
			}else {
				logger.info("创建文件夹失败");
			}
		}
		File csvFile = new File(path +"/temporary/"+fileName1);
		logger.info("***csvFile"+csvFile);
		ossClient.getObject(new GetObjectRequest(bucketName, fileName), csvFile);
		ossClient.shutdown();
		return csvFile;
	}
	
}
