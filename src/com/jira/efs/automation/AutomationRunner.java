package com.jira.efs.automation;

import java.io.IOException;
import java.util.Scanner;

import com.jira.efs.automation.subtask.CreateRORSubtasks;

public class AutomationRunner {
	public static void main(String[] args)
		throws IOException {
		start();
	}

	public static Scanner scanner = new Scanner(System.in);

	public static void start() {
		System.out.println("1.Create subtask\n0.Exit\n\n");
		String option = scanner.next();
		executeCase(option);
	}

	public static void executeCase(String option) {
		if(option.equals("1")) {
			System.out.println("Enter issue ID to generate all Retailer On-Boarding Request related Sub-Tasks\n");
			String issueId = scanner.next();
			CreateRORSubtasks.createTasks(issueId);
		}
		else {
			System.exit(0);
		}
	}
}
