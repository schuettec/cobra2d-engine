package com.github.schuettec.cobra2d.network.common;

import com.esotericsoftware.kryo.Kryo;
import com.github.schuettec.cobra2d.network.common.command.UpdateCommand;
import com.github.schuettec.cobra2d.network.data.EntityState;

public class Cobra2DNetwork {
	public static final int DEFAULT_UDP_PORT = 54777;
	public static final int DEFAULT_TCP_PORT = 54555;

	/**
	 * Registers the classes used by the engine to send update and controll commands.
	 * 
	 * @param kryo The kryo instance of the server/client.
	 */
	public static void registerCommandClasses(Kryo kryo) {
		kryo.register(UpdateCommand.class);
		kryo.register(EntityState.class);
	}
}
