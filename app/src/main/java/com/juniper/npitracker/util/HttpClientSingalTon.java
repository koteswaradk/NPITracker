package com.juniper.npitracker.util;

import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

public class HttpClientSingalTon {

	private HttpClientSingalTon(){

	}
	private static Object yppLock = new Object();
	private static CookieStore yppCookie = null;

	public static HttpClient getHttpClienttest() {

		final DefaultHttpClient httpClient = new DefaultHttpClient();
		synchronized (yppLock) {
			if (yppCookie == null) {
				yppCookie = httpClient.getCookieStore();
			} else {
				httpClient.setCookieStore(yppCookie);
			}
		}
		return httpClient;
	}

}
