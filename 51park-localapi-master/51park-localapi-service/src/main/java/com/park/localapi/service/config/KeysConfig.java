package com.park.localapi.service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("keysConfig")
public class KeysConfig {
	
	@Value("${Keys.private_key}")
	private String private_key;
	@Value("${Keys.public_key}")
	private String public_key;
	public String getPrivate_key() {
		return private_key;
	}
	public void setPrivate_key(String private_key) {
		this.private_key = private_key;
	}
	public String getPublic_key() {
		return public_key;
	}
	public void setPublic_key(String public_key) {
		this.public_key = public_key;
	}
	


	
}
