package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Publisher;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.SyncInitialize;
import bgu.spl.mics.application.messages.MissionReceivedEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.MissionInfo;

import java.util.LinkedList;
import java.util.List;

/**
 * A Publisher only.
 * Holds a list of Info objects and sends them
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Intelligence extends Subscriber {
	private int tick=0;
	private List<MissionInfo> missions;


	public Intelligence(List<MissionInfo> mission) {
		super("Intelligence");
		this.missions=mission;
	}

	@Override
	protected void initialize() {
		//SyncInitialize.getInstance().addInit();
		subscribeBroadcast(TickBroadcast.class , br -> {
			if (br.isTermminate())
			{ terminate(); }
			if(br.getTickNum()>=0){
				tick=br.getTickNum();
				MissionReceivedEvent eventToSend = checkForMissionAtTick(tick);
				if(eventToSend.getMissionInfo()!=null){
					super.getSimplePublisher().sendEvent(eventToSend);
				}
			}
		});
	}

	/**
	 * method will check is there is any mission available for the specific tick and if so will create an MissionReceivedEvent
	 * @param tick
	 * @return
	 */
	private MissionReceivedEvent checkForMissionAtTick(int tick){
		MissionReceivedEvent missionReceivedEvent=null;
		for (MissionInfo missioninfo:missions)
		{
			if(missioninfo.getTimeIssued()==tick){
				missionReceivedEvent = new MissionReceivedEvent(missioninfo);
			}
		}
		return missionReceivedEvent;
	}

}
