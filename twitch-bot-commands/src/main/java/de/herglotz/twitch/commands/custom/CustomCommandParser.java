package de.herglotz.twitch.commands.custom;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.herglotz.twitch.api.irc.messages.CommandMessage;

public class CustomCommandParser {

	private static final Logger LOG = LoggerFactory.getLogger(CustomCommandParser.class);
	private static final String[] PLACEHOLDERS = new String[] { "user" };

	private Map<String, Method> replacements;

	public CustomCommandParser() {
		replacements = new HashMap<>();
		for (String placeholder : PLACEHOLDERS) {
			try {
				replacements.put("<" + placeholder + ">", CommandMessage.class.getMethod(placeholder));
			} catch (NoSuchMethodException | SecurityException e) {
				LOG.warn("Method {} was not found. Replacement will not work", placeholder, e);
				continue;
			}
		}
	}

	public String parse(String message, CommandMessage parameters) {
		for (Entry<String, Method> mapping : replacements.entrySet()) {
			try {
				message = message.replace(mapping.getKey(), mapping.getValue().invoke(parameters).toString());
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				LOG.warn("Method {} could not be called. Replacement will not work", mapping.getKey(), e);
				continue;
			}
		}
		return message;
	}

}
