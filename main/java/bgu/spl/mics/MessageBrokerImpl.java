package bgu.spl.mics;

import jdk.nashorn.internal.ir.Block;

import java.util.*;
import java.util.concurrent.*;


/**
 * The {@link MessageBrokerImpl class is the implementation of the MessageBroker interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBrokerImpl implements MessageBroker {
	//Fields:
	private static MessageBrokerImpl messageBroker=null;
	private ConcurrentMap<String, ConcurrentLinkedQueue> eventsPool = new ConcurrentHashMap<>();
	private ConcurrentMap<Event,Future> futures = new ConcurrentHashMap<>();
	private ConcurrentMap<Subscriber, BlockingQueue<Message>> queues = new ConcurrentHashMap<>();
	private LinkedList<Subscriber> broadcastPool = new LinkedList<>();

	//Constructor:
	private MessageBrokerImpl(){};

	/**
	 * Retrieves the single instance of this class.
	 */
	public static MessageBrokerImpl getInstance() {
		if(messageBroker==null){
			messageBroker=new MessageBrokerImpl();
			}
		return messageBroker;
	}

	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, Subscriber m) {
		if(eventsPool.get(type.getClass().getName())!= null){
			eventsPool.get(type.getClass().getName()).add(m);
		}
		else{
			ConcurrentLinkedQueue tmp = new ConcurrentLinkedQueue<Subscriber>();
			tmp.add(m);
			eventsPool.put(type.getClass().getName(),tmp);
		}

	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, Subscriber m) {
		broadcastPool.addLast(m);

	}

	@Override
	public <T> void complete(Event<T> e, T result) {
		futures.get(e).resolve(result);

	}

	@Override
	public void sendBroadcast(Broadcast b) {
		for (Subscriber sub: broadcastPool){
			sub.addMessage(b);
		}

	}

	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		ConcurrentLinkedQueue<Subscriber> pool =  eventsPool.get(e.getClass().getName());
		if (pool==null){
			pool = new ConcurrentLinkedQueue<Subscriber>();
			eventsPool.put(e.getClass().getName(),pool);
		}
		Future<T> future = new Future<>();
		futures.put(e,future);
		Subscriber sub = pool.poll();
		sub.addMessage(e);
		pool.add(sub);
		return future;
		}


	@Override
	public void register(Subscriber m) {
		BlockingQueue<Message> queue = new LinkedBlockingQueue<>();
		queues.put(m,queue);
	}

	@Override
	public void unregister(Subscriber m) {
		queues.remove(m,queues.get(m));

	}

	@Override
	public Message awaitMessage(Subscriber m) throws InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}

	

}
