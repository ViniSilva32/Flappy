package com.mygdx.game.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class jogo extends ApplicationAdapter {


	private SpriteBatch batch;
	private Texture [] passaros;	//criação das variaveis para que o jogo entenda a imagem que será colocada
	private Texture fundo;


	private int movimentaY = 0;	//movimentam o passaro
	private int movimentaX = 0;



	private float larguraDispositivo;	//deixam o game responsivo
	private float alturaDispositivo;


	private float variacao = 0;  // criação do passaro
	private float gravidade = 0; // gravidade para fazer o passaro cair
	private float posicaoInicialVerticalPassaro = 0; // posição inicial do passaro na tela

	@Override
	public void create () {

		batch = new SpriteBatch();
		passaros = new Texture[3];
		passaros [0] = new Texture("passaro1.png");
		passaros [1] = new Texture("passaro2.png");	//sprites para fazer a animação do passarinho
		passaros [2] = new Texture("passaro3.png");
		fundo = new Texture("fundo.png");


		larguraDispositivo = Gdx.graphics.getWidth();	//identifica a largura da tela do dispositivo

		alturaDispositivo = Gdx.graphics.getHeight();	//identifica a altura da tela do dispositivo

		posicaoInicialVerticalPassaro = alturaDispositivo / 2;	//altura inicial do passaro de acordo com a tela do dispositivo
	}

	@Override
	public void render () {

		batch.begin();

		if(variacao > 3)
			variacao = 0;
		boolean toqueTela = Gdx.input.justTouched();
		if (Gdx.input.justTouched()){
			gravidade = -25;
		}
		if(posicaoInicialVerticalPassaro > 0 || toqueTela)
			posicaoInicialVerticalPassaro = posicaoInicialVerticalPassaro - gravidade;

		batch.draw(fundo, 0, 0, larguraDispositivo, alturaDispositivo);
		batch.draw(passaros[(int) variacao], 30, posicaoInicialVerticalPassaro);


		variacao += Gdx.graphics.getDeltaTime() * 10; //altera a velocidade das asas do passaro

		gravidade++;
		movimentaY++;
		movimentaX++;
		batch.end();

	}
	
	@Override
	public void dispose () {

	}
}
