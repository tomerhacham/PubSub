package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.GadgetAvailableEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.Inventory;

import java.util.concurrent.CountDownLatch;

import static java.lang.Thread.sleep;

/**
 * Q is the only Subscriber\Publisher that has access to the {@link bgu.spl.mics.application.passiveObjects.Inventory}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Q extends Subscriber {
	//Fields:
	private Inventory inventory;
	private CountDownLatch countdown;
	private int tick = 0;

	public Q(CountDownLatch countdown) {
		super("Q");
		this.inventory = Inventory.getInstance();
		this.countdown=countdown;
	}

	@Override
	protected void initialize() {
		//region Broadcast handler
		subscribeBroadcast(TickBroadcast.class, br -> {
			if (br.isTermminate()) {
				terminate();
			}
			if (br.getTickNum() >= 0) {
				tick = br.getTickNum();
				System.out.println("Qtick = "+tick);
				System.out.println(Thread.currentThread().getName()+" received broadcast at time "+tick);
			}
		});
		//endregion

		//region GadgetAvailableEvent handler
		subscribeEvent(GadgetAvailableEvent.class, event ->{
			/*try {
				sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}*/
			event.setReceiver(Thread.currentThread().getName());
						System.out.println(event);
			System.out.println("***callback:"+Thread.currentThread().getName()+ " GadgetAvailableEvent: " + event.hashCode());
			String requested_gadget=event.getRequested_gadget();
			if(inventory.getItem(requested_gadget)){
				complete(event,tick);
				System.out.println("----------------Q tick at complete "+ tick);
				System.out.println("Q supplied "+requested_gadget);
				}
			else{
				complete(event,null);
				System.out.println("----------------Q tick at complete "+ tick);
						System.out.println("Q did not supplied "+requested_gadget);
				}
			System.out.println("GadgetAvailableEvent COMPLETE");
		});
		//endregion

		System.out.println("Q is UP");
		countdown.countDown();
	}
}
