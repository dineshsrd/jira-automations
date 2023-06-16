package com.jira.efs.automation.subtask;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.jira.efs.automation.Helpers;

import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.jira.efs.automation.AutomationRunner.applicationProps;
import static com.jira.efs.automation.AutomationRunner.configJson;

public class CreateRORSubtasks {

	//Create Retailer Onboarding Subtasks.

	private static final String ISSUE_URL = (String) applicationProps.getOrDefault("apiUrl", "https://issue.atlassian.com");
	private static String SUB_TASK_ID = configJson.getString("rob_subtask_id");
	private static String PROJECT_KEY = configJson.getString("project_key");
	static List subTasks = new ArrayList<>() {{
		add("Backlog Refinement");
		add("Coding");
		add("Code Review");
		add("CSS Review");
		add("Testing");
		add("Scheduled");
		add("Monitoring");
	}};

	public static JSONObject createJson(String issueId, String subTask) {
		JSONObject fieldJson = new JSONObject();
		JSONObject fieldDataJson = new JSONObject();
		fieldDataJson.put("project", new JSONObject() {{
			put("key", PROJECT_KEY);
		}});
		fieldDataJson.put("parent", new JSONObject() {{
			put("key", issueId);
		}});
		fieldDataJson.put("summary", subTask);
		fieldDataJson.put("issuetype", new JSONObject() {{
			put("id", SUB_TASK_ID);
		}});
		fieldJson.put("fields", fieldDataJson);
		return fieldJson;
	}

	public static void createTasks(String issueId) {
		System.out.println("==========> Starting ROB Sub-Task creation for "+issueId);
		subTasks.forEach(taskName -> {
			try {
				MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
				RequestBody body = RequestBody.create(mediaType, createJson(issueId, taskName.toString()).toString());

				System.out.println(Helpers.doPostAPICall(ISSUE_URL, body));
			}
			catch(Exception e) {
				throw new RuntimeException(e);
			}
		});
		System.out.println("==========> Completed ROB Sub-Task Creation for "+issueId);
	}

}
