package bgu.spl.mics.application.messages;

import java.util.LinkedList;
import java.util.List;

public class RecallAgentsEvent {
    //fields:
    private List<String> serialAgentsNumbers;
    //constructors:
    public RecallAgentsEvent(List<String> serialAgentsNumbers){
        this.serialAgentsNumbers=serialAgentsNumbers;
    }
    //methods:
    public List<String> GetSerialAgentsNumbers(){
        return this.serialAgentsNumbers;
    }

}
