package com.github.schuettec.cobra2Dexamples.moveableShapes;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Test {

	public static void main(String[] args) {
		Frame frame = new Frame();
		frame.setTitle("AstroCycles | By: Carlos Aviles");
		frame.setLocationRelativeTo(null);
		frame.setFocusable(true);
		frame.setSize(100, 100);
		frame.setBackground(Color.BLUE);
		frame.setVisible(true);
		frame.createBufferStrategy(3);

		do {
			BufferStrategy bs = frame.getBufferStrategy();
			while (bs == null) {
				bs = frame.getBufferStrategy();
			}
			do {
				// The following loop ensures that the contents of the drawing buffer
				// are consistent in case the underlying surface was recreated
				do {
					// Get a new graphics context every time through the loop
					// to make sure the strategy is validated
					Graphics graphics = bs.getDrawGraphics();

					// Render to graphics
					// ...
					graphics.setColor(Color.RED);
					graphics.fillRect(0, 0, 100, 100);
					// Dispose the graphics
					graphics.dispose();

					// Repeat the rendering if the drawing buffer contents
					// were restored
				} while (bs.contentsRestored());

				// Display the buffer
				bs.show();

				// Repeat the rendering if the drawing buffer was lost
			} while (bs.contentsLost());
			try {
				Thread.sleep(100);
			} catch (InterruptedException ex) {
				Logger.getLogger(Test.class.getName())
				    .log(Level.SEVERE, null, ex);
			}
		} while (true);
	}

}