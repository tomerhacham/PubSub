package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.Message;

public class GadgetAvailableEvent implements Event<String> {
    //fields:
    String requested_gadget;
    Future<String> future = new Future<>();

    //constructor:
    public GadgetAvailableEvent(String gadget)
    {this.requested_gadget=gadget;}
    //methods:


    public String getRequested_gadget() {return requested_gadget;}
    public Future<String> getFuture(){
        return this.future;
    }

}

