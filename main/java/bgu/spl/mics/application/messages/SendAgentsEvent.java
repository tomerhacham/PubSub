package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

import java.util.List;

    public class SendAgentsEvent implements Event{
        //fields:
        private List<String> serialAgentsNumbers;
        int duration;
        //constructors:
        public SendAgentsEvent(List<String> serialAgentsNumbers, int duration){
            this.serialAgentsNumbers=serialAgentsNumbers;
        }
        //methods:
        public List<String> GetSerialAgentsNumbers(){
            return this.serialAgentsNumbers;
        }
}
