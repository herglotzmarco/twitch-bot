package de.herglotz.twitch.events.manage;

import de.herglotz.twitch.commands.custom.CustomCommandEntity;
import de.herglotz.twitch.events.TwitchEvent;

public class ManageCommandsEvent implements TwitchEvent {

	private ManageCommandOperation operation;
	private String affectedCommand;
	private CustomCommandEntity updatedCommand;
	private int timeInSeconds;

	public static ManageCommandsEvent update(String affectedCommand, CustomCommandEntity updatedCommand) {
		return new ManageCommandsEvent(ManageCommandOperation.UPDATE, affectedCommand, updatedCommand, 0);
	}

	public static ManageCommandsEvent remove(String affectedCommand) {
		return new ManageCommandsEvent(ManageCommandOperation.REMOVE, affectedCommand, null, 0);
	}

	public static ManageCommandsEvent updateTimed(String affectedCommand, int timeInSeconds) {
		return new ManageCommandsEvent(ManageCommandOperation.UPDATE_TIMED, affectedCommand, null, timeInSeconds);
	}

	public static ManageCommandsEvent removeTimed(String affectedCommand) {
		return new ManageCommandsEvent(ManageCommandOperation.REMOVE_TIMED, affectedCommand, null, -1);
	}

	private ManageCommandsEvent(ManageCommandOperation operation, String affectedCommand,
			CustomCommandEntity updatedCommand, int timeInSeconds) {
		this.operation = operation;
		this.affectedCommand = affectedCommand;
		this.updatedCommand = updatedCommand;
		this.timeInSeconds = timeInSeconds;
	}

	public ManageCommandOperation getOperation() {
		return operation;
	}

	public String getAffectedCommand() {
		return affectedCommand;
	}

	public CustomCommandEntity getUpdatedCommand() {
		return updatedCommand;
	}

	public int getTimeInSeconds() {
		return timeInSeconds;
	}

	public enum ManageCommandOperation {
		UPDATE, REMOVE, UPDATE_TIMED, REMOVE_TIMED;
	}

}
