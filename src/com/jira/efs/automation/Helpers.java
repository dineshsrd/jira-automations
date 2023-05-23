package com.jira.efs.automation;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.RequestBody;

public class Helpers {

	private static Properties applicationProps =new Properties();
	static {
		try {
			FileInputStream inputStream= new FileInputStream("./resources/config.properties");
			applicationProps.load(inputStream);
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static final String encodedApiKey = (String) applicationProps.getOrDefault("base64AuthKey", "provideValidBase64EncodedToken");

	public static void doPostAPICall(String apiUrl, RequestBody body)
		throws IOException {
		OkHttpClient client = new OkHttpClient().newBuilder()
			.build();
		Request request = new Request.Builder()
			.url(apiUrl)
			.method("POST", body)
			.addHeader("Content-Type", "application/json")
			.addHeader("Authorization", "Basic "+encodedApiKey)
			.build();
		Response response = client.newCall(request).execute();
		System.out.println(response.message());
	}

	public static void doGetAPICall(String apiUrl, RequestBody body)
		throws IOException {
		OkHttpClient client = new OkHttpClient().newBuilder()
			.build();
		Request request = new Request.Builder()
			.url(apiUrl)
			.method("GET", body)
			.addHeader("Authorization", "Basic "+encodedApiKey)
			.build();
		Response response = client.newCall(request).execute();
		System.out.println(response.body().string());
	}


}
