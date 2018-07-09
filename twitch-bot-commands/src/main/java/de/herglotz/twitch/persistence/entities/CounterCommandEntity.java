package de.herglotz.twitch.persistence.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.google.common.base.Preconditions;

@Entity
public class CounterCommandEntity {

	@Id
	private String command;

	@Column
	private int counter;

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		Preconditions.checkNotNull(command);
		this.command = command;
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((command == null) ? 0 : command.hashCode());
		result = prime * result + counter;
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
		CounterCommandEntity other = (CounterCommandEntity) obj;
		if (command == null) {
			if (other.command != null)
				return false;
		} else if (!command.equals(other.command))
			return false;
		if (counter != other.counter)
			return false;
		return true;
	}

	public void increaseCounter() {
		counter++;
	}

	public void decreaseCounter() {
		counter--;
	}

}
