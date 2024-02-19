package com.github.schuettec.cobra2Dexamples.libgdx.shader;

import static java.util.Objects.isNull;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.github.schuettec.cobra2Dexamples.textureRendering.TexturedEntity;
import com.github.schuettec.cobra2d.entity.skills.Updatable;
import com.github.schuettec.cobra2d.math.Dimension;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.renderer.RendererAccess;
import com.github.schuettec.cobra2d.renderer.libgdx.LibGdxExtendedAccess;
import com.github.schuettec.cobra2d.world.WorldAccess;

public class ShaderEntity extends TexturedEntity implements Updatable {

	private ShaderProgram shaderProgram;
	private float time;
	private Dimension dimension;

	public ShaderEntity(String textureId, Point worldCoordinates, Dimension intialDimension, int layer) {
		super(textureId, worldCoordinates, intialDimension, layer);
		this.dimension = intialDimension;
	}

	@Override
	public void initialize(RendererAccess rendererAccess) {
		super.initialize(rendererAccess);

	}

	@Override
	public void render(RendererAccess renderer, Point screenTranslation) {

		if (isNull(shaderProgram)) {
			String defaultVertextShader = """
							attribute vec4 a_position;
							attribute vec4 a_color;
							attribute vec2 a_texCoord0;

							uniform mat4 u_projTrans;

							varying vec4 v_color;
							varying vec2 v_texCoords;

							void main() {
							    v_color = a_color;
							    v_texCoords = a_texCoord0;
							    gl_Position = u_projTrans * a_position;
							}
					""";

			String defaultFragmentShader = """
					#ifdef GL_ES
					    precision mediump float;
					#endif

					varying vec4 v_color;
					varying vec2 v_texCoords;
					uniform sampler2D u_texture;
					uniform mat4 u_projTrans;

					void main() {
					         gl_FragColor = texture2D(u_texture, v_texCoords);
					}
					""";

			String default2FragmentShader = """
										#ifdef GL_ES
					#define LOWP lowp
					    precision mediump float;
					#else
					    #define LOWP
					#endif

					varying LOWP vec4 v_color;
					varying vec2 v_texCoords;

					uniform sampler2D u_texture;

					void main()
					{
					    gl_FragColor = 0.5 * texture2D(u_texture, v_texCoords);
					}
										""";

			String fragmentShader = """
					// shader playback time (in seconds)
					uniform float     time;
					uniform float     pos;
					uniform vec2      size;

					varying vec2 v_texCoords;

					uniform sampler2D u_texture;

					void main()
					{
						vec2 p = v_texCoords;
						p.x += sin(p.y * 5. + time * 2.) / 800.;
						p.y += cos(p.x * 2. + time * 4.) / 900.;

						p.x += sin((p.y+p.x) * 15. + time * 2.) / (180. + (2. * sin(time)));
						p.y += cos((p.y+p.x) * 15. + time * 2.) / (200. + (2. * sin(time)));


						gl_FragColor  =  texture2D(u_texture, p);
					}
							""";

			String waterFragmentShader = """
					// shader playback time (in seconds)
						uniform float     time;
						uniform float     pos;
						uniform vec2      size;

						varying vec2 v_texCoords;

						uniform sampler2D u_texture;

						const float freq = 2;
						const float amp = 0.6;
						const float offY = 0;

						void main()
						{
							vec2 p = v_texCoords;

							//p.x += sin(p.y * 5. + time * 2.) / 800.;
							//p.y += cos(p.x * 2. + time * 4.) / 900.;

							//p.x += sin((p.y+p.x) * 5. + time * 2.) / (180. + (2. * sin(time)));
							//p.y += cos((p.y+p.x) * 15. + time * 2.) / (200. + (2. * sin(time)));


						    float offX = time;

							float sinX = sin((v_texCoords.x + offX) * freq ) * (amp-0.5) + (offY + 0.5);
							float sinY = sin((v_texCoords.y + offX) * freq ) * (amp-0.5) + (offY + 0.5);


							vec4 color1 = vec4(sinX,sinX,sinX,1);
							vec4 color2 = vec4(sinY,sinY,sinY,1);

							vec2 position = v_texCoords - vec2(p.x-sinX,p.y-sinY);

							gl_FragColor  = texture2D(u_texture, position ) ;
						}

											""";

			this.shaderProgram = new ShaderProgram(defaultVertextShader, waterFragmentShader);
		}

		LibGdxExtendedAccess extendedRenderer = renderer.extendedRenderer(LibGdxExtendedAccess.class);
		shaderProgram.bind();
		shaderProgram.setUniformf("time", time);
		System.out.println("time: " + time);
		shaderProgram.setUniformf("size", (float) dimension.getWidth(), (float) dimension.getHeight());

		float pos = (float) (((time) % dimension.getWidth()) % 1f);
		System.out.println("pos:" + pos);
		shaderProgram.setUniformf("pos", pos);

		extendedRenderer.setShaderProgramm(shaderProgram);
		super.render(renderer, screenTranslation);
		extendedRenderer.resetShader();
	}

	@Override
	public void update(WorldAccess worldAccess, float deltaTime) {
		this.time += deltaTime;
	}
}
