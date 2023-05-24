import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

import org.json.JSONObject;

public class Main {

	public static String assigneeList = "";
	public static String statusList = "";
	public static void main(String[] args) {
		//mainTask();
	}

	public static void mainTask(){

		String encodedApiKey = "ZGluZXNoLnJAY29tbWVyY2VpcS5haTpBVEFUVDN4RmZHRjBoWUFMeGUta0hVaGFhSURTWDVkZkstaHRMR2R5bVlGYUJuYVpkbG1wY1JmVGpXRWRLeE9uSFNSa2J4eWtZeEdqQjRUN01mZmt3MURGUVdCUDl0MXFsMjAxQmRWV3JyVC1JS3RyOGkySEtPUk1TMC0wVmRxMkNNWFdia0FuT3g3SlVfTTduN1JHM2hMQ0F3Z0tPVW5RN242UnhHcktjV1hxN2swTHlfaVlSOXM9MkNGRkExQzU=";

		try {
			System.out.println("Reading the file...\n");
			File myObj = new File("ticketIds.txt");
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				String ticket = myReader.nextLine();
				System.out.println("Making API call for the ID : "+ticket);
				if(!(ticket.equals("") || ticket.equals(null))){
					HttpRequest request = HttpRequest.newBuilder()
						.uri(URI.create("https://efundamentals.atlassian.net/rest/api/3/issue/"+ticket.trim()))
						.header("Authorization", "Basic "+encodedApiKey)
						.method("GET", HttpRequest.BodyPublishers.noBody())
						.build();
					doAPICall(request);
				}else{
					assigneeList+="\n";
					statusList+="\n";
				}
			}
			myReader.close();
			System.out.println("File Operation completed...\n\n");
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}

		System.out.println("Assignees\n\n"+assigneeList);
		System.out.println("Status\n\n"+statusList);
	}

	public static void doAPICall(HttpRequest request){
		HttpResponse<String> response = null;
		try {
			response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		JSONObject resultObj = new JSONObject(response.body());
		String resultStr = resultObj.toString();
		JSONObject resultObj2 =  new JSONObject(resultStr);
		if(resultObj.has("fields")){
			JSONObject fieldObj = (JSONObject) resultObj.get("fields");
			JSONObject statusObj = (JSONObject) fieldObj.get("status");
			statusList+=(statusObj.get("name").equals("Request Raised")?"TODO\n": (String) statusObj.get("name")+"\n");
		}
		if(resultObj2.has("fields")){
			JSONObject fieldObj1 = (JSONObject) resultObj2.get("fields");
			//System.out.println(fieldObj1);
			JSONObject statusObj1 = fieldObj1.isNull("assignee")?null: (JSONObject) fieldObj1.get("assignee");
			assigneeList+=(statusObj1!=null? (String) statusObj1.get("displayName")+"\n" :"Unassigned\n");
		}
	}
}