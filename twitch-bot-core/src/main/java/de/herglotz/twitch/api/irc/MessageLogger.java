package de.herglotz.twitch.api.irc;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.herglotz.twitch.events.ChatMessageEvent;
import de.herglotz.twitch.events.MessageEvent;
import de.herglotz.twitch.events.TwitchEvent;
import de.herglotz.twitch.messages.ChatMessage;
import de.herglotz.twitch.messages.Message;

@ApplicationScoped
public class MessageLogger {

	private static final Logger LOG = LoggerFactory.getLogger(MessageLogger.class);

	public void handleEvent(@Observes TwitchEvent event) {
		if (event instanceof ChatMessageEvent) {
			ChatMessage message = ((ChatMessageEvent) event).getMessage();
			LOG.info("{}: {}", message.getUsername(), message.getMessage());
		} else if (event instanceof MessageEvent && isTwitchInternalMessage(((MessageEvent) event).getMessage())) {
			LOG.debug(((MessageEvent) event).getMessage().getMessage());
		}
	}

	private boolean isTwitchInternalMessage(Message message) {
		return message.getMessage().startsWith(":tmi.twitch.tv");
	}

}
