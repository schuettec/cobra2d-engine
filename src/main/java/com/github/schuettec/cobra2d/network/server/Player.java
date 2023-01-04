package com.github.schuettec.cobra2d.network.server;

import java.util.LinkedList;
import java.util.List;

import com.esotericsoftware.kryonet.Connection;
import com.github.schuettec.cobra2d.controller.Controller;
import com.github.schuettec.cobra2d.entity.skills.Camera;
import com.github.schuettec.cobra2d.entity.skills.Entity;
import com.github.schuettec.cobra2d.network.common.command.server.PlayerAccess;

public class Player {

	private String name;

	private Connection connection;

	private Camera playerCamera;

	private Entity entity;

	private List<String> lastFrameIds;

	private PlayerAccess playerAccess;

	private NetworkController controller;

	public Player(String name, Connection connection, Camera playerCamera, Entity entity, PlayerAccess playerAccess) {
		super();
		this.name = name;
		this.connection = connection;
		this.playerCamera = playerCamera;
		this.entity = entity;
		this.playerAccess = playerAccess;
		this.lastFrameIds = new LinkedList<>();
		this.controller = new NetworkController();
	}

	public String getName() {
		return name;
	}

	public Connection getConnection() {
		return connection;
	}

	public Camera getPlayerCamera() {
		return playerCamera;
	}

	public Entity getEntity() {
		return entity;
	}

	public PlayerAccess getPlayerAccess() {
		return playerAccess;
	}

	public PlayerAccess getPlayerAccessAs(Class<? extends PlayerAccess> fragment) {
		return playerAccess;
	}

	List<String> getLastFrameIds() {
		return lastFrameIds;
	}

	void setLastFrameIds(List<String> lastFrameIds) {
		this.lastFrameIds = lastFrameIds;
	}

	public Controller getController() {
		return controller;
	}

}
