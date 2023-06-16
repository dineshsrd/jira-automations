package com.jira.efs.automation;

import java.io.FileInputStream;
import java.util.Properties;
import java.util.Scanner;

import org.json.JSONObject;

import com.jira.efs.automation.robTask.CreateROBTasks;
import com.jira.efs.automation.subtask.CreateRORSubtasks;

public class AutomationRunner {

	public static Properties applicationProps = new Properties();
	public static JSONObject configJson = null;

	static {
		try {
			FileInputStream inputStream = new FileInputStream("./resources/config.properties");
			applicationProps.load(inputStream);
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
		configJson = new JSONObject(applicationProps.getProperty("configJson"));
	}

	public static void main(String[] args) {
		start();
		//dummy();
	}

	public static void dummy() {
		String jsonStr = applicationProps.getProperty("jsonStr");
		JSONObject json = new JSONObject(jsonStr);
		System.out.println(json.getString("key"));
	}

	public static Scanner scanner = new Scanner(System.in);

	public static void start() {
		System.out.println("1.Create subtask\n2.Create Retailer-On-Boarding Tickets\n0.Exit\n\n");
		String option = scanner.next();
		executeCase(option);
	}

	public static void executeCase(String option) {
		if(option.equals("1")) {
			System.out.println("Enter issue ID to generate all Retailer On-Boarding Request related Sub-Tasks\n");
			String issueId = scanner.next();
			CreateRORSubtasks.createTasks(issueId);
		}
		else if(option.equals("2")) {
			System.out.println("Enter the BOM ticket ID to generate Retailer On-Boarding Request Tickets\n");
			String bomId = scanner.next();
			CreateROBTasks.createROBTickets(bomId);
		}
		else {
			System.exit(0);
		}
	}
}
