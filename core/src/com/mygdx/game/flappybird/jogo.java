package com.mygdx.game.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Random;

public class jogo extends ApplicationAdapter {


	private int pontos = 0;
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
	BitmapFont textPontuacao;			//gera a pontuação na tela
	private boolean passouCano = false;	// verifica se o passaro passou pelo cano

	private ShapeRenderer shapeRenderer;
	private Circle circuloPassaro;
	private Rectangle retanguloCanoCima;
	private Rectangle retanguloCanoBaixo;

	@Override
	public void create () {

		inicializarTexturas();									//criação dos metodos
		InicializarObjetos();

	}

	private void InicializarObjetos() {


		batch = new SpriteBatch();
		random = new Random();

		larguraDispositivo = Gdx.graphics.getWidth();			//identifica a largura da tela do dispositivo
		alturaDispositivo = Gdx.graphics.getHeight();			//identifica a altura da tela do dispositivo
		posicaoInicialVerticalPassaro = alturaDispositivo / 2;	//altura inicial do passaro de acordo com a tela do dispositivo
		posicaoCanoHorizonatal = larguraDispositivo;
		espacoEntreCanos = 350;

		textPontuacao = new BitmapFont();
		textPontuacao.setColor(Color.WHITE);  // inicia a pontuação e a posiciona na tela
		textPontuacao.getData().setScale(10);

	}

	private void inicializarTexturas() {


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

		verificarEstadoJogo(); // metodo que que contem os cofigos que verificam o estado do jogo
		desenharTexturas();   // metodo que contem os codigos para desenhar as texturas
		validarPontos();
		detectarColisao();

	}

	private void detectarColisao() {

		// desenha a area de colisão dos canos e do passaro
		circuloPassaro.set(50 + passaros[0].getWidth() / 2, posicaoInicialVerticalPassaro + passaros[0].getHeight() /2, passaros[0].getWidth() /2 );

		retanguloCanoCima.set(posicaoCanoHorizonatal,
				alturaDispositivo /2 - canoTopo.getHeight() - espacoEntreCanos /2 + posicaoCanoVertical, canoTopo.getWidth(), canoTopo.getHeight());

		retanguloCanoBaixo.set(posicaoCanoHorizonatal,
				alturaDispositivo / 2 - canoBaixo.getHeight() - espacoEntreCanos / 2 + posicaoCanoVertical, canoBaixo.getWidth(), canoBaixo.getHeight());


		boolean colisaoCanoCima = Intersector.overlaps(circuloPassaro, retanguloCanoCima);
		boolean colisaoCanoBaixo = Intersector.overlaps(circuloPassaro, retanguloCanoBaixo);

		if(colisaoCanoBaixo || colisaoCanoCima ){
			Gdx.app.log("Log", "Bateu!");
		}
	}

	private void validarPontos() {
		if(posicaoCanoHorizonatal < 50 - passaros[0].getWidth()){
			if(!passouCano){											//verifica se o passaro tocou no cano
				pontos++;
				passouCano = true;
			}

		}
	}

	private void verificarEstadoJogo() {

		posicaoCanoHorizonatal-= Gdx.graphics.getDeltaTime() * 200;
		if(posicaoCanoHorizonatal < - canoBaixo.getWidth()){		//desenha os canos nas tela
			posicaoCanoHorizonatal = larguraDispositivo;
			posicaoCanoVertical = random.nextInt(400) -200;
			passouCano = false;
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
		//desenha os passaro o fundo  e os canos no game
		batch.draw(fundo, 0, 0, larguraDispositivo, alturaDispositivo);
		batch.draw(passaros[(int) variacao], 50,  posicaoInicialVerticalPassaro);
		batch.draw(canoBaixo, posicaoCanoHorizonatal, alturaDispositivo /2 - canoBaixo.getHeight() - espacoEntreCanos /2 + posicaoCanoVertical);
		batch.draw(canoTopo,posicaoCanoHorizonatal,alturaDispositivo/2 + espacoEntreCanos /2 + posicaoCanoVertical);
		textPontuacao.draw(batch, String.valueOf(pontos), larguraDispositivo / 2, alturaDispositivo - 100);

		batch.end();

	}

	@Override
	public void dispose () {

	}
}
