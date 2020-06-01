package com.planetas.game;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

/**
 * @author Víctor Díaz
 */

// Modelo de esfera
public class Elemento3D {

    public Matrix4 matriz;
    public float escala;

    private float velocidade;
    private float radioOrbita;
    private float gradoPosicion;

    private float xiroX;
    private float xiroY;
    private float xiroZ;

    private Vector3 temp;

    private float rotarX;
    private float rotarY;
    private float rotarZ;

    // Construtor
    public Elemento3D(float pos, float radioOrbita, float escala, float velocidade, float xiroX, float xiroY, float xiroZ){

        this.matriz = new Matrix4();
        this.gradoPosicion = pos;
        this.escala=escala;
        this.radioOrbita = radioOrbita;
        this.velocidade = velocidade;

        this.xiroX = xiroX;
        this.xiroY = xiroY;
        this.xiroZ = xiroZ;

        temp = new Vector3();

    }

    // Método que actualiza posición e rotación
    public void update(float delta){

        gradoPosicion = (gradoPosicion + delta * velocidade);
        if (gradoPosicion >= 360) {gradoPosicion -= 360;}

        float posX = (float) Math.sin(gradoPosicion) * radioOrbita * 1.5f;
        float posZ = (float) Math.cos(gradoPosicion) * radioOrbita;


        rotarX += 60f * delta * xiroX;
        rotarY += 60f * delta * xiroY;
        rotarZ += 60f * delta * xiroZ;

        matriz.idt();
        matriz.translate(new Vector3(posX,0,posZ));
        matriz.scl(escala);
        matriz.rotate(1, 0, 0, rotarX);
        matriz.rotate(0, 1, 0, rotarY);
        matriz.rotate(0, 0, 1, rotarZ);

    }

}
