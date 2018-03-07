package de.herglotz.twitch.api.irc;

public class ChatMessage {

	private String username;
	private String targetChannel;
	private String message;

	public ChatMessage(String username, String targetChannel, String message) {
		this.username = username;
		this.targetChannel = targetChannel;
		this.message = message;
	}

	public String getUsername() {
		return this.username;
	}

	public String getTargetChannel() {
		return this.targetChannel;
	}

	public String getMessage() {
		return this.message;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result + ((targetChannel == null) ? 0 : targetChannel.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
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
		ChatMessage other = (ChatMessage) obj;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		if (targetChannel == null) {
			if (other.targetChannel != null)
				return false;
		} else if (!targetChannel.equals(other.targetChannel))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

}
