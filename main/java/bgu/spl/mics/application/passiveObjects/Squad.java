package bgu.spl.mics.application.passiveObjects;
import java.util.*;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

/**
 * Passive data-object representing a information about an agent in MI6.
 * You must not alter any of the given public methods of this class. 
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class Squad {

	//Fields:
	private Map<String, Agent> agents;
	private static Squad squad = null;
	private static class SingeltonHolder{
		private static Squad instance = new Squad();
	}

	//Constructor:
	/**
	 * private constructor for the use of getInstance method only
	 */
	private Squad(){
		this.agents= new ConcurrentHashMap<>();
	}

	//Methods:
	/**
	 * Retrieves the single instance of this class.
	 */
	public static Squad getInstance() {
		return SingeltonHolder.instance;
	}

	/**
	 * Initializes the squad. This method adds all the agents to the squad.
	 * <p>
	 * @param agents 	Data structure containing all data necessary for initialization
	 * 						of the squad.
	 */
	public void load (Agent[] agents) {
		for (Agent agent: agents) {
			this.agents.put(agent.getSerialNumber(),agent);
		}
	}

	/**
	 * Releases agents.
	 */
	public void releaseAgents(List<String> serials){
		Collections.sort(serials);
		for (String SerialNum:serials) {
			synchronized (this.agents.get(SerialNum)) {
				this.agents.get(SerialNum).release();
			}
		}
	}

	/**
	 * simulates executing a mission by calling sleep.
	 * @param time   milliseconds to sleep
	 */
	public void sendAgents(List<String> serials, int time){
		Collections.sort(serials);
		try {
			//System.out.println("Execute Mission");
			sleep(time*100);
			releaseAgents(serials);
		//	System.out.println(Thread.currentThread().getName()+" is AWAKE***********************");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * acquires an agent, i.e. holds the agent until the caller is done with it
	 * @param serials   the serial numbers of the agents
	 * @return ‘false’ if an agent of serialNumber ‘serial’ is missing, and ‘true’ otherwise
	 */
	public boolean getAgents(List<String> serials){
		Collections.sort(serials);
		for (String serial: serials){
			//System.out.println(" check if agent is exist");
			if(!agents.containsKey(serial)){
				System.out.println("agent isnt exist - hadar");
				return false;
			}
		}
			for (String serial: serials){
				Agent agent= agents.get(serial);
				synchronized(agent) {
					while (!agent.isAvailable()) {
						//System.out.println(agent.getName()+" is not available");
						try {
							agent.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					agent.acquire();
				}
				//System.out.println(agent.Name+" acquired");
			}
			return true;
	}

    /**
     * gets the agents names
     * @param serials the serial numbers of the agents
     * @return a list of the names of the agents with the specified serials.
     */
    public  List<String> getAgentsNames(List<String> serials){
		Collections.sort(serials);
		LinkedList<String> group= new LinkedList<>() ;
    	for (String serial: serials){
			group.add(this.agents.get(serial).getName());
		}

	    return group;
    }

    public List<String> getAgentsSerials(){
		LinkedList<String> serials= new LinkedList<>() ;
		for (String serial: agents.keySet()){
			serials.add(serial);
		}
		return serials;
	}

}
