package de.herglotz.twitch.events;

public class ManageCommandsEvent implements TwitchEvent {

	private ManageCommandOperation operation;
	private String command;
	private String message;
	private int timeInSeconds;

	public static ManageCommandsEvent update(String command, String message) {
		return new ManageCommandsEvent(ManageCommandOperation.UPDATE, command, message, 0);
	}

	public static ManageCommandsEvent remove(String command) {
		return new ManageCommandsEvent(ManageCommandOperation.REMOVE, command, null, 0);
	}

	public static ManageCommandsEvent updateTimed(String command, int timeInSeconds) {
		return new ManageCommandsEvent(ManageCommandOperation.UPDATE_TIMED, command, null, timeInSeconds);
	}

	public static ManageCommandsEvent removeTimed(String command) {
		return new ManageCommandsEvent(ManageCommandOperation.REMOVE_TIMED, command, null, -1);
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
		UPDATE, REMOVE, UPDATE_TIMED, REMOVE_TIMED;
	}

}
