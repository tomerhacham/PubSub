package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.Message;

public class GadgetAvailableEvent implements Event {
    //fields:
    String Gadget;
    //constructor:
    public GadgetAvailableEvent(String gadget){this.Gadget= gadget;}
    //methods:
    public String getGadget(){
        return this.Gadget;
    }

}

