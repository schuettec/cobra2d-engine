package com.github.schuettec.cobra2d.renderer;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Model;

/**
 * Interface to define the access to the sound platform.
 */
public interface ModelAccess {

  public Model getModel(String modelRessourceId);

  public Camera getCamera3D();
}
