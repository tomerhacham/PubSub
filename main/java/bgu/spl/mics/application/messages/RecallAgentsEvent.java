package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

import java.util.LinkedList;
import java.util.List;

public class RecallAgentsEvent implements Event {
    //fields:
    private List<String> serialAgentsNumbers;
    private String sender;
    private String receiver;
    //constructors:
    public RecallAgentsEvent(List<String> serialAgentsNumbers){
        this.serialAgentsNumbers=serialAgentsNumbers;
    }
    public RecallAgentsEvent(){};
    //methods:
    public List<String> GetSerialAgentsNumbers(){
        return this.serialAgentsNumbers;
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

    @Override
    public String toString() {
        return "RecallAgentsEvent{" +
                "sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                '}';
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
}
