package com.github.schuettec.cobra2d.network.common;

import java.util.Hashtable;
import java.util.LinkedList;

import com.badlogic.gdx.math.Circle;
import com.esotericsoftware.kryo.Kryo;
import com.github.schuettec.cobra2d.math.Dimension;
import com.github.schuettec.cobra2d.math.EntityPoint;
import com.github.schuettec.cobra2d.math.Line;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.math.Polygon;
import com.github.schuettec.cobra2d.math.Rectangle;
import com.github.schuettec.cobra2d.network.common.command.client.CreateEntityClientCommand;
import com.github.schuettec.cobra2d.network.common.command.client.RemoveEntityClientCommand;
import com.github.schuettec.cobra2d.network.common.command.client.UpdateClientCameraCommand;
import com.github.schuettec.cobra2d.network.common.command.client.UpdateEntityClientCommand;
import com.github.schuettec.cobra2d.network.common.command.server.UpdateKeysControllerCommand;
import com.github.schuettec.cobra2d.network.common.command.server.UpdateMouseControllerCommand;
import com.github.schuettec.cobra2d.network.data.EntityState;

public class Cobra2DNetwork {
	/**
	 * Registers the classes used by the engine to send update and controll commands.
	 * 
	 * @param kryo The kryo instance of the server/client.
	 */
	public static void registerCommandClasses(Kryo kryo) {
		kryo.register(UpdateMouseControllerCommand.class);
		kryo.register(UpdateClientCameraCommand.class);
		kryo.register(UpdateEntityClientCommand.class);
		kryo.register(CreateEntityClientCommand.class);
		kryo.register(RemoveEntityClientCommand.class);
		kryo.register(UpdateKeysControllerCommand.class);
		kryo.register(EntityState.class);
		kryo.register(Point.class);
		kryo.register(Polygon.class);
		kryo.register(Circle.class);
		kryo.register(Dimension.class);
		kryo.register(EntityPoint.class);
		kryo.register(Line.class);
		kryo.register(Rectangle.class);
		kryo.register(Hashtable.class);
		kryo.register(LinkedList.class);
	}
}
