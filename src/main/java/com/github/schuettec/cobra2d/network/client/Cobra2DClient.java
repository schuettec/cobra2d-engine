package com.github.schuettec.cobra2d.network.client;

import static com.github.schuettec.cobra2d.network.common.Cobra2DNetwork.registerCommandClasses;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import java.util.function.Supplier;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.github.schuettec.cobra2d.engine.Cobra2DConstants;
import com.github.schuettec.cobra2d.engine.Cobra2DEngine;
import com.github.schuettec.cobra2d.entity.skills.Entity;
import com.github.schuettec.cobra2d.network.common.command.client.ClientCommand;
import com.github.schuettec.cobra2d.network.data.EntityState;
import com.github.schuettec.cobra2d.network.data.EntityStateManager;
import com.github.schuettec.cobra2d.world.Cobra2DWorld;
import com.github.schuettec.cobra2d.world.WorldAccess;

public class Cobra2DClient implements ClientAccess {

	private Client client;

	private Cobra2DEngine engine;

	private EntityStateManager stateManager;

	private WorldAccess worldAccess;

	private Map<Class<? extends Entity>, Supplier<Entity>> entityCreator;

	private Supplier<Entity> supplier;

	public Cobra2DClient(Cobra2DEngine engine) {
		this.engine = engine;
		this.entityCreator = new Hashtable<>();
		Cobra2DWorld world = this.engine.getWorld();
		this.stateManager = new EntityStateManager(world);
		this.worldAccess = engine.getWorld()
		    .getWorldAccess();
	}

	public void addEntityCreator(Class<? extends Entity> entityType, Supplier<Entity> creator) {
		entityCreator.put(entityType, creator);
	}

	public void connect(String ip) {
		connect(ip, Cobra2DConstants.DEFAULT_TCP_PORT, Cobra2DConstants.DEFAULT_UDP_PORT);
	}

	public void connect(String ip, int tcpPort, int udpPort) {
		this.client = new Client();
		client.start();

		registerCommandClasses(client.getKryo());

		try {
			client.connect(5000, ip, 54555, 54777);
		} catch (IOException e) {
			throw new RuntimeException(
			    "Cannot connect to client: " + ip + " with tcp port " + tcpPort + " and udp port " + udpPort, e);
		}

		client.addListener(new Listener() {
			@Override
			public void received(Connection connection, Object object) {
				if (object instanceof ClientCommand) {
					ClientCommand clientCommand = (ClientCommand) object;
					clientCommand.perform(worldAccess, Cobra2DClient.this);
				}
			}
		});
	}

	public void test() {
		// UpdateCommand request = new UpdateCommand(Integer.MAX_VALUE, "Hello World!");
		// client.sendUDP(request);
	}

	public void disconnect() {
		client.close();
		client.stop();
		try {
			client.dispose();
		} catch (IOException e) {
		}
	}

	@Override
	public void writeEntityState(EntityState entityState, Entity entity) {
		stateManager.writeEntityState(entityState, entity);
	}

	@Override
	public Entity createEntity(String entityClass) {
		if (stateManager.isRegistered(entityClass)) {
			Class<? extends Entity> entityCls = stateManager.getEntityClass(entityClass);
			try {
				supplier = entityCreator.get(entityCls);
				return supplier.get();
			} catch (Exception e) {
				throw new RuntimeException("Could not create entity.", e);
			}
		} else {
			throw new RuntimeException("Attempt to create object that was not registered to be remotely transferrable.");
		}
	}

}
