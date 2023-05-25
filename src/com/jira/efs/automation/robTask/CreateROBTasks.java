package com.jira.efs.automation.robTask;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;

import com.jira.efs.automation.Helpers;
import com.jira.efs.automation.subtask.CreateRORSubtasks;

import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.jira.efs.automation.AutomationRunner.applicationProps;
import static com.jira.efs.automation.AutomationRunner.configJson;

public class CreateROBTasks {
	private static final String ISSUE_URL = (String) applicationProps.getOrDefault("apiUrl", "https://issue.atlassian.com");
	private static String ROB_TASK_ID = configJson.getString("rob_issuetype_id");
	private static String PROJECT_KEY = configJson.getString("project_key");
	public static JSONObject createJson(String bomId, String subTask, String desc) {
		JSONObject fieldJson = new JSONObject();
		JSONObject fieldDataJson = new JSONObject();
		fieldDataJson.put("project", new JSONObject() {{
			put("key", PROJECT_KEY);
		}});
		fieldDataJson.put("parent", new JSONObject() {{
			put("key", bomId);
		}});
		fieldDataJson.put("summary", subTask);
		fieldDataJson.put("description", desc);
		fieldDataJson.put("issuetype", new JSONObject() {{
			put("id", ROB_TASK_ID);
		}});
		fieldJson.put("fields", fieldDataJson);
		return fieldJson;
	}

	public static void createROBTickets(String bomId) {
		System.out.println("==========> Starting ROB Tickets creation for "+bomId);
		try {
			FileInputStream inputStream = new FileInputStream("./resources/List.xlsx");

			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

			XSSFSheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();
			boolean hasData = true;
			while(rowIterator.hasNext() && hasData) {
				Row row = rowIterator.next();

				Iterator<Cell> cellIterator = row.cellIterator();
				String retailerName = "";
				String countryCode = "";
				String siteUrl = "";
				int count = 0;
				while(cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					String cellValue = cell.getStringCellValue();
					if(count==0 && cellValue.equals("")){
						hasData = false;
						break;
					}
					if(count == 1) {
						retailerName = cellValue;
					}
					if(count == 2) {
						countryCode = cellValue;
					}
					if(count == 3) {
						siteUrl = cellValue;
					}
					count++;
					if(count == 6) { // insert the column number where the ticket URL needs to be updated
						if(!retailerName.equals("") && !countryCode.equals("") && !siteUrl.equals("")) {
							String summary = "Gather, Extract, Process " + retailerName.replace(" ", "-") + "-" + countryCode;
							String desc = summary + "\n Site: " + siteUrl + " \n" + "\n" + "Detail: \n" + "\n" + "Search: \n" + "\n" + "Category: \n" + "\n" + "Banners: No \n" + "\n" + "Multi-store: No";
							String robTicketUrl = makeAPICalls(bomId, summary, desc);
							cell.setCellValue(robTicketUrl);
						}
						break;
					}
				}

			}

			inputStream.close();
//			FileOutputStream outs = new FileOutputStream("./resources/List.xlsx");
//			workbook.write(outs);
//			outs.close();
		}
		catch(Exception exception) {
			exception.printStackTrace();
		}
		System.out.println("==========> Completed ROB Tickets creation for "+bomId);
	}

	public static String makeAPICalls(String bomId, String summary, String desc){
		String robTicketUrl = ISSUE_URL;
		try{
			MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
			RequestBody body = RequestBody.create(mediaType, createJson(bomId, summary, desc).toString());
			System.out.println("Creating ROB Ticket...");
			JSONObject subTaskData = Helpers.doPostAPICall(ISSUE_URL, body);
			robTicketUrl = subTaskData.getString("self");
			CreateRORSubtasks.createTasks(subTaskData.getString("key"));
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return robTicketUrl;
	}

}
