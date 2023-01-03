package com.github.schuettec.cobra2d.network.server;

import java.util.LinkedList;
import java.util.List;

import com.esotericsoftware.kryonet.Connection;
import com.github.schuettec.cobra2d.entity.skills.Camera;
import com.github.schuettec.cobra2d.entity.skills.Entity;

public class Player {

	private String name;

	private Connection connection;

	private Camera playerCamera;

	private Entity entity;

	private List<String> lastFrameIds;

	public Player(String name, Connection connection, Camera playerCamera, Entity entity) {
		super();
		this.name = name;
		this.connection = connection;
		this.playerCamera = playerCamera;
		this.entity = entity;
		this.lastFrameIds = new LinkedList<>();
	}

	public String getName() {
		return name;
	}

	Connection getConnection() {
		return connection;
	}

	Camera getPlayerCamera() {
		return playerCamera;
	}

	Entity getEntity() {
		return entity;
	}

	List<String> getLastFrameIds() {
		return lastFrameIds;
	}

	void setLastFrameIds(List<String> lastFrameIds) {
		this.lastFrameIds = lastFrameIds;
	}

}
