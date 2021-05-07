package com.mygdx.game.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Random;

public class jogo extends ApplicationAdapter {



	private int movimentaX = 0;
	private int gravidade = 0; 									// gravidade para fazer o passaro cair
	//private int movimentaXcano = 0;

	private SpriteBatch batch;
	private Texture [] passaros;								//criação das variaveis para que o jogo entenda a imagem que será colocada
	private Texture canoTopo;
	private Texture canoBaixo;
	private Texture fundo;


	private float larguraDispositivo;							//deixam o game responsivo
	private float alturaDispositivo;
	private float variacao = 0;  								// criação do passaro
	private float posicaoInicialVerticalPassaro = 0;
	private float posicaoCanoHorizonatal;
	private float espacoEntreCanos;
	private float posicaoCanoVertical;

	private Random random;

	@Override
	public void create () {

		inicializarTexturas();									//criação dos metodos
		InicializarObjetos();

	}

	private void InicializarObjetos() {


		larguraDispositivo = Gdx.graphics.getWidth();			//identifica a largura da tela do dispositivo
		alturaDispositivo = Gdx.graphics.getHeight();			//identifica a altura da tela do dispositivo
		posicaoInicialVerticalPassaro = alturaDispositivo / 2;	//altura inicial do passaro de acordo com a tela do dispositivo
		posicaoCanoHorizonatal = larguraDispositivo;
		espacoEntreCanos = 150;
	}

	private void inicializarTexturas() {
		batch = new SpriteBatch();
		random = new Random();

		passaros = new Texture[3];
		passaros [0] = new Texture("passaro1.png");
		passaros [1] = new Texture("passaro2.png");	//sprites para fazer a animação do passarinho
		passaros [2] = new Texture("passaro3.png");

		fundo = new Texture("fundo.png");
		canoBaixo = new Texture("cano_baixo_maior.png");
		canoTopo = new Texture("cano_topo_maior.png");
	}

	@Override
	public void render () {

		verificarEstadoJogo(); //
		desenharTexturas();   //


	}

	private void verificarEstadoJogo() {

		posicaoCanoHorizonatal-= Gdx.graphics.getDeltaTime() * 200;
		if(posicaoCanoHorizonatal < - canoBaixo.getWidth()){
			posicaoCanoHorizonatal = larguraDispositivo;
			posicaoCanoHorizonatal = random.nextInt(400) -200;
		}

		boolean toqueTela = Gdx.input.justTouched(); 			// o passaro interage com o toque na tela
		if (Gdx.input.justTouched()){
			gravidade = -25;
		}
		if(posicaoInicialVerticalPassaro > 0 || toqueTela)
			posicaoInicialVerticalPassaro = posicaoInicialVerticalPassaro - gravidade;

		variacao += Gdx.graphics.getDeltaTime() * 10; 			//altera a velocidade das asas do passaro
		if(variacao > 3)
			variacao = 0;

		gravidade++;								//adiciona gravidade para fazer o passaro cair
		movimentaX++;
	}

	private void desenharTexturas () {

		batch.begin();
		batch.draw(fundo, 0, 0, larguraDispositivo, alturaDispositivo);
		batch.draw(passaros[(int) variacao], movimentaX,  posicaoInicialVerticalPassaro);
		batch.draw(canoBaixo, posicaoCanoHorizonatal, alturaDispositivo /2 - canoBaixo.getHeight() - espacoEntreCanos /2 + posicaoCanoHorizonatal);
		batch.draw(canoTopo,posicaoCanoHorizonatal,alturaDispositivo/2 + espacoEntreCanos /2 + posicaoCanoVertical);

		batch.end();

	}

	@Override
	public void dispose () {

	}
}
