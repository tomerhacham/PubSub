package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.Message;

import java.util.LinkedList;

public class AgentsAvailableEvent implements Event {
    //fields:
    private LinkedList<String> serialAgentsNumbers;
    //constructor:
    public AgentsAvailableEvent(LinkedList<String> serialAgentsNumbers){
        this.serialAgentsNumbers=serialAgentsNumbers;
    }
    //methods:
    public LinkedList<String> getSerialAgentsNumbers(){
        return this.serialAgentsNumbers;
    }
}
