package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

import java.util.List;

public class AgentsAvailableEvent implements Event {
    //fields:
    private List<String> serialAgentsNumbers;
    private String sender;
    private String receiver;
    //constructor:
    public AgentsAvailableEvent(List<String> serialAgentsNumbers){
        this.serialAgentsNumbers=serialAgentsNumbers;
    }
    public AgentsAvailableEvent(){};
    //methods:
    public List<String> getSerialAgentsNumbers(){
        return this.serialAgentsNumbers;
    }
    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    @Override
    public String toString() {
        return "AgentsAvailableEvent{" +
                "sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", requestedAgents='" + serialAgentsNumbers.toString() + '\'' +
                '}';
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
}
