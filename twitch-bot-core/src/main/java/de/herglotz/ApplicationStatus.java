package de.herglotz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum ApplicationStatus {

	STOPPED, STARTING, STARTED, STOPPING, UNSTABLE;

	private static final Logger LOG = LoggerFactory.getLogger(ApplicationStatus.class);

	public static ApplicationStatus mergeStatus(ApplicationStatus first, ApplicationStatus second) {
		if (first == second) {
			return first;
		}
		if ((first == STARTING && second == STARTED)// second is already done
				|| (second == STARTING && first == STARTED)// first is already done
				|| (first == STARTING && second == STOPPED)// second has not yet received the event
				|| (second == STARTING && first == STOPPED)) {// first has not yet received the event
			return STARTING;
		}
		if ((first == STOPPING && second == STOPPED)// second is already done
				|| (second == STOPPING && first == STOPPED)// first is already done
				|| (first == STOPPING && second == STARTED)// second has not yet received the event
				|| (second == STOPPING && first == STARTED)) { // first has not yet received the event
			return STOPPING;
		}
		LOG.warn("Invalid combination: {} and {}", first, second);
		return UNSTABLE;
	}

}
