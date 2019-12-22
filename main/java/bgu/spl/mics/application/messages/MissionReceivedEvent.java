package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.Message;
import bgu.spl.mics.application.passiveObjects.MissionInfo;

public class MissionReceivedEvent implements Event {
    //fields:
    private MissionInfo missionInfo;
    // constructor:
    public MissionReceivedEvent(MissionInfo missionInfo) {
        this.missionInfo = missionInfo;
    }
    // methods:
    public MissionInfo getMissionInfo(){
        return this.missionInfo;
    }
}