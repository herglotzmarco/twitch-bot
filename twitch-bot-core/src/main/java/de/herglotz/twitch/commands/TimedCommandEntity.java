package de.herglotz.twitch.commands;

import java.util.Objects;

public class TimedCommandEntity {

	private String command;
	private int timeInSeconds;

	public TimedCommandEntity() {
	}

	public TimedCommandEntity(String command, int timeInSeconds) {
		this.command = command;
		this.timeInSeconds = timeInSeconds;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public int getTimeInSeconds() {
		return timeInSeconds;
	}

	public void setTimeInSeconds(int timeInSeconds) {
		this.timeInSeconds = timeInSeconds;
	}

	@Override
	public int hashCode() {
		return Objects.hash(command, timeInSeconds);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		TimedCommandEntity other = (TimedCommandEntity) obj;
		return Objects.equals(command, other.command) && timeInSeconds == other.timeInSeconds;
	}

}
