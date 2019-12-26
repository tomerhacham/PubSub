package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

import java.util.List;

    public class SendAgentsEvent implements Event{
        //fields:
        private List<String> serialAgentsNumbers;
        private  int timeToSleep;
        //constructors:
        public SendAgentsEvent(List<String> serialAgentsNumbers, int timeToSleep){
            this.serialAgentsNumbers=serialAgentsNumbers;
            this.timeToSleep= timeToSleep;
        }
        public SendAgentsEvent(){};
        //methods:
        public List<String> GetSerialAgentsNumbers(){
            return this.serialAgentsNumbers;
        }
        public int getTimeToSleep(){
            return this.timeToSleep;
        }
}
