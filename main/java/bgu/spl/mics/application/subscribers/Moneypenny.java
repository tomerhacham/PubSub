package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.SyncInitialize;
import bgu.spl.mics.application.messages.AgentsAvailableEvent;
import bgu.spl.mics.application.messages.RecallAgentsEvent;
import bgu.spl.mics.application.messages.SendAgentsEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.passiveObjects.Squad;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Only this type of Subscriber can access the squad.
 * Three are several Moneypenny-instances - each of them holds a unique serial number that will later be printed on the report.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Moneypenny extends Subscriber {
	//Fields:
	private Integer id;
	private int tickMP;
	private CountDownLatch countdown;
	private Squad squad = Squad.getInstance();

	//Constructor:
	public Moneypenny(int id, CountDownLatch countdown) {
		super("Moneypenny "+id);
		this.id = id;
		this.countdown=countdown;
	}

	@Override
	protected void initialize() {
		subscribeBroadcast(TickBroadcast.class, br -> {
			if (br.isTermminate()) {
				terminate();
			}
			if (br.getTickNum() >= 0) {
				tickMP = br.getTickNum();
				System.out.println(Thread.currentThread().getName()+" received broadcast at time "+tickMP);
			}
		});
		subscribeEvent(AgentsAvailableEvent.class, Get_Agents -> {
			System.out.println(Thread.currentThread().getName()+ " recieved agents aviable: "+ Get_Agents.hashCode());
			List<String> serials= Get_Agents.getSerialAgentsNumbers();
			if (squad.getAgents(serials)) {
				complete(Get_Agents, id);
			}
		});
		subscribeEvent(RecallAgentsEvent.class, Release_agents -> {
			squad.releaseAgents(Release_agents.GetSerialAgentsNumbers());
			complete(Release_agents, true);
		});

		subscribeEvent(SendAgentsEvent.class, Send_agents-> {
			squad.releaseAgents(Send_agents.GetSerialAgentsNumbers());
			complete(Send_agents, squad.getAgentsNames(Send_agents.GetSerialAgentsNumbers()));
		});
		System.out.println("Moneypenny "+this.id+" is UP");
		countdown.countDown();
	}
}



