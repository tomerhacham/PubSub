package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.SyncInitialize;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.passiveObjects.Squad;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
	private LinkedList<Event> events;
	private boolean isSpecial;
	private int Minstance;

	//Constructor:
	public Moneypenny(int id, CountDownLatch countdown,boolean special, int Minstance) {
		super("Moneypenny "+id);
		this.id = id;
		this.countdown=countdown;
		events = new LinkedList<Event>();
		isSpecial = special;
		this.Minstance=Minstance;
	}

	@Override
	protected void initialize() {
		//region MwentHome  handle
		subscribeBroadcast(MwentHome.class, br -> {
			Minstance--;
			System.out.println(this.getName()+" ------------------------terminate " + this.Minstance);
			if (Minstance==0) {
				terminate();
			}
		});
		//endregion

		//region Special Moneypenny
		if(!isSpecial)
		{
			//region Broadcast handle
			subscribeBroadcast(TickBroadcast.class, br -> {
				if (br.getTickNum() >= 0) {
					tickMP = br.getTickNum();
					System.out.println(Thread.currentThread().getName() + " received broadcast at time " + tickMP);
				}
			});
			//endregion

			//region AgentAvailable handle
			subscribeEvent(AgentsAvailableEvent.class, Get_Agents -> {
				Get_Agents.setReceiver(Thread.currentThread().getName());
				System.out.println(Get_Agents);
				events.add(Get_Agents);
				//System.out.println("***callback:"+Thread.currentThread().getName()+ " received AgentsAvailableEvent: "+ Get_Agents.hashCode());
				List<String> serials = Get_Agents.getSerialAgentsNumbers();
				if (squad.getAgents(Get_Agents.getSerialAgentsNumbers())) {
					System.out.println("tick:"+tickMP+" "+Thread.currentThread().getName() + " answered that the" + serials.toString() + " are Available");
					complete(Get_Agents, id);
				} else {
					System.out.println("tick:"+tickMP+" "+Thread.currentThread().getName() + " answered that the" + serials.toString() + " are NOT Available");
					complete(Get_Agents, null);
				}
			});
			//endregion
		}
		//endregion
		//region Regular Moneypenny
		else if(isSpecial)
		{
			//region Broadcast handle
			subscribeBroadcast(TickBroadcast.class, br -> {
				if (br.getTickNum() >= 0) {
					tickMP = br.getTickNum();
					System.out.println(Thread.currentThread().getName() + " received broadcast at time " + tickMP);
				}
			});
			//endregion

			//region SendAgents handle
			subscribeEvent(SendAgentsEvent.class, Send_agents -> {
				Send_agents.setReceiver(Thread.currentThread().getName());
				System.out.println(Send_agents);
				System.out.println(Thread.currentThread().getName()+"received SendAgentsEvent");
				events.add(Send_agents);
				complete(Send_agents, squad.getAgentsNames(Send_agents.GetSerialAgentsNumbers()));
				squad.sendAgents(Send_agents.GetSerialAgentsNumbers(), Send_agents.getTimeToSleep());
				System.out.println("***callback:" + Thread.currentThread().getName() + " SendAgentsEvent " + Send_agents.GetSerialAgentsNumbers().toString() + " COMPLETE");

			});
			//endregion

			//region RecallAgent handle
			subscribeEvent(RecallAgentsEvent.class, Release_agents -> {
				Release_agents.setReceiver(Thread.currentThread().getName());
				System.out.println(Release_agents);
				events.add(Release_agents);
				squad.releaseAgents(Release_agents.GetSerialAgentsNumbers());
				complete(Release_agents, true);
				System.out.println("***callback:" + Thread.currentThread().getName() + " RecallAgentsEvent COMPLETE");
			});
			//endregion
		}
		//endregion

		System.out.println("Moneypenny "+this.id+" is UP");
		countdown.countDown();
	}

	public List<Event> getEvent(){
		return this.events;
	}
}



