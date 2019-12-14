package bgu.spl.mics;

import java.util.*;


/**
 * The {@link MessageBrokerImpl class is the implementation of the MessageBroker interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBrokerImpl implements MessageBroker {
	//Fields:
	private static MessageBrokerImpl messageBroker=null;
	private Map<String, ArrayList> eventsPool = new HashMap<>();
	public LinkedList<RunnableSubPub> allSubPubs = new LinkedList<>();

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
			ArrayList tmp = new ArrayList<Subscriber>();
			tmp.add(m);
			eventsPool.put(type.getClass().getName(),tmp);
		}

	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, Subscriber m) {
		// TODO Auto-generated method stub

	}

	@Override
	public <T> void complete(Event<T> e, T result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendBroadcast(Broadcast b) {
		// TODO Auto-generated method stub

	}

	
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		ArrayList<Subscriber> pool =  eventsPool.get(e.getClass().getName());
		if (pool==null){
			pool = new ArrayList<Subscriber>();
			eventsPool.put(e.getClass().getName(),pool);
		}
		else{
			for (Subscriber sub:pool) {
				sub.addEvent(e);
			}
		}
		return null;
	}

	@Override
	public void register(Subscriber m) {
		allSubPubs.addLast(m);
		m.makeQueue();

	}

	@Override
	public void unregister(Subscriber m) {
		// TODO Auto-generated method stub

	}

	@Override
	public Message awaitMessage(Subscriber m) throws InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}

	

}
