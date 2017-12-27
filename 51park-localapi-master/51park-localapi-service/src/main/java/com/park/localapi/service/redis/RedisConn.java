package com.park.localapi.service.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component  
public class RedisConn {
	@Value("${redis.host}")
	private String host;  
	@Value("${redis.port}")
    private int port;  
	@Value("${redis.timeout}")
    private int timeout; 
	@Value("${redis.password}")
    private String password;
  
  
    public String getHost() {  
        return host;  
    }  
  
    public void setHost(String host) {  
        this.host = host;  
    }  
  
    public int getPort() {  
        return port;  
    }  
  
    public void setPort(int port) {  
        this.port = port;  
    }  
  
    public int getTimeout() {  
        return timeout;  
    }  
  
    public void setTimeout(int timeout) {  
        this.timeout = timeout;  
    }  
    
    public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override  
    public String toString() {  
        return "Redis [localhost=" + host + ", port=" + port + ", timeout=" + timeout + "]";  
    }  
}
