package bgu.spl.mics.application.passiveObjects;

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

		int missionIndex = 0;
		for(Report OurReport : this.reports){

			JSONObject report = new JSONObject();

			report.put("missionName",OurReport.getMissionName());
			report.put("m",OurReport.getM());
			report.put("moneypenny",OurReport.getMoneypenny());

			JSONArray agentsSerialNumber = new JSONArray();
			int agentIndex = 0;
			for(String agentSerialNumber : OurReport.getAgentsSerialNumbersNumber()){
				agentsSerialNumber.add(agentIndex,agentSerialNumber);
				agentIndex++;
			}
			report.put("agentsSerialNumbers",agentsSerialNumber);

			JSONArray agentsNames = new JSONArray();
			agentIndex = 0;
			for(String agentName : OurReport.getAgentsNames()){
				agentsSerialNumber.add(agentIndex,agentName);
				agentIndex++;
			}
			report.put("agentsName",agentsNames);

			report.put("gadgetName",OurReport.getGadgetName());
			report.put("timeCreated",OurReport.getTimeCreated());
			report.put("timeIssued",OurReport.getTimeIssued());
			report.put("qTime",OurReport.getQTime());

			reports.add(missionIndex,report);
			missionIndex++;
		}


		finalDiary.put("reports",reports);
		finalDiary.put("total",total);

		try (FileWriter file = new FileWriter(filename + ".json")) {
			file.write(finalDiary.toJSONString());
			file.flush();
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
