package bgu.spl.mics.application.passiveObjects;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Passive object representing the diary where all reports are stored.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private fields and methods to this class as you see fit.
 */
public class Diary {

	private static class SingeltonHolder{
		private static Diary instance = new Diary();
	}
	//Fields:
	private List<Report> reports; //only executed missions will be reported here
	private AtomicInteger total; //total number of received missions
	/**
	 * Retrieves the single instance of this class.
	 */
	private Diary()
	{
		reports = new LinkedList<>();
		total=new AtomicInteger(0);

	}

	public static Diary getInstance() {
		return SingeltonHolder.instance;
	}

	public List<Report> getReports() {
		return reports;
	}

	/**
	 * adds a report to the diary
	 * @param reportToAdd - the report to add
	 */
	public void addReport(Report reportToAdd){
		reports.add(reportToAdd);
	}

	/**
	 *
	 * <p>
	 * Prints to a file name @filename a serialized object List<Report> which is a
	 * List of all the reports in the diary.
	 * This method is called by the main method in order to generate the output.
	 */
	public void printToFile(String filename){
		JSONObject finalDiary = new JSONObject();

		JSONArray reports = new JSONArray();

		int missinsarray_index = 0;
		for(Report report : this.reports){

			JSONObject report_json_object = new JSONObject();

			JSONArray agentsSerialNumber = new JSONArray();
			int agentarray_index = 0;
			for(String serialNumber : report.getAgentsSerialNumbersNumber()){
				agentsSerialNumber.add(agentarray_index, serialNumber);
				agentarray_index++;
			}

			JSONArray agentsNames = new JSONArray();
			agentarray_index = 0;
			for(String agentName : report.getAgentsNames()){
				agentsNames.add(agentarray_index,agentName);
				agentarray_index++;
			}

			fillReport(report, report_json_object, agentsSerialNumber, agentsNames);

			reports.add(missinsarray_index, report_json_object);
			missinsarray_index++;
		}

		finalDiary.put("reports",reports);
		finalDiary.put("total",total);

		String prettyJsonString = getBeautifulString(finalDiary);

		try (FileWriter file = new FileWriter(filename + ".json")) {
			file.write(prettyJsonString);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String getBeautifulString(JSONObject finalDiary) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(finalDiary.toJSONString());
		return gson.toJson(je);
	}

	private void fillReport(Report report, JSONObject report_json_object, JSONArray agentsSerialNumber, JSONArray agentsNames) {
		report_json_object.put("agentsSerialNumbers",agentsSerialNumber);
		report_json_object.put("missionName",report.getMissionName());
		report_json_object.put("m",report.getM());
		report_json_object.put("moneypenny",report.getMoneypenny());
		report_json_object.put("agentsName",agentsNames);
		report_json_object.put("gadgetName",report.getGadgetName());
		report_json_object.put("timeCreated",report.getTimeCreated());
		report_json_object.put("timeIssued",report.getTimeIssued());
		report_json_object.put("qTime",report.getQTime());
	}

	/**
	 * Gets the total number of received missions (executed / aborted) be all the M-instances.
	 * @return the total number of received missions (executed / aborted) be all the M-instances.
	 */
	public int getTotal(){
		return total.get();
	}

	public void increment() {
		total.incrementAndGet();
		;
	}
}
