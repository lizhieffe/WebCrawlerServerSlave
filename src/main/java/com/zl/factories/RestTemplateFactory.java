package com.zl.factories;

import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class RestTemplateFactory {
	
	public static RestTemplate getRestTemplateWithTimeout() {
		return new RestTemplate(getFactoryWithTimeout());
	}
	
	private static HttpComponentsClientHttpRequestFactory factoryWithTimeout;
	private static int timeout = 2000;

	synchronized private static ClientHttpRequestFactory getFactoryWithTimeout() {
		if (factoryWithTimeout == null) {
			factoryWithTimeout = new HttpComponentsClientHttpRequestFactory();
			factoryWithTimeout.setReadTimeout(timeout);
			factoryWithTimeout.setConnectTimeout(timeout);
		}
        return factoryWithTimeout;
	}
	
	
}
