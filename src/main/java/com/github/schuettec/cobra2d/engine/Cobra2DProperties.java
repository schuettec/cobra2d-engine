package com.github.schuettec.cobra2d.engine;

import static com.github.schuettec.cobra2d.engine.Cobra2DConstants.DEFAULT_TCP_PORT;
import static com.github.schuettec.cobra2d.engine.Cobra2DConstants.DEFAULT_UDP_PORT;
import static java.lang.String.valueOf;

import java.util.Properties;

import com.github.schuettec.cobra2d.renderer.RendererType;

public class Cobra2DProperties {

	private Properties properties;

	public Cobra2DProperties(Properties properties) {
		this.properties = properties;
		initProperties();
	}

	private void initProperties() {
		checkConfiguration(properties);
	}

	private void checkConfiguration(final Properties properties) {
		for (final String key : PROPERTIES_KEYS) {
			if (!properties.containsKey(key)) {
				throw new RuntimeException("Incorrect engine configuration: Missing configuration for '" + key + "'");
			}
		}
	}

	public boolean hasRenderer() {
		return !RendererType.NONE.equals(getRendererType());
	}

	public boolean isCreateController() {
		return Boolean.parseBoolean(properties.getProperty(CREATE_CONTROLLER));
	}

	public boolean getFullscreen() {
		return Boolean.parseBoolean(properties.getProperty(FULLSCREEN));
	}

	public boolean isDoMapUpdate() {
		return Boolean.parseBoolean(properties.getProperty(MAP_UPDATE));
	}

	public int getFPS() {
		return Integer.parseInt(properties.getProperty(REQUESTED_FPS));
	}

	public int getRefreshRate() {
		return Integer.parseInt(properties.getProperty(REFRESH_REATE));
	}

	public int getBitDepth() {
		return Integer.parseInt(properties.getProperty(BIT_DEPHT));
	}

	public int getResolutionY() {
		return Integer.parseInt(properties.getProperty(RESOLUTION_Y));
	}

	public int getResolutionX() {
		return Integer.parseInt(properties.getProperty(RESOLUTION_X));
	}

	public int getTcpPort() {
		return Integer.parseInt(properties.getProperty(TCP_PORT, valueOf(DEFAULT_TCP_PORT)));
	}

	public int getUdpPort() {
		return Integer.parseInt(properties.getProperty(UDP_PORT, valueOf(DEFAULT_UDP_PORT)));
	}

	public RendererType getRendererType() {
		String property = this.properties.getProperty(RENDERER);
		return RendererType.valueOf(property.toUpperCase());
	}

	public ResourceLocation getResourceLocation() {
		String property = this.properties.getProperty(RESOURCE_LOCATION);
		return ResourceLocation.valueOf(property.toUpperCase());
	}

	/**
	 * This constant defines the property key, used to read the x-resolution from
	 * the configuration file or properties.
	 */
	public static final String RESOLUTION_X = "resolution-x";
	/**
	 * This constant defines the property key, used to read the y-resolution from
	 * the configuration file or properties.
	 */
	public static final String RESOLUTION_Y = "resolution-y";
	/**
	 * This constant defines the property key, used to read the bit-depht of the
	 * renderer from the configuration file or properties.
	 */
	public static final String BIT_DEPHT = "bit-depht";
	/**
	 * This constant defines the property key, used to read the refresh rate of the
	 * screen from the configuration file or properties.
	 */
	public static final String REFRESH_REATE = "refresh-rate";
	/**
	 * This constant defines the property key, used to read the requested frames per
	 * second from the configuration file or properties.
	 */
	public static final String REQUESTED_FPS = "fps";
	/**
	 * This constant defines the property key, used to read the fullscreen flag from
	 * the configuration file or properties.
	 */
	public static final String FULLSCREEN = "fullscreen";
	/**
	 * This constant defines the property key, used to read the map-update flag from
	 * the configuration file or properties.
	 */
	public static final String MAP_UPDATE = "map-update";
	/**
	 * This constant defines the property key, used to specify ne renderer type.
	 * Currently the Java2D renderer and no renderer is available.
	 */
	public static final String RENDERER = "renderer";
	/**
	 * This constant defines the property key, used to specify from what type of
	 * location resources are loaded.
	 */
	public static final String RESOURCE_LOCATION = "resource-type";
	/**
	 * This constant defines the property key, used to instruct the engine to create an input controller for the specified
	 * renderer.
	 */
	public static final String CREATE_CONTROLLER = "create-controller";
	/**
	 * TCP port for game server hosting. Only needed if the dedicated server is set as renderer.
	 */
	public static final String TCP_PORT = "tcp-port";
	/**
	 * TCP port for game server hosting. Only needed if the dedicated server is set as renderer.
	 */
	public static final String UDP_PORT = "udp-port";
	/**
	 * Property keys required for a valid engine configuration-
	 */
	protected static final String[] PROPERTIES_KEYS = new String[] {
	    RESOLUTION_X, RESOLUTION_Y, BIT_DEPHT, REFRESH_REATE, REQUESTED_FPS, FULLSCREEN, MAP_UPDATE, RENDERER,
	    RESOURCE_LOCATION
	};

}
