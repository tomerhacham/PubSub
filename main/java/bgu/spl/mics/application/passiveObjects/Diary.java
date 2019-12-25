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
		JSONArray reportDetails = new JSONArray();
		reportDetails.addAll(reports);
		JSONObject reports = new JSONObject();
		reports.put("reports",reportDetails);
		JSONObject total = new JSONObject();
		total.put("total",total);
		JSONArray Diary = new JSONArray();
		Diary.add(reports);
		Diary.add(total);

		//Write JSON file
		try (FileWriter file = new FileWriter(filename+".json")) {

			file.write(Diary.toJSONString());
			file.flush();

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
