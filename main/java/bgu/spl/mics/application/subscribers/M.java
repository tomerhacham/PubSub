package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Future;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.MissionInfo;
import bgu.spl.mics.application.passiveObjects.Report;

import java.util.List;

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

	//Constructor:
	public M(int id,int duration) {
		super("M");
		this.id=id;
		this.duration=duration;
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
			if(FutureQ.get()!=-1){
				AgentsAvailableEvent getAgents = new AgentsAvailableEvent(income_mission.getMissionInfo().getSerialAgentsNumbers());
				Future<Integer> FutureMP = getSimplePublisher().sendEvent(getAgents);
				MissionInfo missionInfo = income_mission.getMissionInfo();
				if(FutureMP!=null){
					if(missionInfo.getTimeIssued()==tickM && tickM+missionInfo.getDuration()<=duration){
						SendAgentsEvent ExecuteMission = new SendAgentsEvent(missionInfo.getSerialAgentsNumbers(),missionInfo.getDuration());
						Future<List<String>> agentsNames = getSimplePublisher().sendEvent(ExecuteMission);
						if(agentsNames!=null){
							Report report= new Report();
							report.setMissionName(missionInfo.getMissionName());
							report.setM(this.id);
							report.setMoneypenny(FutureMP.get());
							report.setAgentsSerialNumbersNumber(missionInfo.getSerialAgentsNumbers());
							report.setAgentsNames(agentsNames.get());
							report.setTimeIssued(missionInfo.getTimeIssued());
							report.setQTime(FutureQ.get());
							report.setTimeCreated(tickM);
							diary.addReport(report);
						}
					}
					else {
						RecallAgentsEvent MissionFail= new RecallAgentsEvent(missionInfo.getSerialAgentsNumbers());
					}
				}
			}
		});
	}

}
