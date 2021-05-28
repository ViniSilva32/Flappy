package com.mygdx.game.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
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

	private int pontuacaoMaxima = 0;
	private int pontos = 0;										//Para gerar, e somar e mostrar os pontos
	private int gravidade = 0; 									//gravidade para fazer o passaro cair
	private int estadoJogo = 0;									//para definir o estado do game após cada ação do jogador

	private Texture [] passaros;								//criação das variaveis para que o jogo reconheça a imagem que servirá como sprite
	private Texture canoTopo;									//CanoSuperior
	private Texture canoBaixo;									//CanoInferior
	private Texture fundo;										//cenário
	private Texture gameover;									//GameOver

	private float larguraDispositivo;							//deixa o game responsivo de acordo com a tela do aparelho
	private float alturaDispositivo;
	private float variacao = 0;  								// variação da animção do pássaro
	private float posicaoInicialVerticalPassaro = 0;			// define a posicção inicial do passaro
	private float posicaoHorizontalPassaro = 0;
	private float posicaoCanoHorizonatal;						//define a posição dos canos
	private float espacoEntreCanos;								//define a distancia entre os canos
	private float posicaoCanoVertical;							//define a posição inicial dos canos

	private SpriteBatch batch;

	private Random random;										//random

	private boolean passouCano = false;							//verifica se o passaro passou pelo cano

	BitmapFont textPontuacao;									//BitmapFont criado para desenhar a pontuação na tela
	BitmapFont textReiniciar;									//BitmapFont criado para desenhar a opção de reiniciar na tela
	BitmapFont textMelhorPontuacao;								//BitmapFont criado para desenhar a pontuação total após o GameOver na tela

	Sound somVoando;
	Sound somColisao;
	Sound somPontuacao;

	private ShapeRenderer shapeRenderer;
	private Circle circuloPassaro;								//criação circuloPassaro
	private Rectangle retanguloCanoCima;						//criação retanguloCanoCima
	private Rectangle retanguloCanoBaixo;						//criação retanguloCanoBaixo

	Preferences preferencais;

	@Override
	public void create ()
	{
		inicializarTexturas();									//Metodo para iniciar as texturas da cena
		InicializarObjetos();									//Metodo para iniciar os objetos da cena
	}
	@Override
	public void render ()
	{
		verificarEstadoJogo(); 									//metodo que que contem os cofigos que verificam o estado do jogo
		desenharTexturas();   									//metodo que contem os codigos para desenhar as texturas
		validarPontos();										//metodo para validar os pontos ao passar pelos canos
		detectarColisao();										//metodo para detectar a colisão do passaro com os canos
	}
	private void InicializarObjetos()
	{
		batch = new SpriteBatch();
		random = new Random();									//Randomiza a posição do proximo cano

		larguraDispositivo = Gdx.graphics.getWidth();			//identifica a largura da tela do dispositivo
		alturaDispositivo = Gdx.graphics.getHeight();			//identifica a altura da tela do dispositivo
		posicaoInicialVerticalPassaro = alturaDispositivo / 2;	//altura inicial do passaro de acordo com a tela do dispositivo
		posicaoCanoHorizonatal = larguraDispositivo;
		espacoEntreCanos = 500;									//Define o espaço entre os canos

		textPontuacao = new BitmapFont();								//inicializa a fonte
		textPontuacao.setColor(com.badlogic.gdx.graphics.Color.WHITE);  //define a cor da textura
		textPontuacao.getData().setScale(10);							//define o tamanho da fonte que será exibida do ponto

		textMelhorPontuacao = new BitmapFont();					//inicializa a fonte
		textMelhorPontuacao.setColor(Color.RED);  				//define a cor da textura
		textMelhorPontuacao.getData().setScale(2);				//define o tamanho da fonte que será exibida da pontuação final

		textReiniciar = new BitmapFont();						//inicializa a fonte
		textReiniciar.setColor(Color.GREEN);  					//define a cor da textura
		textReiniciar.getData().setScale(2);					//define o tamanho da fonte que será exibida do ponto

		shapeRenderer = new ShapeRenderer();					//inicializa ShapeRenderer
		circuloPassaro = new Circle();							//inicializa Circle
		retanguloCanoCima = new Rectangle();					//inicializa Rectangle do cano superior
		retanguloCanoBaixo = new Rectangle();					//inicializa o Rectangle do cano inferior

		somVoando = Gdx.audio.newSound(Gdx.files.internal("som_asa.wav"));		//habilita o som do passaro batendo as assas
		somColisao = Gdx.audio.newSound(Gdx.files.internal("som_batida.wav"));	//habilita o som do passaro se chocando contra o cano
		somPontuacao = Gdx.audio.newSound(Gdx.files.internal("som_pontos.wav"));	//habilita o som do passaro passando pelos canos

		preferencais = Gdx.app.getPreferences("flappybird");
		pontuacaoMaxima = preferencais.getInteger("pontuacaoMaxima",0);	//seleciona e guarda a maior pontuação di jogador para ser exibida
	}

	private void inicializarTexturas()												   //metodo para inicializar as texturas do game
	{
		passaros = new Texture[3];
		passaros [0] = new Texture("passaro1.png");
		passaros [1] = new Texture("passaro2.png");							//sprites para fazer a animação do passarinho
		passaros [2] = new Texture("passaro3.png");

		fundo = new Texture("fundo.png");			 						//Sprite do cenário
		canoBaixo = new Texture("cano_baixo_maior.png");			        //Sprite cano baixo
		canoTopo = new Texture("cano_topo_maior.png");			 			//Sprite cano cima
		gameover = new Texture("game_over.png");				 			// sprite GameOver
	}


	private void detectarColisao()
	{
		circuloPassaro.set(50 + passaros[0].getWidth() / 2,
				posicaoInicialVerticalPassaro + passaros[0].getHeight() /2, passaros[0].getWidth() /2 );	//desenha a area de colisão do passaro

		retanguloCanoBaixo.set(posicaoCanoHorizonatal,
				alturaDispositivo / 2 - canoBaixo.getHeight() - espacoEntreCanos / 2 + posicaoCanoVertical,
				canoBaixo.getWidth(), canoBaixo.getHeight());														//desenha área de colisão dos canos

		retanguloCanoCima.set(posicaoCanoHorizonatal,
				alturaDispositivo /2 + espacoEntreCanos /2 + posicaoCanoVertical,
				canoTopo.getWidth(), canoTopo.getHeight());

		boolean colisaoCanoCima = Intersector.overlaps(circuloPassaro, retanguloCanoCima);			//detecta se a colisão foi no cano de cima ou no de baixo
		boolean colisaoCanoBaixo = Intersector.overlaps(circuloPassaro, retanguloCanoBaixo);

		if(colisaoCanoBaixo || colisaoCanoCima ){
			Gdx.app.log("Log", "Bateu!");				  //mostra no log a mensagem Bateu! caso o player colida com um cano
			if(estadoJogo == 1){
				somColisao.play();									  //ativa o som de colisão
				estadoJogo = 2;										  //muda o estado do jogo para 2
			}
		}
	}
	private void validarPontos()
	{
		if(posicaoCanoHorizonatal < 50 - passaros[0].getWidth()){
			if(!passouCano){										  //verifica se o passaro passou pelo cano e se sim, adiciona pontos
				pontos++;											  //adiciona pontos ao player caso passe pelos canos
				passouCano = true;									  //verica se o player passou pelo cano
				somPontuacao.play();   								  // quando passar pelo cano faz o efeito sonoro som_pontos.wav
			}
		}
		variacao += Gdx.graphics.getDeltaTime() * 10;				  //velocidade em que o passaro bate as asas
		if(variacao > 3)
			variacao = 0;
	}
	private void verificarEstadoJogo()
	{
		boolean toqueTela = Gdx.input.justTouched();				  //cria a interação do toque na tela

		if (estadoJogo == 0)										  //se o estado do jogo for 0...
		{
			if (Gdx.input.justTouched()) {                            //Ficar sem tocar na tela faz a gravidade levar o passaro para o chão
				gravidade = -25;									  //remove a gravidade
				estadoJogo = 1;										  //muda o estado do jogo para 1
				somVoando.play();									  //ativa o som das asas do passaro
			}
		}else if (estadoJogo == 1) {								  //se o estado do jogo for...

				if (Gdx.input.justTouched()) {						  //Ficar sem tocar na tela faz a gravidade levar o passaro para o chão
					gravidade = -15;								  //Muda a altura que o passaro pula a cada toque
					somVoando.play();								  //aplica o som das asas do passaro
			}
		posicaoCanoHorizonatal-= Gdx.graphics.getDeltaTime() * 200;   //define a posição do cano
		if(posicaoCanoHorizonatal < - canoBaixo.getWidth()){		  //desenha os canos nas tela e os posiciona de acordo com a tela do dispositivo
			posicaoCanoHorizonatal = larguraDispositivo;              //ajusta a posição dos canos de acordo com a tela do aparelho
			posicaoCanoVertical = random.nextInt(400) -200;		  //randomiza a posição dos proximos canos que irão aparecer
			passouCano = false;
		}
			if(posicaoInicialVerticalPassaro > 0 || toqueTela)
				posicaoInicialVerticalPassaro = posicaoInicialVerticalPassaro - gravidade; // a cada toque na tela reduz a gravidade

			gravidade++;                                                //aciona a gravidade para fazer o passaro cair
		}else if (estadoJogo == 2){										//se o estado do jogo for 2...
			if(pontos > pontuacaoMaxima)								//verifica se a pontuação atual é maior que a anterior, e se sim, guarda a nova pontuação
			{
				pontuacaoMaxima = pontos;
				preferencais.putInteger("pontuaçãoMaxima", pontuacaoMaxima);
			}
			posicaoHorizontalPassaro -= Gdx.graphics.getDeltaTime() * 500;		//reinicia a posição do passaro ao reiniciar o game

			if(toqueTela)														//se tocar na tela...
			{
				estadoJogo = 0;													//volta o estado do jogo para 0
				pontos = 0;														//volta a pontuação atual para 0
				gravidade = 0;													//zera a gravidade
				posicaoHorizontalPassaro = 0;									//reinicia a posição inicial do passaro
				posicaoInicialVerticalPassaro = alturaDispositivo / 2;			//reinicia a posição inicial dos canos
				posicaoCanoHorizonatal = larguraDispositivo;
			}
		}
	}
	private void desenharTexturas () {

		//desenha as texturas do game e as posiciona corretamente na tela de acordo com o aparelho

		batch.begin(); //inicia o processo para desenhar as texturas
		batch.draw(fundo, 0, 0, larguraDispositivo, alturaDispositivo);
		batch.draw(passaros[(int) variacao], 50 + posicaoHorizontalPassaro,  posicaoInicialVerticalPassaro);
		batch.draw(canoBaixo, posicaoCanoHorizonatal,
				alturaDispositivo /2 - canoBaixo.getHeight() - espacoEntreCanos /2 + posicaoCanoVertical);
		batch.draw(canoTopo,posicaoCanoHorizonatal,alturaDispositivo/2 + espacoEntreCanos /2 + posicaoCanoVertical);
		textPontuacao.draw(batch, String.valueOf(pontos), larguraDispositivo / 2, alturaDispositivo -100);

		if(estadoJogo == 2){
			batch.draw(gameover, larguraDispositivo / 2 - gameover.getWidth() / 2, alturaDispositivo / 2); //Desenha e posiciona o Game Over
			textReiniciar.draw(batch,"TOQUE NA TELA PARA REINICIAR!",
					larguraDispositivo / 2 -250, alturaDispositivo / 2 - gameover.getHeight() / 2);	// Escreve e posisicona a mensagem de reinicio
			textMelhorPontuacao.draw(batch,"MELHOR PONTUAÇÃO: " + pontuacaoMaxima + " PONTOS",
					larguraDispositivo / 2 -250, alturaDispositivo / 2- gameover.getHeight() * 2);	//Escreve e posiciona a melhor pontuação
		}
		batch.end(); //Finaliza o processo para desenhar as texturas
	}

	@Override
	public void dispose ()
	{

	}
}
