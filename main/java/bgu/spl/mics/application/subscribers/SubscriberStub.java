package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Event;
import bgu.spl.mics.Subscriber;

import java.util.Queue;

public class SubscriberStub extends Subscriber {

    /**
     * @param name the Subscriber name (used mainly for debugging purposes -
     *             does not have to be unique)
     */
    public SubscriberStub(String name) {
        super(name);
    }

    @Override
    protected void initialize() {

    }

}
