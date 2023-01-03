package com.github.schuettec.cobra2d.network.server;

import static com.github.schuettec.cobra2d.network.common.Cobra2DNetwork.registerCommandClasses;

import java.io.IOException;

import com.esotericsoftware.kryonet.Server;
import com.github.schuettec.cobra2d.engine.Cobra2DEngine;
import com.github.schuettec.cobra2d.network.common.Cobra2DNetwork;
import com.github.schuettec.cobra2d.network.data.EntityStateManager;

/**
 * Cobra2D networking game server.
 */
public class Cobra2DServer {

	private int tcpPort;
	private int udpPort;
	private Server server;

	private Cobra2DEngine engine;

	private EntityStateManager stateManager;

	public Cobra2DServer() {

	}

	public Cobra2DServer(Cobra2DEngine engine) {
		this(engine, Cobra2DNetwork.DEFAULT_TCP_PORT, Cobra2DNetwork.DEFAULT_UDP_PORT);
	}

	public Cobra2DServer(Cobra2DEngine engine, int tcpPort, int udpPort) {
		this.tcpPort = tcpPort;
		this.udpPort = udpPort;
		this.engine = engine;
		this.stateManager = new EntityStateManager(engine.getWorld());
	}

	public void start() {
		this.server = new Server();
		server.start();
		registerCommandClasses(server.getKryo());
		try {
			server.bind(54555, 54777);
		} catch (IOException e) {
			throw new RuntimeException("Could not start server.", e);
		}

	}

}
