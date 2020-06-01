package com.planetas.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 * @author Víctor Díaz
 */

// Clase que crea o planetario
public class Planetas extends Game implements InputProcessor {


	private int dragX, dragY;
	private float lonxitude, latitude;

	private Mesh meshSol;
	private Mesh meshPlaneta1;
	private Mesh meshPlaneta2;
	private Mesh meshPlaneta3;
	private Mesh meshUniverso;

	private Elemento3D sol;
	private Elemento3D planeta1;
	private Elemento3D planeta2;
	private Elemento3D planeta3;
	private Elemento3D universo;

	private ShaderProgram shaderProgram;

	private Texture texturaSol;
	private Texture textura1;
	private Texture textura2;
	private Texture textura3;
	private Texture texturaUniverso;

	private PerspectiveCamera camara3d;

	// variables da posición da cámara
	float camPosX, camPosY, camPosZ;

	@Override
	public void create() {
		// TODO Auto-generated method stub

		Gdx.input.setInputProcessor(this);
		lonxitude=0;
		latitude=0;
		camPosX=0;
		camPosY=0;
		camPosZ=40;

		shaderProgram = new ShaderProgram(Gdx.files.internal("vertex.vert"), Gdx.files.internal("fragment.frag"));
		if (shaderProgram.isCompiled() == false) {
			Gdx.app.log("ShaderError", shaderProgram.getLog());
			System.exit(0);
		}


		// cargamos o modelo esfera para todos os obxectos
		ModelLoader loader = new ObjLoader();
		Model model = loader.loadModel(Gdx.files.internal("esfera.obj"));
		meshSol = model.meshes.get(0);
		meshPlaneta1 = model.meshes.get(0);
		meshPlaneta2 = model.meshes.get(0);
		meshPlaneta3 = model.meshes.get(0);
		meshUniverso = model.meshes.get(0);

		FileHandle ifhSol = Gdx.files.internal("sol.jpg");
		texturaSol = new Texture(ifhSol);
		FileHandle ifh1 = Gdx.files.internal("terra.jpg");
		textura1 = new Texture(ifh1);
		FileHandle ifh2 = Gdx.files.internal("marte.jpg");
		textura2 = new Texture(ifh2);
		FileHandle ifh3 = Gdx.files.internal("jupiter.jpg");
		textura3 = new Texture(ifh3);
		FileHandle ifhUniverso = Gdx.files.internal("estrelas.jpg");
		texturaUniverso = new Texture(ifhUniverso);

		camara3d = new PerspectiveCamera();
		camara3d.fieldOfView = 60;

		sol =  new Elemento3D(0f, 0f, 2.5f, 0, 0f,-0.1f, 0f);
		planeta1 = new Elemento3D(00f, 5f, 1f, 1.4f,0f, 3f,0f);
		planeta2 = new Elemento3D(120f, 10f, 1.5f, -1f, 0f, 1.5f, 0f);
		planeta3 = new Elemento3D(240, 15f, 1.7f,0.6f, 0f, 1.8f, 0f);

		// O universo tamén é unha esfera enorme que xira lentamente para dar a sensación de que
		// é o conxunto de planetas, a modo de galaxia, o que xira
		universo = new Elemento3D(0, 0f, 80f, 0, 0f, -0.02f,0f);

	}


	@Override
	public void render() {

		// Borramos e redibuxamos o fondo en negro.
		Gdx.gl20.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT|GL20.GL_DEPTH_BUFFER_BIT);

		Gdx.gl20.glEnable(GL20.GL_DEPTH_TEST);
		Gdx.gl20.glEnable(GL20.GL_TEXTURE_2D);

		// Redebuxamos os planetas e o universo
		sol.update(Gdx.graphics.getDeltaTime());
		planeta1.update(Gdx.graphics.getDeltaTime());
		planeta2.update(Gdx.graphics.getDeltaTime());
		planeta3.update(Gdx.graphics.getDeltaTime());
		universo.update(Gdx.graphics.getDeltaTime());

		shaderProgram.begin();

		// Sol
		texturaSol.bind(0);

		shaderProgram.setUniformi("u_texture", 0);
		shaderProgram.setUniformMatrix("u_worldView", camara3d.combined.cpy().mul(sol.matriz));
		meshSol.render(shaderProgram, GL20.GL_TRIANGLES);

		// planetas
		textura1.bind(0);

		shaderProgram.setUniformi("u_texture", 0);
		shaderProgram.setUniformMatrix("u_worldView", camara3d.combined.cpy().mul(planeta1.matriz));
		meshPlaneta1.render(shaderProgram, GL20.GL_TRIANGLES);

		textura2.bind(0);

		shaderProgram.setUniformi("u_texture", 0);
		shaderProgram.setUniformMatrix("u_worldView", camara3d.combined.cpy().mul(planeta2.matriz));
		meshPlaneta2.render(shaderProgram, GL20.GL_TRIANGLES);


		textura3.bind(0);

		shaderProgram.setUniformi("u_texture", 0);
		shaderProgram.setUniformMatrix("u_worldView", camara3d.combined.cpy().mul(planeta3.matriz));
		meshPlaneta3.render(shaderProgram, GL20.GL_TRIANGLES);

		// Universo
		texturaUniverso.bind(0);

		shaderProgram.setUniformi("u_texture", 0);
		shaderProgram.setUniformMatrix("u_worldView", camara3d.combined.cpy().mul(universo.matriz));
		meshUniverso.render(shaderProgram, GL20.GL_TRIANGLES);

		shaderProgram.end();

		Gdx.gl20.glDisable(GL20.GL_TEXTURE_2D);
		Gdx.gl20.glDisable(GL20.GL_DEPTH_TEST);

	}

	@Override
	public void resize (int width,int height){
		// Definimos os parámetros da cámara

		float aspectRatio = (float) width / (float) height;
		camara3d.viewportWidth=aspectRatio*1f;
		camara3d.viewportHeight=1f;
		camara3d.far=1000f;
		camara3d.near=0.1f;
		camara3d.position.set(camPosX,camPosY,camPosZ);
		camara3d.lookAt(0,0,0);
		camara3d.update();
	}

	@Override
	public void dispose(){
		meshUniverso.dispose();
		meshPlaneta1.dispose();
		meshPlaneta2.dispose();
		shaderProgram.dispose();

	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		dragX = screenX;
		dragY = screenY;
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {

		// se desplazamos o dedo (ou rato co botón pulsado)
		// cambiaremos a cámara (vista) de posición.

		lonxitude += (float)(screenX-dragX)/(float)Gdx.graphics.getWidth() * 90f;
		if (lonxitude >= 360) {lonxitude -= 360;}
		if (lonxitude < 0) {lonxitude += 360;}
		latitude += (float)(dragY-screenY)/(float)Gdx.graphics.getHeight() * 90f;
		if (latitude >= 360) {latitude -= 360;}
		if (latitude < 0) {latitude += 360;}

		dragX = screenX;
		dragY = screenY;

		camPosX = (float) Math.sin(Math.toRadians(lonxitude)) * (float) Math.cos(Math.toRadians(latitude)) * -40;
		camPosY = (float) Math.sin(Math.toRadians(lonxitude)) * (float) Math.sin(Math.toRadians(latitude)) * -40;
		camPosZ = (float) Math.cos(Math.toRadians(lonxitude)) * 40;

		System.out.println(lonxitude + " " + latitude);

		camara3d.position.set(camPosX, camPosY , camPosZ);

		// Sempre diriximos a cámara ao "centro" do universo (Sol)
		camara3d.lookAt(0,0,0);
		camara3d.update();
		return true;

	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}



}

