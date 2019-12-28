package bgu.spl.mics;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.subscribers.Moneypenny;

import java.util.*;
import java.util.concurrent.*;


/**
 * The {@link MessageBrokerImpl class is the implementation of the MessageBroker interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBrokerImpl implements MessageBroker {

	private static class SingeltonHolder{
		private static MessageBrokerImpl instance = new MessageBrokerImpl();
	}
	//Fields:
	private ConcurrentMap<Class<? extends Message>, ConcurrentLinkedQueue<Subscriber>> eventsPool = new ConcurrentHashMap<>();
	private ConcurrentMap<Event,Future> futures = new ConcurrentHashMap<>();
	private ConcurrentMap<Subscriber, LinkedBlockingQueue<Message>> queues = new ConcurrentHashMap<>();


	//Constructor:
	private MessageBrokerImpl(){
		List<ConcurrentLinkedQueue> Pool = MessageBrokerImpl.makePools();
		List<Message> Messages = MessageBrokerImpl.makeMessages();
		for(int i=0;i<Pool.size();i++){
			eventsPool.put(Messages.get(i).getClass(), Pool.get(i));
		}
	};

	/**
	 * Retrieves the single instance of this class.
	 */
	public static MessageBrokerImpl getInstance() {
		return SingeltonHolder.instance;
	}

	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, Subscriber m) {
		if(eventsPool.get(type)!= null){
			eventsPool.get(type).add(m);
		}
		/*else{
			ConcurrentLinkedQueue<Subscriber> subscriberspool = new ConcurrentLinkedQueue<>();
			subscriberspool.add(m);
			eventsPool.put(type,subscriberspool);
		}*/
	//	System.out.println(m.getName()+" subscribe to event: "+type.getSimpleName());

	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, Subscriber m) {
		if(eventsPool.get(type)!= null){
			eventsPool.get(type).add(m);
	//		System.out.println(m.getName()+"---Added to existing pool");
		}
		/*else{
			ConcurrentLinkedQueue<Subscriber> subscriberspool = new ConcurrentLinkedQueue<>();
			subscriberspool.add(m);
			eventsPool.put(type,subscriberspool);
			System.out.println(m.getName()+ "--- make NEW pool for the broadcast");

		}*/
	//	System.out.println(m.getName()+" subscribe to broadcast: "+type.getSimpleName());

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
		//System.out.println("--for Broadcast:");
		//printQueue(subscriberQueue);
		if(subscriberQueue!=null) {
			for(int i=0; i< subscriberQueue.size(); i++){
				Subscriber sub = subscriberQueue.poll();
				 LinkedBlockingQueue queue = queues.get(sub);
				 if(queue==null){
					 register(sub);
				 }
				queue = queues.get(sub);
				try {
					queue.put(b);
					subscriberQueue.add(sub);
					/*System.out.println("------- Queue of "+sub.getName());;
					PrintSubscriberQueue(queue);*/
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		Future<T> future = new Future<>();;
		ConcurrentLinkedQueue<Subscriber> pool =  eventsPool.get(e.getClass());
		if (pool!=null) {
			if (!pool.isEmpty()) {
				Subscriber sub = pool.poll();
				if(sub!=null){
				pool.add(sub);
				futures.put(e, future);
				try {
					LinkedBlockingQueue queue = queues.get(sub);
					if (queue == null) {
						register(sub);
					}
					queue = queues.get(sub);
					queue.put(e);
					/*System.out.println("------- Queue of "+sub.getName());
					PrintSubscriberQueue(queue);*/
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
				}
			}
			else {
				future = new Future<>();
				futures.put(e, future);
				if (e.getClass().getSimpleName().equals("AgentsAvailableEvent")) {
					complete(e,null);
				}
				else if (e.getClass().getSimpleName().equals("GadgetAvailableEvent")) {
					complete(e,null);
				}
			}
		}
		return future;
	}

	@Override
	public void register(Subscriber m) {
		LinkedBlockingQueue<Message> queue = new LinkedBlockingQueue<>();
		queues.put(m,queue);
	//	System.out.println(m.getName()+" register");
	}

	@Override
	public void unregister(Subscriber m) {
		if(queues.keySet().contains(m)){
			queues.remove(m,queues.get(m));
			for (Class<? extends Message> type: eventsPool.keySet()){
					ConcurrentLinkedQueue  pool = eventsPool.get(type);
					while(pool.contains(m)){pool.remove(m);}
				}
			if(m.getName().contains("moneypenny")){
				List<Event> events= ((Moneypenny)m).getEvent();
				for(Event event:events){
					futures.get(event).resolve(null);
				}
			}
		}
	//	System.out.println(m.getName()+" unregister");


	}

	@Override
	public Message awaitMessage(Subscriber m) throws InterruptedException {
		LinkedBlockingQueue<Message> queue = queues.get(m);
		return queue.take();
	}

	private void printQueue(ConcurrentLinkedQueue<Subscriber> queue){
		for (Subscriber sub:queue) {
	//		System.out.println(sub.getName());
		}
	}

	private static List<ConcurrentLinkedQueue> makePools(){
		List<ConcurrentLinkedQueue> pools = new LinkedList<>();

		ConcurrentLinkedQueue<Subscriber> TickBroadcastPool = new ConcurrentLinkedQueue<>();
		ConcurrentLinkedQueue<Subscriber> AgentAvailablePool = new ConcurrentLinkedQueue<>();
		ConcurrentLinkedQueue<Subscriber> GadgetAvailablePool = new ConcurrentLinkedQueue<>();
		ConcurrentLinkedQueue<Subscriber> MissionReceivedEventPool = new ConcurrentLinkedQueue<>();
		ConcurrentLinkedQueue<Subscriber> RecallAgentsEventPool = new ConcurrentLinkedQueue<>();
		ConcurrentLinkedQueue<Subscriber> SendAgentsEventPool = new ConcurrentLinkedQueue<>();

		pools.add(TickBroadcastPool);
		pools.add(AgentAvailablePool);
		pools.add(GadgetAvailablePool);
		pools.add(MissionReceivedEventPool);
		pools.add(RecallAgentsEventPool);
		pools.add(SendAgentsEventPool);

		return pools;
	}

	private static List<Message> makeMessages(){
		List<Message> messages = new LinkedList<>();

		AgentsAvailableEvent agentsAvailableEvent = new AgentsAvailableEvent();
		GadgetAvailableEvent gadgetAvailableEvent = new GadgetAvailableEvent();
		MissionReceivedEvent missionReceivedEvent = new MissionReceivedEvent();
		RecallAgentsEvent recallAgentsEvent = new RecallAgentsEvent();
		SendAgentsEvent sendAgentsEvent = new SendAgentsEvent();
		TickBroadcast tickBroadcast = new TickBroadcast();

		messages.add(agentsAvailableEvent);
		messages.add(gadgetAvailableEvent);
		messages.add(missionReceivedEvent);
		messages.add(recallAgentsEvent);
		messages.add(sendAgentsEvent);
		messages.add(tickBroadcast);

		return messages;
	}

	private void PrintSubscriberQueue(LinkedBlockingQueue<Message> queue){
		for (Message message:queue) {
	//		System.out.print(message.getClass().getSimpleName()+ ", ");
		}
	}

}
