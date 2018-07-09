package de.herglotz.twitch.commands;

import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class CounterKeyListener implements NativeKeyListener {

	private static final int ADD_CODE = 53;
	private static final int SUB_CODE = 3639;

	private Runnable addMethod;
	private Runnable subMethod;

	public CounterKeyListener(Runnable addMethod, Runnable subMethod) {
		this.addMethod = addMethod;
		this.subMethod = subMethod;
	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent event) {
	}

	@Override
	public void nativeKeyPressed(NativeKeyEvent event) {
	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent event) {
		if (event.getKeyCode() == ADD_CODE) {
			addMethod.run();
		} else if (event.getKeyCode() == SUB_CODE) {
			subMethod.run();
		}
	}

}
