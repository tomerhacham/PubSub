package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.SyncInitialize;
import bgu.spl.mics.application.messages.GadgetAvailableEvent;
import bgu.spl.mics.application.messages.MissionReceivedEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.Inventory;

/**
 * Q is the only Subscriber\Publisher that has access to the {@link bgu.spl.mics.application.passiveObjects.Inventory}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Q extends Subscriber {
	//Fields:
	Inventory inventory;
	int tick = 0;

	public Q() {
		super("Q");
		this.inventory = Inventory.getInstance();
	}

	@Override
	protected void initialize() {
		//SyncInitialize.getInstance().addInit();
		subscribeBroadcast(TickBroadcast.class, br -> {
			if (br.isTermminate()) {
				terminate();
			}
			if (br.getTickNum() >= 0) {
				tick = br.getTickNum();
			}
		});
		subscribeEvent(GadgetAvailableEvent.class, event ->{
			String requested_gadget=event.getRequested_gadget();
			if(inventory.getItem(requested_gadget)){
				complete(event,tick );
				}
			else{complete(event,-1);}
		});
	}
}
