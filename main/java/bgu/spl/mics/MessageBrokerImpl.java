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
	private ConcurrentMap<String, Queue<Subscriber>> eventsPool = new ConcurrentHashMap<>();
	private ConcurrentMap<Event,Future> futures = new ConcurrentHashMap<>();
	private ConcurrentMap<Subscriber, LinkedBlockingQueue<Message>> queues = new ConcurrentHashMap<>();
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
			Queue<Subscriber> subscriberspool = new LinkedList<>();
			subscriberspool.add(m);
			eventsPool.put(type.getClass().getName(),subscriberspool);
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
		for (Subscriber sub : broadcastPool) {
			try {
				queues.get(sub).put(b);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		Queue<Subscriber> pool =  eventsPool.get(e.getClass().getName());
		if (pool==null){
			pool = new LinkedList<Subscriber>();
			eventsPool.put(e.getClass().getName(),pool);
		}
		Future<T> future = new Future<>();
		futures.put(e,future);
		Subscriber sub = pool.poll();
		try {
			queues.get(sub).put(e);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
		pool.add(sub);
		return future;
		}


	@Override
	public void register(Subscriber m) {
		LinkedBlockingQueue<Message> queue = new LinkedBlockingQueue<>();
		queues.put(m,queue);
	}

	@Override
	public void unregister(Subscriber m) {
		queues.remove(m,queues.get(m));

	}

	@Override
	public Message awaitMessage(Subscriber m) throws InterruptedException {
		BlockingQueue<Message> queue = queues.get(m);
		Message message = queue.take();
		return message;
	}

	

}
