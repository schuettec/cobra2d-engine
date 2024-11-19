package com.github.schuettec.cobra2d.renderer.libgdx;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Model;
import com.github.schuettec.cobra2d.renderer.ModelAccess;
import com.github.schuettec.cobra2d.renderer.Sound;
import com.github.schuettec.cobra2d.renderer.SoundAccess;

public class LibGdxAccess implements SoundAccess, ModelAccess {

  private LibGdxRenderer libGdxRenderer;

  public LibGdxAccess(LibGdxRenderer libGdxRenderer) {
    this.libGdxRenderer = libGdxRenderer;
  }

  @Override
  public Sound getSound(String soundRessourceId) {
    com.badlogic.gdx.audio.Sound soundGdx = libGdxRenderer.getSound(soundRessourceId);
    Sound sound = new SoundAdapter(soundGdx);
    return sound;
  }

  @Override
  public Model getModel(String modelRessourceId) {
    return libGdxRenderer.getModel(modelRessourceId);
  }

  public Camera getCamera3D() {
    return libGdxRenderer.getCamera3D();
  }

}
