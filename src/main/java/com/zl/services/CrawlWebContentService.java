package com.zl.services;

import java.net.URL;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import abstracts.AService;

import com.zl.entities.WebContentEntity;
import com.zl.interfaces.ICrawlWebContentService;
import com.zl.job.manager.WebContentManager;

@Service
@Scope("prototype")
public class CrawlWebContentService extends AService implements ICrawlWebContentService {

	private URL url;
	private int depth;
	
	@Autowired
	public WebContentManager webContentManager;
	
	@Async
	@Override
	public void crawlWebContent(URL url, int depth) {
		this.url = url;
		this.depth = depth;
		this.start();
	}
	
	@Override
	public HttpMethod getHttpMethod() {
		return HttpMethod.GET;
	}

	@Override
	public String getUri() {
		return null;
	}

	@Override
	public String constructRequestUrl() {
		return url.toString();
	}

	@Override
	public HttpEntity<String> constructRequestHttpEntity() {
		HttpHeaders header = new HttpHeaders();
		JSONObject item = new JSONObject();;
		return new HttpEntity<String>(item.toString(), header);
	}

	@Override
	public void onSuccess(ResponseEntity<String> response) {
		
		// Discard all non-text files
        // Further assumptions on the mime type should not be made, because
        // some WSDLs advertise themselves as text/plain, others as text/xml
        // Anyway, we should try to identify WSDL pages by the definitions-
        // tag rather than by content-type.
		try {
			if (response.getHeaders().getContentType().toString().substring(0, 4).equalsIgnoreCase("text"))
				webContentManager.addContent(new WebContentEntity(response.getBody().toString(), depth - 1));
		}
		catch (IndexOutOfBoundsException ex) {
			
		}
	}

	@Override
	public void onFailure(ResponseEntity<String> response) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isSuccess(ResponseEntity<String> response) {
		return response.getStatusCode() == HttpStatus.OK;
	}
}
