package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.MissionReceivedEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.MissionInfo;

import java.sql.SQLOutput;
import java.util.List;
import java.util.concurrent.CountDownLatch;

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
	private CountDownLatch countdown;


	public Intelligence(List<MissionInfo> mission, CountDownLatch countdown) {
		super("Intelligence");
		this.missions=mission;
		this.countdown=countdown;
	}

	@Override
	protected void initialize() {
		subscribeBroadcast(TickBroadcast.class , br -> {
			if (br.isTermminate())
			{ terminate(); }
			if(br.getTickNum()>=0){
				tick=br.getTickNum();
				System.out.println(Thread.currentThread().getName()+" received broadcast at time "+tick);
				MissionReceivedEvent eventToSend=null;
				for (MissionInfo missioninfo:missions) {
					if (missioninfo.getTimeIssued()==tick){
						eventToSend = new MissionReceivedEvent(missioninfo);
						missions.remove(missioninfo);
						break;
					}

				}
				if(eventToSend!=null){
					super.getSimplePublisher().sendEvent(eventToSend);
					System.out.println(Thread.currentThread().getName()+" sent a mission: "+ eventToSend.hashCode());
				}
			}
		});
		System.out.println("Intelligence is UP");
		countdown.countDown();
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
