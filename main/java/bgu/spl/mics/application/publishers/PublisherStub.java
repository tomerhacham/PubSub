package bgu.spl.mics.application.publishers;

import bgu.spl.mics.MessageBroker;
import bgu.spl.mics.Publisher;

public class PublisherStub extends Publisher{
    /**
     * @param name the Publisher name (used mainly for debugging purposes -
     *             does not have to be unique)
     */
    public PublisherStub(String name ) {
        super(name);
    }

    @Override
    protected void initialize() {

    }

    @Override
    public void run() {

    }
}
