package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.EventStub;
import bgu.spl.mics.application.publishers.PublisherStub;
import bgu.spl.mics.application.subscribers.SubscriberStub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MessageBrokerTest {
    //Fields:
    MessageBroker msgBroker;
    @BeforeEach
    public void setUp(){
        msgBroker = MessageBrokerImpl.getInstance();
    }

    @Test
    public void test(){
        assertNotNull(msgBroker,"MessageBroker is Null");
        assertSame(msgBroker,MessageBrokerImpl.getInstance(),"MessageBroker are not the same Object");
    }

    @Test
    public void SendAndReceiveTest(){
        Subscriber sub = new SubscriberStub("Shaked Mihnas Pasim");
        Publisher pub = new PublisherStub("Tomer Holtsat Pasim");
        EventStub event = new EventStub();
        msgBroker.register(sub);
        msgBroker.subscribeEvent(event.getClass(),sub);
        msgBroker.sendEvent(event);
        Message message = null;
        try{
            message = msgBroker.awaitMessage(sub);
        }
        catch(Exception e)
        {}

        assertNotNull(message,"Message is Null");
        msgBroker.complete(event,"Done");
        assertEquals(event.getFuture().get(), "Done","Result of Future is not the same");
        msgBroker.unregister(sub);
        msgBroker.sendEvent(event);
        message=null;
        try{
            message=msgBroker.awaitMessage(sub);
        }
        catch(Exception e){}
        assertNull(message,"Message is not Null, unregister is failed");






    }
}
