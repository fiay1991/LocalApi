package com.park.localapi.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("proxyConfig")
public class ProxyConfig {
	
	@Value("${Proxy.port}")
	private int pory;
	@Value("${Proxy.url}")
	private String url;
	@Value("${Proxy.post}")
	private String post;
	
	public int getPory() {
		return pory;
	}
	public void setPory(int pory) {
		this.pory = pory;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getPost() {
		return post;
	}
	public void setPost(String post) {
		this.post = post;
	}

}
