package bgu.spl.mics;
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
	private ConcurrentMap<Class<? extends Message>, ConcurrentLinkedQueue<Subscriber>> eventsPool = new ConcurrentHashMap<>();
	private ConcurrentMap<Event,Future> futures = new ConcurrentHashMap<>();
	private ConcurrentMap<Subscriber, LinkedBlockingQueue<Message>> queues = new ConcurrentHashMap<>();
	//private LinkedList<Subscriber> broadcastPool = new LinkedList<>();

	//Constructor:
	private MessageBrokerImpl(){};

	/**
	 * Retrieves the single instance of this class.
	 */
	public synchronized static MessageBrokerImpl getInstance() {
		if(messageBroker==null){
			messageBroker=new MessageBrokerImpl();
			}
		return messageBroker;
	}

	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, Subscriber m) {
		if(eventsPool.get(type)!= null){
			eventsPool.get(type).add(m);
		}
		else{
			ConcurrentLinkedQueue<Subscriber> subscriberspool = new ConcurrentLinkedQueue<>();
			subscriberspool.add(m);
			eventsPool.put(type,subscriberspool);
		}

	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, Subscriber m) {
		if(eventsPool.get(type)!= null){
			eventsPool.get(type).add(m);
		}
		else{
			ConcurrentLinkedQueue<Subscriber> subscriberspool = new ConcurrentLinkedQueue<>();
			subscriberspool.add(m);
			eventsPool.put(type,subscriberspool);
		}
	}

	@Override
	public <T> void complete(Event<T> e, T result) {
		if(e!=null && futures.get(e)!=null){
			futures.get(e).resolve(result);
		}

	}

	@Override
	public void sendBroadcast(Broadcast b) {
		ConcurrentLinkedQueue<Subscriber> subscriberQueue = eventsPool.get(b.getClass());
		if(subscriberQueue!=null) {
			for(int i=0; i< subscriberQueue.size(); i++){
				Subscriber sub = subscriberQueue.poll();
				 BlockingQueue queue = queues.get(sub);
				try {
					queue.put(b);
					subscriberQueue.add(sub);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		Future<T> future = null;
		ConcurrentLinkedQueue<Subscriber> pool =  eventsPool.get(e.getClass());
		if (pool!=null){
			if(!pool.isEmpty()) {
				Subscriber sub = pool.poll();
				pool.add(sub);
				future=new Future<>();
				futures.put(e,future);
				try {
					LinkedBlockingQueue queue = queues.get(sub);
					if(queue==null){
						register(sub);
					}
					queue.put(e);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}
		}
		return future;
	}


	@Override
	public void register(Subscriber m) {
		LinkedBlockingQueue<Message> queue = new LinkedBlockingQueue<>();
		queues.put(m,queue);
	}

	@Override
	public void unregister(Subscriber m) {
		if(queues.keySet().contains(m)){
			queues.remove(m,queues.get(m));
			for (Class<? extends Message> type: eventsPool.keySet()){
					ConcurrentLinkedQueue  pool = eventsPool.get(type);
					while(pool.contains(m)){pool.remove(m);}
				}
			}

	}

	@Override
	public Message awaitMessage(Subscriber m) throws InterruptedException {
		LinkedBlockingQueue<Message> queue = queues.get(m);
		return queue.take();
	}

	

}
