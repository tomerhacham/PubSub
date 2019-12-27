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

		subscribeBroadcast(TickBroadcast.class , br -> {
			if (br.isTermminate())
			{ terminate(); }
			if(br.getTickNum()>=0){
				tickM=br.getTickNum();
				System.out.println(Thread.currentThread().getName()+" received broadcast at time "+tickM);
			}
		});

		subscribeEvent(MissionReceivedEvent.class, income_mission->{
//			income_mission.setReceiver(Thread.currentThread().getName());
//			System.out.println(income_mission);
//			AgentsAvailableEvent agentsAvailableEvent = new AgentsAvailableEvent(income_mission.getMissionInfo().getSerialAgentsNumbers());
//			agentsAvailableEvent.setSender(Thread.currentThread().getName());
//			Future<Integer> future_AgentAvailableEvent = getSimplePublisher().sendEvent(agentsAvailableEvent);
//
//			GadgetAvailableEvent gadgetAvailableEvent = new GadgetAvailableEvent(income_mission.getMissionInfo().getGadget());
//			gadgetAvailableEvent.setSender(Thread.currentThread().getName());
//			Future<Integer> future_gadgetAvailableEvent = getSimplePublisher().sendEvent(gadgetAvailableEvent);
//
//
//			if(future_AgentAvailableEvent.get()!= null){
//				if(future_gadgetAvailableEvent.get()>0) {
//					if(tickM<=income_mission.getMissionInfo().getTimeExpired()) {
//						SendAgentsEvent sendAgentsEvent = new SendAgentsEvent(income_mission.getMissionInfo().getSerialAgentsNumbers(), income_mission.getMissionInfo().getDuration());
//						sendAgentsEvent.setSender(Thread.currentThread().getName());
//						getSimplePublisher().sendEvent(sendAgentsEvent);
//						//TODO: add reports details
//					}
//					else{
//						RecallAgentsEvent recallAgentsEvent = new RecallAgentsEvent(income_mission.getMissionInfo().getSerialAgentsNumbers());
//						recallAgentsEvent.setSender(Thread.currentThread().getName());
//						getSimplePublisher().sendEvent(recallAgentsEvent);
//					}
//				}
//				else{
//					RecallAgentsEvent recallAgentsEvent = new RecallAgentsEvent(income_mission.getMissionInfo().getSerialAgentsNumbers());
//					recallAgentsEvent.setSender(Thread.currentThread().getName());
//					getSimplePublisher().sendEvent(recallAgentsEvent);
//				}
//			}
			diary.increment();
			income_mission.setReceiver(Thread.currentThread().getName());
			//System.out.println("**callback:"+Thread.currentThread().getName()+ " received new MissionReceivedEvent "+income_mission.getMissionInfo().getMissionName());
			System.out.println(income_mission);
			AgentsAvailableEvent getAgents = new AgentsAvailableEvent(income_mission.getMissionInfo().getSerialAgentsNumbers());
			getAgents.setSender(Thread.currentThread().getName());
			Future<Integer> FutureMP = getSimplePublisher().sendEvent(getAgents);
			System.out.println(getAgents);
			//System.out.println(Thread.currentThread().getName()+ " sent agent available: "+ getAgents.hashCode());
			MissionInfo missionInfo = income_mission.getMissionInfo();
			Integer MP= FutureMP.get();

			if(MP!= null) {
				System.out.println(Thread.currentThread().getName() + " received AgentAvailableEvent: " + income_mission.getMissionInfo().getSerialAgentsNumbers().toString());
				GadgetAvailableEvent getGadet = new GadgetAvailableEvent(income_mission.getMissionInfo().getGadget());
				getGadet.setSender(Thread.currentThread().getName());
				System.out.println(getGadet);
				Future<Integer> FutureQ = getSimplePublisher().sendEvent(getGadet);
				System.out.println(Thread.currentThread().getName() + " sent GadgetAvailableEvent: " + getGadet.getRequested_gadget());
				Integer Q= FutureQ.get();

				if (Q != -1) {
					System.out.println(Thread.currentThread().getName()+ "received Gadget:" +missionInfo.getGadget());
					boolean missioncomplete = false;

					if (tickM < missionInfo.getTimeExpired()) {

						missioncomplete = true;
						SendAgentsEvent sendAgents = new SendAgentsEvent(income_mission.getMissionInfo().getSerialAgentsNumbers(), missionInfo.getDuration());
						sendAgents.setSender(Thread.currentThread().getName());
						System.out.println(sendAgents);
						Future<List<String>> FutureSendAgents  = getSimplePublisher().sendEvent(sendAgents);
						System.out.println(Thread.currentThread().getName()+" ASKED to send the agents to execute mission");
						List<String> agentsName= FutureSendAgents.get();
						if (agentsName != null) {
							System.out.println(Thread.currentThread().getName()+" has been notify that the agents sent");
							Report report = new Report();

							report.setMissionName(missionInfo.getMissionName());
							report.setM(this.id);
							report.setMoneypenny(MP);
							report.setAgentsSerialNumbersNumber(missionInfo.getSerialAgentsNumbers());
							report.setAgentsNames(agentsName);
							report.setTimeIssued(missionInfo.getTimeIssued());
							report.setQTime(Q);
							report.setTimeCreated(tickM);
							System.out.println(report);
							diary.addReport(report);
							System.out.println("REPORT CREATED");
							report.toString();
						}

					}
					if (missioncomplete && missionInfo.getDuration() + tickM > duration) {
						RecallAgentsEvent MissionFail = new RecallAgentsEvent(missionInfo.getSerialAgentsNumbers());
						MissionFail.setSender(Thread.currentThread().getName());
						this.getSimplePublisher().sendEvent(MissionFail);
						System.out.println(MissionFail);
						System.out.println(Thread.currentThread().getName()+" send RecallAgentsEvent");
					}
				}
				else {
					RecallAgentsEvent MissionFail = new RecallAgentsEvent(missionInfo.getSerialAgentsNumbers());
					MissionFail.setSender(Thread.currentThread().getName());
					this.getSimplePublisher().sendEvent(MissionFail);
					System.out.println(MissionFail);
					System.out.println(Thread.currentThread().getName()+" send RecallAgentsEvent");
				}
			}
			else{
				System.out.println("agents isnt exist");
				System.out.println("M did not received "+income_mission.getMissionInfo().getGadget());
			}
		});
		System.out.println("M "+id+" is UP");
		countdown.countDown();
	}

}
