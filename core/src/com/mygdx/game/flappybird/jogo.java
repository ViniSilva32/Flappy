package com.mygdx.game.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class jogo extends ApplicationAdapter {

	//criação das variaveis
	//para que o jogo entenda a imagem que será colocada
	private SpriteBatch batch;
	private Texture passaro;
	private Texture fundo;

	//movimentam o passaro
	private int movimentaY = 0;
	private int movimentaX = 0;


	//deixam o game responsivo
	private float larguraDispositivo;
	private float alturaDispositivo;

	@Override
	public void create () {
		//criação dos objetos para que possam ser gerados na tela
		batch = new SpriteBatch();
		passaro = new Texture("passaro1.png");
		fundo = new Texture("fundo.png");
		//identifica a largura da tela do dispositivo
		larguraDispositivo = Gdx.graphics.getWidth();
		//identifica a altura da tela do dispositivo
		alturaDispositivo = Gdx.graphics.getHeight();
	}

	@Override
	public void render () {

		batch.begin();
		//adequa a tela do game para cada dispositivo
		batch.draw(fundo, 0, 0, larguraDispositivo, alturaDispositivo);
		//renderiza o passaro na tela
		batch.draw(passaro, 50, 50, movimentaX, movimentaY);

		movimentaY++;
		movimentaX++;

		batch.end();
	}
	
	@Override
	public void dispose () {

	}
}
