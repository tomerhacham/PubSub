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
		diary.increment();
		subscribeBroadcast(TickBroadcast.class , br -> {
			if (br.isTermminate())
			{ terminate(); }
			if(br.getTickNum()>=0){
				tickM=br.getTickNum();
				}
		});

		subscribeEvent(MissionReceivedEvent.class, income_mission->{
			GadgetAvailableEvent getGadet = new GadgetAvailableEvent(income_mission.getMissionInfo().getGadget());
			Future<Integer> FutureQ = getSimplePublisher().sendEvent(getGadet);
			if(FutureQ.get()!=-1) {
				AgentsAvailableEvent getAgents = new AgentsAvailableEvent(income_mission.getMissionInfo().getSerialAgentsNumbers());
				Future<Integer> FutureMP = getSimplePublisher().sendEvent(getAgents);
				MissionInfo missionInfo = income_mission.getMissionInfo();

				if (FutureMP != null) {

					boolean missioncomplete = false;

					if (tickM < missionInfo.getTimeExpired()) {

						missioncomplete = true;
						SendAgentsEvent sendAgents = new SendAgentsEvent(income_mission.getMissionInfo().getSerialAgentsNumbers(), missionInfo.getDuration());
						Future<List<String>> agentsName = getSimplePublisher().sendEvent(sendAgents);
						if (agentsName.get() != null) {

							Report report = new Report();

							report.setMissionName(missionInfo.getMissionName());
							report.setM(this.id);
							report.setMoneypenny(FutureMP.get());
							report.setAgentsSerialNumbersNumber(missionInfo.getSerialAgentsNumbers());
							report.setAgentsNames(agentsName.get());
							report.setTimeIssued(missionInfo.getTimeIssued());
							report.setQTime(FutureQ.get());
							report.setTimeCreated(tickM);
							diary.addReport(report);
						}

					}
					if (missioncomplete && missionInfo.getDuration() + tickM > duration) {
						RecallAgentsEvent MissionFail = new RecallAgentsEvent(missionInfo.getSerialAgentsNumbers());
					}
				}
			}
		});
		System.out.println("M "+id+" is UP");
		countdown.countDown();
	}

}
