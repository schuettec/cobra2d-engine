package com.github.schuettec.cobra2d.engine;

/**
 * Constants used by the engine to read values from the config file, or
 * determine the timeouts of reading and connection to ressources. You can use
 * the maven replace mechanism to replace those values by an external
 * configuration.
 *
 * @author Chris
 *
 */
public class Cobra2DConstants {

	/**
	 * This constant determines the read timeout of all ressources accessed by
	 * URLs.
	 */
	public static final int READ_TIMEOUT = 3000;
	/**
	 * This constant determines the timeout of creating a connection to the
	 * ressources accessed by URLs.
	 */
	public static final int CONNECTION_TIMEOUT = 3000;

	/**
	 * Default udp port if dedicated server is started.
	 */
	public static final int DEFAULT_UDP_PORT = 54777;
	/**
	 * Default tcp port if dedicated server is started.
	 */
	public static final int DEFAULT_TCP_PORT = 54555;

}
