package bgu.spl.mics.application.passiveObjects;

import bgu.spl.mics.application.subscribers.M;

import java.util.List;

/**
 * Passive data-object representing a delivery vehicle of the store.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class Report {

	private String MissionName;
	private int M;
	private int Moneypenny;
	private List<String> AgentsSerialNumbers;
	private List<String> AgentsNames;
	private String GadgetName;
	private int TimeIssued;
	private int QTime;
	private int TimeCreated;

	public Report (String missionName, int m, int moneypenny, List<String> agentsSerialNumbers, List<String> agentsNames, String gadgetName, int timeIssued, int QTime,int timeCreated){
		this.MissionName= missionName;
		this.M= m;
		this.Moneypenny= moneypenny;
		this.AgentsSerialNumbers= agentsSerialNumbers;
		this.AgentsNames= agentsNames;
		this.GadgetName =gadgetName;
		this.TimeIssued= timeIssued;
		this.QTime= QTime;
		this.TimeCreated= timeCreated;
	}
	/**
     * Retrieves the mission name.
     */
	public String getMissionName() {
		return this.MissionName;
	}

	/**
	 * Sets the mission name.
	 */
	public void setMissionName(String missionName) {
		this.MissionName= missionName;
	}

	/**
	 * Retrieves the M's id.
	 */
	public int getM() {
		return M;
	}

	/**
	 * Sets the M's id.
	 */
	public void setM(int m) {
		this.M= m;
	}

	/**
	 * Retrieves the Moneypenny's id.
	 */
	public int getMoneypenny() {
		return this.Moneypenny;
	}

	/**
	 * Sets the Moneypenny's id.
	 */
	public void setMoneypenny(int moneypenny) {
		this.Moneypenny= moneypenny;
	}

	/**
	 * Retrieves the serial numbers of the agents.
	 * <p>
	 * @return The serial numbers of the agents.
	 */
	public List<String> getAgentsSerialNumbersNumber() {
		return this.AgentsSerialNumbers;
	}

	/**
	 * Sets the serial numbers of the agents.
	 */
	public void setAgentsSerialNumbersNumber(List<String> agentsSerialNumbersNumber) {
		this.AgentsSerialNumbers= agentsSerialNumbersNumber;
	}

	/**
	 * Retrieves the agents names.
	 * <p>
	 * @return The agents names.
	 */
	public List<String> getAgentsNames() {
		return this.AgentsNames;
	}

	/**
	 * Sets the agents names.
	 */
	public void setAgentsNames(List<String> agentsNames) {
		this.AgentsNames= agentsNames;
	}

	/**
	 * Retrieves the name of the gadget.
	 * <p>
	 * @return the name of the gadget.
	 */
	public String getGadgetName() {
		return this.GadgetName;
	}

	/**
	 * Sets the name of the gadget.
	 */
	public void setGadgetName(String gadgetName) {
		this.GadgetName= gadgetName;
	}

	/**
	 * Retrieves the time-tick in which Q Received the GadgetAvailableEvent for that mission.
	 */
	public int getQTime() {
		return this.QTime;
	}

	/**
	 * Sets the time-tick in which Q Received the GadgetAvailableEvent for that mission.
	 */
	public void setQTime(int qTime) {
		this.QTime= qTime;
	}

	/**
	 * Retrieves the time when the mission was sent by an Intelligence Publisher.
	 */
	public int getTimeIssued() {
		return this.TimeIssued;
	}

	/**
	 * Sets the time when the mission was sent by an Intelligence Publisher.
	 */
	public void setTimeIssued(int timeIssued) {
		this.TimeIssued= timeIssued;
	}

	/**
	 * Retrieves the time-tick when the report has been created.
	 */
	public int getTimeCreated() {
		return this.TimeCreated;
	}

	/**
	 * Sets the time-tick when the report has been created.
	 */
	public void setTimeCreated(int timeCreated) {
		this.TimeCreated= timeCreated;
	}
}
