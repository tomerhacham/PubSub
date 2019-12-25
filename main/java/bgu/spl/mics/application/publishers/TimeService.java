package bgu.spl.mics.application.publishers;

import bgu.spl.mics.Publisher;
import bgu.spl.mics.SimplePublisher;
import bgu.spl.mics.application.messages.TickBroadcast;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * TimeService is the global system timer There is only one instance of this Publisher.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other subscribers about the current time tick using {@link Tick Broadcast}.
 * This class may not hold references for objects which it is not responsible for.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends Publisher {
	//Fields:
	private int duration;
	private int tick;
	private Timer timer;

	public TimeService(int duration) {
		super("TimeService");
		this.duration = duration;
		this.tick = 1;
		timer = new Timer();
	}

	@Override
	protected void initialize() {
	}

	@Override
	public void run() {
		while (tick <= duration) {
			System.out.println("Sender " + getName() + " publish a broadcast");
			SimplePublisher simplePublisher = this.getSimplePublisher();
			TimerTask ticker = new TimerTask() {
				public void run() {
					TickBroadcast broadcast = new TickBroadcast(tick);
					if (tick == duration) {
						broadcast.setTermminate(true);
					}

					simplePublisher.sendBroadcast(broadcast);
				}
			};
			timer.scheduleAtFixedRate(ticker, 0, 1000);
			try {
				Thread.currentThread().sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
