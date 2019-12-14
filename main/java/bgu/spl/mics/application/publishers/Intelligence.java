package bgu.spl.mics.application.publishers;

import bgu.spl.mics.Publisher;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.passiveObjects.MissionInfo;

import java.util.LinkedList;
import java.util.List;

/**
 * A Publisher only.
 * Holds a list of Info objects and sends them
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Intelligence extends Publisher {
	List<MissionInfo> IntelligenceSource;
	List<MissionInfo> MissionsToExecute;


	public Intelligence(List<MissionInfo> IntelligenceSource, List<MissionInfo> MissionsToExecute) {
		super("Change_This_Name");
		this.IntelligenceSource=IntelligenceSource;
		this.MissionsToExecute = MissionsToExecute;
		// TODO Implement this
	}

	@Override
	protected void initialize() {
		// TODO Implement this
	}

	@Override
	public void run() {
		// TODO Implement this
	}

}
