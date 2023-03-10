package com.github.schuettec.cobra2d.network.server;

import static com.github.schuettec.cobra2d.network.common.Cobra2DNetwork.registerCommandClasses;
import static java.util.Objects.isNull;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Supplier;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.github.schuettec.cobra2d.controller.Controller;
import com.github.schuettec.cobra2d.engine.Cobra2DEngine;
import com.github.schuettec.cobra2d.engine.NoInputController;
import com.github.schuettec.cobra2d.entity.camera.BasicRectangleMapCamera;
import com.github.schuettec.cobra2d.entity.skills.Camera;
import com.github.schuettec.cobra2d.entity.skills.Entity;
import com.github.schuettec.cobra2d.math.Dimension;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.network.common.command.client.ClientCommand;
import com.github.schuettec.cobra2d.network.common.command.client.CreateEntityClientCommand;
import com.github.schuettec.cobra2d.network.common.command.client.RemoveEntityClientCommand;
import com.github.schuettec.cobra2d.network.common.command.client.UpdateClientCameraCommand;
import com.github.schuettec.cobra2d.network.common.command.client.UpdateEntityClientCommand;
import com.github.schuettec.cobra2d.network.common.command.server.BasicPlayerAccess;
import com.github.schuettec.cobra2d.network.common.command.server.PlayerAccess;
import com.github.schuettec.cobra2d.network.common.command.server.ServerCommand;
import com.github.schuettec.cobra2d.network.data.EntityState;
import com.github.schuettec.cobra2d.network.data.EntityStateManager;
import com.github.schuettec.cobra2d.renderer.Renderer;
import com.github.schuettec.cobra2d.renderer.RendererException;
import com.github.schuettec.cobra2d.world.ActiveWorldUpdater;
import com.github.schuettec.cobra2d.world.Cobra2DWorld;
import com.github.schuettec.cobra2d.world.Collision;
import com.github.schuettec.cobra2d.world.WorldAccess;
import com.github.schuettec.cobra2d.world.WorldListener;

/**
 * Cobra2D networking game server.
 */
public class Cobra2DServer implements Renderer, WorldListener {

	private static final NoInputController NO_INPUT_CONTROLLER = new NoInputController();
	private int tcpPort;
	private int udpPort;
	private Server server;

	private Cobra2DEngine engine;

	private ActiveWorldUpdater updater;

	private EntityStateManager stateManager;
	private WorldAccess worldAccess;

	private Map<Connection, Player> playersByConnection;

	private Supplier<? extends PlayerAccess> playerAccessSupplier = BasicPlayerAccess::new;
	private Supplier<Entity> spawnPlayerEntitySupplier;
	private Function<Entity, ? extends Camera> playerCameraSupplier = (playerAccess) -> createBasicCamera();

	public Cobra2DServer() {

	}

	private Camera createBasicCamera() {
		return new BasicRectangleMapCamera(new Point(0, 0), new Dimension(1920, 1080), false);
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
		this.playersByConnection = new ConcurrentHashMap<>();
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
			    List<String> lastFrame = player.getLastFrameIds();
			    cameraCollision.stream()
			        .map(Collision::getOpponent)
			        .forEach(entity -> {
				        if (stateManager.isRegistered(entity)) {
					        thisFrameIds.add(entity.getId());
					        EntityState entityState = stateManager.readEntityState(entity);
					        String entityClass = entity.getClass()
					            .getName();
					        ClientCommand command = null;
					        if (lastFrame.contains(entity.getId())) {
						        command = new UpdateEntityClientCommand(entityClass, entityState);
					        } else {
						        command = new CreateEntityClientCommand(entityClass, entityState);
					        }
					        sendCommand(connection, command);

					        // Update player camera
					        String followEntityId = playerCamera.getFollowEntityId();
					        Point position = playerCamera.getPosition();
					        UpdateClientCameraCommand updateCamera = new UpdateClientCameraCommand(position, followEntityId);
					        sendCommand(connection, updateCamera);
				        }
			        });
			    lastFrame.removeAll(thisFrameIds);
			    lastFrame.stream()
			        .forEach(toRemoveEntityId -> {
				        RemoveEntityClientCommand removeCmd = new RemoveEntityClientCommand(toRemoveEntityId);
				        sendCommand(connection, removeCmd);
			        });
			    lastFrame.clear();
			    lastFrame.addAll(thisFrameIds);
		    });
	}

	private void sendCommand(Connection connection, ClientCommand command) {
		if (command.isTCP()) {
			connection.sendTCP(command);
		} else {
			connection.sendUDP(command);
		}
	}

	private void createPlayerAndSpawn(Connection connection) {
		Entity playerEntity = spawnPlayerEntitySupplier.get();
		PlayerAccess playerAccess = playerAccessSupplier.get();
		Camera playerCamera = playerCameraSupplier.apply(playerEntity);
		Player player = new Player("unknown player", connection, playerCamera, playerEntity, playerAccess);
		playerAccess.setPlayer(player);
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
		getPlayer(connection).ifPresent(player -> {
			if (object instanceof ServerCommand) {
				ServerCommand serverCommand = (ServerCommand) object;
				serverCommand.perform(worldAccess, player.getPlayerAccess());
			}
		});
	}

	@Override
	public Controller getControllerForEntity(Entity entity) {
		return getPlayerByEntity(entity).map(p -> p.getController())
		    .orElseGet(() -> {
			    return NO_INPUT_CONTROLLER;
		    });
	}

	private Optional<Player> getPlayerByEntity(Entity entity) {
		String id = entity.getId();
		return this.playersByConnection.values()
		    .stream()
		    // Either the entity can be controllable in a network game
		    // or the camera of the player
		    .filter(p -> p.getEntity()
		        .getId()
		        .equals(id)
		        || p.getPlayerCamera()
		            .getId()
		            .equals(id))
		    .findFirst();
	}

	private Optional<Player> getPlayer(Connection connection) {
		return Optional.ofNullable(this.playersByConnection.get(connection));
	}

	@Override
	public void start() {
		if (isNull(spawnPlayerEntitySupplier)) {
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

	public void setSpawnEntityFactory(Supplier<Entity> spawnEntitySupplier) {
		this.spawnPlayerEntitySupplier = spawnEntitySupplier;
	}

	public void setPlayerAccessFactory(Supplier<? extends PlayerAccess> playerAccessSupplier) {
		this.playerAccessSupplier = playerAccessSupplier;
	}

	public void setPlayerCameraFactory(Function<Entity, ? extends Camera> playerCameraSupplier) {
		this.playerCameraSupplier = playerCameraSupplier;
	}

}
