package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.Message;

import java.util.LinkedList;
import java.util.List;

public class AgentsAvailableEvent implements Event {
    //fields:
    private List<String> serialAgentsNumbers;
    //constructor:
    public AgentsAvailableEvent(List<String> serialAgentsNumbers){
        this.serialAgentsNumbers=serialAgentsNumbers;
    }
    //methods:
    public List<String> getSerialAgentsNumbers(){
        return this.serialAgentsNumbers;
    }
}
