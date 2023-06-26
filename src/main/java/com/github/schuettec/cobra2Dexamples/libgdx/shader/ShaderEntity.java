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

			String fragmentShader = """
					// shader playback time (in seconds)
					uniform float     time;
					uniform vec2      size;

					varying vec2 v_texCoords;

					uniform sampler2D u_texture;

					void main()
					{
					            vec2 p = v_texCoords; // gl_FragCoord.xy/size.xy;

					            p.x += sin(p.y * 15. + time * 2.) / 800.;
					            p.y += cos(p.x * 10. + time * 2.) / 900.;

					            p.x += sin((p.y+p.x) * 15. + time * 2.) / (180. + (2. * sin(time)));
					            p.y += cos((p.y+p.x) * 15. + time * 2.) / (200. + (2. * sin(time)));

					            gl_FragColor  =  texture2D(u_texture, p);
					}
							""";

			String blackWhiteFragmentShader = """
					#ifdef GL_ES
					#define LOWP lowp
					precision mediump float;
					#else
					#define LOWP
					#endif

					void main()
					{
					  gl_FragColor = vec4(1,0,0,1);
					}
					""";

			this.shaderProgram = new ShaderProgram(defaultVertextShader, fragmentShader);
		}

		LibGdxExtendedAccess extendedRenderer = renderer.extendedRenderer(LibGdxExtendedAccess.class);
		shaderProgram.setUniformf("time", time);
		System.out.println("time: " + time);
		shaderProgram.setUniformf("size", (float) dimension.getWidth(), (float) dimension.getHeight());

		extendedRenderer.setShaderProgramm(shaderProgram);
		super.render(renderer, screenTranslation);
		extendedRenderer.resetShader();
	}

	@Override
	public void update(WorldAccess worldAccess, float deltaTime) {
		this.time += deltaTime;
	}
}
