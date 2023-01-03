package com.github.schuettec.cobra2d.network.common;

import com.esotericsoftware.kryo.Kryo;
import com.github.schuettec.cobra2d.network.common.command.client.UpdateClientCommand;
import com.github.schuettec.cobra2d.network.data.EntityState;

public class Cobra2DNetwork {
	/**
	 * Registers the classes used by the engine to send update and controll commands.
	 * 
	 * @param kryo The kryo instance of the server/client.
	 */
	public static void registerCommandClasses(Kryo kryo) {
		kryo.register(UpdateClientCommand.class);
		kryo.register(EntityState.class);
	}
}
