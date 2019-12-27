package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.Message;

public class GadgetAvailableEvent implements Event<Integer> {
    //fields:
    String requested_gadget;
    Future<Integer> future = new Future<>();
    private String sender;
    private String receiver;

    //constructor:
    public GadgetAvailableEvent(String gadget)
    {this.requested_gadget=gadget;}

    public GadgetAvailableEvent(){};
    //methods:

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
        return "GadgetAvailableEvent{" +
                "sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", gadget='" + requested_gadget + '\'' +
                '}';
    }

    public String getRequested_gadget() {return requested_gadget;}
    public Future<Integer> getFuture(){
        return this.future;
    }

}

