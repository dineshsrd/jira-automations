package com.jira.efs.automation;

import java.io.IOException;

import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.RequestBody;

import static com.jira.efs.automation.AutomationRunner.applicationProps;
import static com.jira.efs.automation.AutomationRunner.configJson;

public class Helpers {

	private static final String encodedApiKey = (String) applicationProps.getOrDefault("base64AuthKey", "provideValidBase64EncodedToken");

	public static JSONObject doPostAPICall(String apiUrl, RequestBody body)
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
		String rezp = response.body().string();
		JSONObject responseJson = new JSONObject(rezp);
		return responseJson;
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
