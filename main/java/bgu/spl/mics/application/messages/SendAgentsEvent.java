package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

import java.util.List;

    public class SendAgentsEvent implements Event{
        //fields:
        private List<String> serialAgentsNumbers;
        private  int timeToSleep;
        private String sender;
        private String receiver;
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

        @Override
        public String toString() {
            return "SendAgentsEvent{" +
                    "sender='" + sender + '\'' +
                    ", receiver='" + receiver + '\'' +
                    ", SendAgents='" + serialAgentsNumbers.toString() + '\'' +
                    '}';
        }
    }
