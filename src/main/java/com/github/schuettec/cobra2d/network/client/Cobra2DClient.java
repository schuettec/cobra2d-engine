package com.github.schuettec.cobra2d.network.client;

import static com.github.schuettec.cobra2d.network.common.Cobra2DNetwork.registerCommandClasses;

import java.io.IOException;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.github.schuettec.cobra2d.network.common.Cobra2DNetwork;
import com.github.schuettec.cobra2d.network.common.command.UpdateCommand;

public class Cobra2DClient {

	private Client client;

	public void connect(String ip) {
		connect(ip, Cobra2DNetwork.DEFAULT_TCP_PORT, Cobra2DNetwork.DEFAULT_UDP_PORT);
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
				if (object instanceof UpdateCommand) {
					UpdateCommand updateCommand = (UpdateCommand) object;
					System.out.println(updateCommand);
				}
			}
		});
	}

	public void disconnect() {
		client.close();
		client.stop();
		try {
			client.dispose();
		} catch (IOException e) {
		}
	}

	public void test() {
		// UpdateCommand request = new UpdateCommand(Integer.MAX_VALUE, "Hello World!");
		// client.sendUDP(request);
	}

	public static void main(String[] args) {
		Cobra2DClient client = new Cobra2DClient();
		client.connect("localhost");
		client.test();
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
		}
		client.disconnect();
	}
}
