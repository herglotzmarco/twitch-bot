package de.herglotz.twitch.commands.custom;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.google.common.base.Preconditions;

@Entity
public class CustomCommandEntity {

	@Id
	private String command;

	@Column
	private String message;

	public String getCommand() {
		return command;
	}

	public String getMessage() {
		return message;
	}

	public void setCommand(String command) {
		Preconditions.checkNotNull(command);
		this.command = command;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((command == null) ? 0 : command.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CustomCommandEntity other = (CustomCommandEntity) obj;
		if (command == null) {
			if (other.command != null)
				return false;
		} else if (!command.equals(other.command))
			return false;
		return true;
	}

}
