package com.github.schuettec.cobra2d.network.server;

import static com.github.schuettec.cobra2d.network.common.Cobra2DNetwork.registerCommandClasses;
import static java.util.Objects.isNull;

import java.io.IOException;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.github.schuettec.cobra2d.controller.Controller;
import com.github.schuettec.cobra2d.engine.Cobra2DEngine;
import com.github.schuettec.cobra2d.engine.NoInputController;
import com.github.schuettec.cobra2d.entity.Collision;
import com.github.schuettec.cobra2d.entity.skills.Camera;
import com.github.schuettec.cobra2d.entity.skills.Entity;
import com.github.schuettec.cobra2d.math.Dimension;
import com.github.schuettec.cobra2d.network.common.command.client.RemoveFromClientCommand;
import com.github.schuettec.cobra2d.network.common.command.client.UpdateClientCommand;
import com.github.schuettec.cobra2d.network.data.EntityState;
import com.github.schuettec.cobra2d.network.data.EntityStateManager;
import com.github.schuettec.cobra2d.renderer.Renderer;
import com.github.schuettec.cobra2d.renderer.RendererException;
import com.github.schuettec.cobra2d.world.ActiveWorldUpdater;
import com.github.schuettec.cobra2d.world.Cobra2DWorld;
import com.github.schuettec.cobra2d.world.WorldAccess;
import com.github.schuettec.cobra2d.world.WorldListener;

/**
 * Cobra2D networking game server.
 */
public class Cobra2DServer implements Renderer, WorldListener {

	private int tcpPort;
	private int udpPort;
	private Server server;

	private Cobra2DEngine engine;

	private ActiveWorldUpdater updater;

	private EntityStateManager stateManager;
	private Supplier<Entity> spawnEntitySupplier;
	private Dimension cameraDimension;

	private WorldAccess worldAccess;

	private Map<Connection, Player> playersByConnection;

	public Cobra2DServer() {

	}

	@Override
	public void initializeRenderer(Cobra2DEngine engine) throws RendererException {
		this.engine = engine;
		this.tcpPort = this.engine.getTcpPort();
		this.udpPort = this.engine.getUdpPort();
		this.engine = engine;
		Cobra2DWorld world = engine.getWorld();
		world.addWorldListener(this);
		this.worldAccess = world.getWorldAccess();
		this.stateManager = new EntityStateManager(world);
		this.updater = new ActiveWorldUpdater(engine.getRefreshRate(), true, world);
		this.playersByConnection = new Hashtable<>();
	}

	@Override
	public void afterUpdate() {
		playersByConnection.values()
		    .parallelStream()
		    .forEach(player -> {
			    Connection connection = player.getConnection();
			    Camera playerCamera = player.getPlayerCamera();
			    List<String> thisFrameIds = new LinkedList<>();
			    List<Collision> cameraCollision = worldAccess.getCameraCollision(playerCamera);
			    cameraCollision.stream()
			        .map(Collision::getOpponent)
			        .forEach(entity -> {
				        if (stateManager.isStateManaged(entity)) {
					        thisFrameIds.add(entity.getId());
					        EntityState entityState = stateManager.readEntityState(entity);
					        String entityClass = entity.getClass()
					            .getName();
					        UpdateClientCommand updateCmd = new UpdateClientCommand(entityClass, entityState);
					        connection.sendUDP(updateCmd);
				        }
			        });
			    List<String> lastFrame = player.getLastFrameIds();
			    lastFrame.removeAll(thisFrameIds);
			    lastFrame.stream()
			        .forEach(toRemoveEntityId -> {
				        RemoveFromClientCommand removeCmd = new RemoveFromClientCommand(toRemoveEntityId);
				        connection.sendUDP(removeCmd);
			        });
		    });
	}

	private void createPlayerAndSpawn(Connection connection) {
		Entity playerEntity = spawnEntitySupplier.get();
		ServerCamera playerCamera = new ServerCamera(playerEntity, cameraDimension);
		Player player = new Player("unknown player", connection, playerCamera, playerEntity);
		this.playersByConnection.put(connection, player);
		worldAccess.spawnEntity(playerCamera);
		worldAccess.spawnEntity(playerEntity);
	}

	private void removePlayer(Connection connection) {
		getPlayer(connection).ifPresent(player -> {
			worldAccess.removeEntity(player.getEntity());
			worldAccess.removeEntity(player.getPlayerCamera());
		});
	}

	private void processPlayerCommand(Connection connection, Object object) {

	}

	private Optional<Player> getPlayer(Connection connection) {
		return Optional.ofNullable(this.playersByConnection.get(connection));
	}

	@Override
	public void start() {
		if (isNull(spawnEntitySupplier)) {
			throw new RuntimeException("No spawn factory set!");
		}

		this.server = new Server();
		server.start();
		registerCommandClasses(server.getKryo());
		try {
			server.bind(tcpPort, udpPort);
		} catch (IOException e) {
			throw new RuntimeException("Could not start server.", e);
		}

		// For later: Define the start of the world!
		updater.start();

		server.addListener(new Listener() {
			@Override
			public void connected(Connection connection) {
				super.connected(connection);
				createPlayerAndSpawn(connection);
			}

			@Override
			public void disconnected(Connection connection) {
				super.disconnected(connection);
				removePlayer(connection);
				playersByConnection.remove(connection);
			}

			@Override
			public void received(Connection connection, Object object) {
				super.received(connection, object);
				processPlayerCommand(connection, object);
			}

			@Override
			public void idle(Connection connection) {
				super.idle(connection);
			}

		});
	}

	@Override
	public void finish() {
		updater.stop();
		this.server.close();
		try {
			this.server.dispose();
		} catch (Exception e) {
		}
	}

	@Override
	public Controller getController() {
		return new NoInputController();
	}

	public void setSpawnEntityFactory(Supplier<Entity> spawnEntitySupplier) {
		this.spawnEntitySupplier = spawnEntitySupplier;
	}

	public void setNetworkCameraDimension(Dimension cameraDimension) {
		this.cameraDimension = cameraDimension;
	}

}
