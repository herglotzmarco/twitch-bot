package de.herglotz.twitch.commands.custom;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class CustomCommandEntity {

	@Id
	private String command;

	@Column
	private String message;

	public CustomCommandEntity() {
	}

	public CustomCommandEntity(String command, String message) {
		this.command = command;
		this.message = message;
	}

	public String getCommand() {
		return command;
	}

	public String getMessage() {
		return message;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public int hashCode() {
		return Objects.hash(command, message);
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
		CustomCommandEntity other = (CustomCommandEntity) obj;
		return Objects.equals(command, other.command) && Objects.equals(message, other.message);
	}

}
