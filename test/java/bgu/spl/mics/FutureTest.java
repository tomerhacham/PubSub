package bgu.spl.mics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class FutureTest {
    @BeforeEach
    public void setUp(){

    }

    @Test
    public void test(){
        Future<String> future = new Future();
        future.resolve("Done");
        assertNotNull(future.get(),"Future not Resolved");
    }

    @Test
    public void isDoneTest(){
        Future<String> future = new Future();
        assertFalse(future.isDone(),"Future is done when it shouldn't");
        future.resolve("Done");
        assertTrue(future.isDone(),"Future is not Done when it should");


    }

    @Test
    public void getWithTimeOut() throws InterruptedException {
        Future<String> future1 = new Future<>();
        String result = future1.get(1, TimeUnit.SECONDS);
        Thread.sleep(1000);
        assertNull(result,"the result is not null after 1000ms");
        future1.resolve("Done");
        result=future1.get(1, TimeUnit.SECONDS);
        assertNotNull(result,"result came back as null");
    }
}
