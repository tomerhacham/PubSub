package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Future;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.SyncInitialize;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.MissionInfo;
import bgu.spl.mics.application.passiveObjects.Report;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * M handles ReadyEvent - fills a report and sends agents to mission.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class M extends Subscriber {

	//Fields:
	private Integer id;
	private int tickM;
	private int duration;
	private CountDownLatch countdown;

	//Constructor:
	public M(int id,int duration, CountDownLatch countdown) {
		super("M"+id);
		this.id=id;
		this.duration=duration;
		this.countdown=countdown;
	}

	@Override
	protected void initialize() {
		Diary diary= Diary.getInstance();
		//region Broadcast handler
		subscribeBroadcast(TickBroadcast.class , br -> {
			if (br.isTermminate())
			{
				MwentHome mwentHome = new MwentHome();
				getSimplePublisher().sendBroadcast(mwentHome);
				terminate(); }
			if(br.getTickNum()>=0){
				tickM=br.getTickNum();
				System.out.println(Thread.currentThread().getName()+" received broadcast at time "+tickM);
			}
		});
		//endregion

		//region MissionReceivedEvent handler
		subscribeEvent(MissionReceivedEvent.class, income_mission->{
			System.out.println("tickM = "+tickM);
			diary.increment();
			income_mission.setReceiver(Thread.currentThread().getName());
			System.out.println(income_mission);
			AgentsAvailableEvent getAgents = new AgentsAvailableEvent(income_mission.getMissionInfo().getSerialAgentsNumbers());
			getAgents.setSender(Thread.currentThread().getName());
			Future<Integer> FutureMP = getSimplePublisher().sendEvent(getAgents);
			FutureMP.setKind("FutureMP");
			System.out.println(getAgents);
			MissionInfo missionInfo = income_mission.getMissionInfo();
			Integer MP= FutureMP.get();//missionInfo.getDuration(),TimeUnit.MILLISECONDS);

			//region Agents are available scenario
			if (MP != null) {
				System.out.println("tick: " + tickM + " " + Thread.currentThread().getName() + " received AgentAvailableEvent: " + income_mission.getMissionInfo().getSerialAgentsNumbers().toString());
				GadgetAvailableEvent getGadet = new GadgetAvailableEvent(income_mission.getMissionInfo().getGadget());
				getGadet.setSender(Thread.currentThread().getName());
				System.out.println(getGadet);
				Future<Integer> FutureQ = getSimplePublisher().sendEvent(getGadet);
				FutureQ.setKind("FutureQ");
				System.out.println(Thread.currentThread().getName() + " sent GadgetAvailableEvent: " + getGadet.getRequested_gadget());
				Integer Q = FutureQ.get();

				//region Gadget is available scenario
				if (Q != null) {
					System.out.println(Thread.currentThread().getName() + "received Gadget:" + missionInfo.getGadget());
					boolean missioncomplete = false;

					if (Q < duration && Q <= missionInfo.getTimeExpired()) {
						//region Execute Mission
						int toSend = 0;
						if (Q + missionInfo.getDuration() > duration) {
							toSend = (duration - Q);
						} else {
							toSend = missionInfo.getDuration();
						}
						SendAgentsEvent sendAgents = new SendAgentsEvent(income_mission.getMissionInfo().getSerialAgentsNumbers(), missionInfo.getDuration());//toSend);
						sendAgents.setSender(Thread.currentThread().getName());
						System.out.println(sendAgents);

						Future<List<String>> FutureSendAgents = getSimplePublisher().sendEvent(sendAgents);
						System.out.println(Thread.currentThread().getName() + " ASKED to send the agents to execute mission");
						List<String> agentsName = FutureSendAgents.get();
						if (agentsName != null) {
							Report report = new Report();
							report.setGadgetName(getGadet.getRequested_gadget());
							report.setMissionName(missionInfo.getMissionName());
							report.setM(this.id);
							report.setMoneypenny(MP);
							report.setAgentsSerialNumbersNumber(missionInfo.getSerialAgentsNumbers());
							report.setAgentsNames(agentsName);
							report.setTimeIssued(missionInfo.getTimeIssued());
							//System.out.println("*******" + Q + "********");
							report.setQTime(Q);
							report.setTimeCreated(tickM);
							System.out.println(report);
							diary.addReport(report);
							System.out.println("REPORT CREATED");
							complete(income_mission, true);
						}
					} else {
						RecallAgentsEvent MissionFail = new RecallAgentsEvent(missionInfo.getSerialAgentsNumbers());
						MissionFail.setSender(Thread.currentThread().getName());
						this.getSimplePublisher().sendEvent(MissionFail);
						System.out.println(MissionFail);
						System.out.println(Thread.currentThread().getName() + " send RecallAgentsEvent - due time issue");
					}
				} else {
					RecallAgentsEvent MissionFail = new RecallAgentsEvent(missionInfo.getSerialAgentsNumbers());
					MissionFail.setSender(Thread.currentThread().getName());
					this.getSimplePublisher().sendEvent(MissionFail);
					System.out.println(MissionFail);
					System.out.println(Thread.currentThread().getName() + " send RecallAgentsEvent - due gadget issue");
				}

				//endregion
			} else {
				System.out.println("M did not received " + missionInfo.getSerialAgentsNumbers());
				complete(income_mission, false);
			}
		});
		//endregion

		System.out.println("M "+id+" is UP");
		countdown.countDown();
	}

}
