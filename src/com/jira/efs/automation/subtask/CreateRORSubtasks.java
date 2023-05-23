package com.jira.efs.automation.subtask;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.jira.efs.automation.Helpers;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class CreateRORSubtasks {

	//Create Retailer Onboarding Subtasks.

	private static final String ISSUE_URL = "https://efundamentals.atlassian.net/rest/api/2/issue/";

	static List subTasks = new ArrayList<>() {{
		add("Backlog Refinement");
		add("Coding");
		add("Code Review");
		add("CSS Review");
		add("Testing");
		add("Scheduled");
		add("Monitoring");
	}};


	public static JSONObject createJson(String issueId, String subTask){
		JSONObject fieldJson = new JSONObject();
		JSONObject fieldDataJson = new JSONObject();
		fieldDataJson.put("project", new JSONObject(){{put("key", "ESF");}});
		fieldDataJson.put("parent", new JSONObject(){{put("key", issueId);}});
		fieldDataJson.put("summary", subTask);
		fieldDataJson.put("issuetype", new JSONObject(){{put("id", "5");}});
		fieldJson.put("fields", fieldDataJson);
		return fieldJson;
	}

	public static void createTasks(String issueId) {
		String url = ISSUE_URL;
		System.out.println(url);
		subTasks.forEach(taskName -> {
			try {
				MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
				RequestBody body = RequestBody.create(mediaType, createJson(issueId, taskName.toString()).toString());
				Helpers.doPostAPICall(url, body);
			}
			catch(Exception e) {
				throw new RuntimeException(e);
			}
		});
	}

}
