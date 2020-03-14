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
		if (first == UNSTABLE || second == UNSTABLE) {
			return UNSTABLE;
		}
		if ((first == STARTING && second == STARTED)//
				|| (second == STARTING && first == STARTED)) {
			return STARTING;
		}
		if ((first == STOPPING && second == STOPPED)//
				|| (second == STOPPING && first == STOPPED)) {
			return STOPPING;
		}
		LOG.warn("Invalid combination: {} and {}", first, second);
		return UNSTABLE;
	}

}
