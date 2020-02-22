package de.herglotz.twitch.events;

public class ManageCommandsEvent implements TwitchEvent {

	private ManageCommandOperation operation;
	private String command;
	private String message;
	private int timeInSeconds;

	public static ManageCommandsEvent remove(String command) {
		return new ManageCommandsEvent(ManageCommandOperation.REMOVE, command, null, 0);
	}

	public static ManageCommandsEvent add(String command, String message) {
		return new ManageCommandsEvent(ManageCommandOperation.ADD, command, message, 0);
	}

	public static ManageCommandsEvent timed(String command, int timeInSeconds) {
		return new ManageCommandsEvent(ManageCommandOperation.TIMED, command, null, timeInSeconds);
	}

	private ManageCommandsEvent(ManageCommandOperation operation, String command, String message, int timeInSeconds) {
		this.operation = operation;
		this.command = command;
		this.message = message;
		this.timeInSeconds = timeInSeconds;
	}

	public ManageCommandOperation getOperation() {
		return operation;
	}

	public String getCommand() {
		return command;
	}

	public String getMessage() {
		return message;
	}

	public int getTimeInSeconds() {
		return timeInSeconds;
	}

	public enum ManageCommandOperation {
		ADD, REMOVE, TIMED;
	}

}
