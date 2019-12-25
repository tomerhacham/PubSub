package bgu.spl.mics.application;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class SyncInitialize
{
    //Fields:
    private static SyncInitialize syncInitialize=new SyncInitialize();
    private AtomicInteger numberOfInitialize = new AtomicInteger(0);
    private AtomicInteger numberOfThreads = new AtomicInteger(0);

    private SyncInitialize(){}

    public static SyncInitialize getInstance(){
        if(syncInitialize==null){
            syncInitialize= new SyncInitialize();
        }
        return syncInitialize;
    }

    public synchronized void setnumberOfThreads(int number_of_threads){
        int old_val = numberOfThreads.get();
        int new_val = old_val+1;

        while(!numberOfThreads.compareAndSet(old_val,new_val)){
            old_val = numberOfThreads.get();
            new_val = old_val+1;
        }
    }

    public void addInit(){
        numberOfInitialize.incrementAndGet();
        }
    public AtomicInteger getNumberOfInitialize() {return this.numberOfInitialize;}
}
