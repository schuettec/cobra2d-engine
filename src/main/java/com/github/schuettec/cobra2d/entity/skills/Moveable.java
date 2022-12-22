package com.github.schuettec.cobra2d.entity.skills;

import java.io.Serializable;

public interface Moveable extends Serializable {

	// Gives the entity the chance to calculate the new position for the next
	// frame
	public void next();
}
