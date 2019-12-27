package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.Message;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.passiveObjects.MissionInfo;

public class MissionReceivedEvent implements Event {
    //fields:
    private MissionInfo missionInfo;
    private String sender;
    private String receiver;
    // constructor:
    public MissionReceivedEvent(MissionInfo missionInfo) {
        this.missionInfo = missionInfo;
    }

    @Override
    public String toString() {
        return "MissionReceivedEvent{" +
                "sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", MissionName='" + missionInfo.getMissionName() + '\'' +
                '}';
    }

    public MissionReceivedEvent(){};
    // methods:
    public MissionInfo getMissionInfo(){
        return this.missionInfo;
    }
    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

}