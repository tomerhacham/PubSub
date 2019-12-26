package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.Message;

public class TickBroadcast implements Broadcast {
    //fields:
    private int tickNum;
    private boolean termminate=false;

    //constructors:
    public TickBroadcast(int tickNum){this.tickNum=tickNum;}
    public TickBroadcast(){};

    //methods:
    public int getTickNum() {
        return tickNum;
    }

    public void setTermminate(boolean termminate) {
        this.termminate = termminate;
    }

    public boolean isTermminate() {
        return termminate;
    }
}
