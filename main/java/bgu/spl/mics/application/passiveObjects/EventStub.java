package bgu.spl.mics.application.passiveObjects;
import bgu.spl.mics.Event;
import bgu.spl.mics.Future;


public class EventStub implements Event<String>{

    private Future<String> future = new Future<>();

    public String get(){
        return future.get();
    }

    public boolean isDone(){
        return future.isDone();
    }

    public void resolve(String result){
        this.future.resolve(result);
    }

    public Future getFuture(){
        return this.future;
    }

}
