package xadrez;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import tabuleiroJogo.Peca;
import tabuleiroJogo.Posicao;
import tabuleiroJogo.Tabuleiro;
import xadrez.pe�as.Rei;
import xadrez.pe�as.Torre;

public class PartidaXadrez {
	
	private int turno;
	private Cor jogadorAtual;
	private boolean check;
	private boolean checkMate;
	
	private List<Peca> pecasNoTabuleiro = new ArrayList<>();
	private List<Peca> pecasCapturada = new ArrayList<>();
	
	private Tabuleiro tabuleiro;

	public PartidaXadrez() {
		tabuleiro = new Tabuleiro(8, 8);
		turno = 1;
		jogadorAtual = Cor.WHITE;
		configInicial();
	}

	public int getTurno() {
		return turno;
	}

	public Cor getJogadorAtual() {
		return jogadorAtual;
	}
	
	public boolean isCheck() {
		return check;
	}
	
	public boolean isCheckMate() {
		return checkMate;
	}

	public PecaXadrez[][] getPecas() {
		PecaXadrez[][] matriz = new PecaXadrez[tabuleiro.getLinhas()][tabuleiro.getColunas()];
		for (int i = 0; i < tabuleiro.getLinhas(); i++) {
			for(int j = 0; j < tabuleiro.getColunas(); j++) {
				matriz[i][j] = (PecaXadrez) tabuleiro.peca(i, j);
			}
		}
		return matriz;
	}
	
	public boolean[][] movimentosPossivel(PosicaoXadrez posOrigem) {
		Posicao posicao = posOrigem.toPosicao();
		validaPosicaoOrigem(posicao);
		return tabuleiro.peca(posicao).movimentosPossiveis();
	}
	
	public PecaXadrez executarMovimXadrez(PosicaoXadrez posOrigem, PosicaoXadrez posDestino) {
		//Converter as nossas duas posicoes para posicoes da matriz
		Posicao origem = posOrigem.toPosicao();
		Posicao destino = posDestino.toPosicao();
		validaPosicaoOrigem(origem);
		validaPosicaoDestino(origem, destino);
		Peca pecaCapturada = fazMovimento(origem, destino);
		
		if(testeCheck(jogadorAtual)) {
			desfazerMovimento(origem, destino, pecaCapturada);
			throw new XadrezException("Voc� n�o pode se colocar em xeque!");
		}
		
		check = (testeCheck(jogadorOponente(jogadorAtual))) ? true : false;
		
		if(testeCheck(jogadorOponente(jogadorAtual))) {
			checkMate = true;
		} else {
			proximoTurno();
		}
		
		return (PecaXadrez) pecaCapturada;
	}

	private void validaPosicaoDestino(Posicao origem, Posicao destino) {
		if(!tabuleiro.peca(origem).movimentoPossivel(destino)) {
			throw new XadrezException("A pe�a escolhida n�o pode mover para posi��o do destino!!!");
		}
	}

	private Peca fazMovimento(Posicao origem, Posicao destino) {
		PecaXadrez p = (PecaXadrez) tabuleiro.removePeca(origem);
		p.incContMovimentos();
		Peca pecaCapturada = tabuleiro.removePeca(destino);
		tabuleiro.lugarPeca(p, destino);
		
		if(pecaCapturada != null) {
			pecasNoTabuleiro.remove(pecaCapturada);
			pecasCapturada.add(pecaCapturada);
		}
		
		return pecaCapturada;
	}
	
	private void desfazerMovimento(Posicao origem, Posicao destino, Peca pecaCapturada) {
		PecaXadrez p = (PecaXadrez) tabuleiro.removePeca(destino);
		p.decContMovimentos();
		tabuleiro.lugarPeca(p, origem);
		
		if(pecaCapturada != null) {
			tabuleiro.lugarPeca(pecaCapturada, destino);
			pecasCapturada.remove(pecaCapturada);
			pecasNoTabuleiro.add(pecaCapturada);
		}
	}

	private void validaPosicaoOrigem(Posicao posicao) {
		if(!tabuleiro.existeUmaPeca(posicao)) {
			throw new XadrezException("N�o existe nenhuma pe�a na posi��o de origem");
		}
		if(jogadorAtual != ((PecaXadrez) tabuleiro.peca(posicao)).getCor()) {
			throw new XadrezException("N�o pode mover a pe�a advers�ria!!!");
		}
		if(!tabuleiro.peca(posicao).existeUmMovimentoPossivel()) {
			throw new XadrezException("N�o existe nenhum movimento para a pe�a escolhida");
		}
	}

	private void lugarNovaPeca(char coluna, int linha, PecaXadrez peca) {
		tabuleiro.lugarPeca(peca, new PosicaoXadrez(coluna, linha).toPosicao());
		pecasNoTabuleiro.add(peca);
	}
	
	private void proximoTurno() {
		turno++;
		jogadorAtual = (jogadorAtual == Cor.WHITE) ? Cor.BLACK : Cor.WHITE;
	}
	
	private Cor jogadorOponente(Cor cor) {
		return (cor == Cor.WHITE) ? Cor.BLACK : Cor.WHITE;
	}
	
	private PecaXadrez king(Cor cor) {
		List<Peca> lista = pecasNoTabuleiro.stream().filter(x -> ((PecaXadrez) x).getCor() == cor).collect(Collectors.toList());
		for (Peca peca : lista) {
			if(peca instanceof Rei) {
				return (PecaXadrez)peca;
			}
		}
		throw new IllegalStateException("N�o existe o rei de cor " + cor + " no tabuleiro.");
	}
	
	private boolean testeCheck(Cor cor) {
		Posicao posicaoRei = king(cor).getPosicaoXadrez().toPosicao();
		List<Peca> pecasOponentes = pecasNoTabuleiro.stream().filter(x -> ((PecaXadrez) x).getCor() == jogadorOponente(cor)).collect(Collectors.toList());
		for (Peca peca : pecasOponentes) {
			boolean matriz[][] = peca.movimentosPossiveis();
			if(matriz[posicaoRei.getLinha()][posicaoRei.getColuna()]) {
				return true;
			}
		}
		return false;
	}
	private boolean testeCheckMate(Cor cor) {
		if(!testeCheck(cor)) {
			return false;
		}
		List<Peca> lista = pecasNoTabuleiro.stream().filter(x -> ((PecaXadrez) x).getCor() == cor).collect(Collectors.toList());
		for (Peca peca : lista) {
			boolean matriz[][] = peca.movimentosPossiveis();
			for (int i = 0; i < tabuleiro.getLinhas(); i++) {
				for (int j = 0; j < tabuleiro.getColunas(); j++) {
					if(matriz[i][j]) {
						Posicao origem = ((PecaXadrez)peca).getPosicaoXadrez().toPosicao();
						Posicao destino = new Posicao(i, j);
						Peca pecaCapturada = fazMovimento(origem, destino);
						boolean testeCheck = testeCheck(cor);
						desfazerMovimento(origem, destino, pecaCapturada);
						if(!testeCheck) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}

	private void configInicial() {
		/*
		 * lugarNovaPeca('c', 1, new Torre(tabuleiro, Cor.WHITE)); lugarNovaPeca('c', 2,
		 * new Torre(tabuleiro, Cor.WHITE)); lugarNovaPeca('d', 2, new Torre(tabuleiro,
		 * Cor.WHITE)); lugarNovaPeca('e', 2, new Torre(tabuleiro, Cor.WHITE));
		 * lugarNovaPeca('e', 1, new Torre(tabuleiro, Cor.WHITE)); lugarNovaPeca('d', 1,
		 * new Rei(tabuleiro, Cor.WHITE));
		 * 
		 * lugarNovaPeca('c', 7, new Torre(tabuleiro, Cor.BLACK)); lugarNovaPeca('c', 8,
		 * new Torre(tabuleiro, Cor.BLACK)); lugarNovaPeca('d', 7, new Torre(tabuleiro,
		 * Cor.BLACK)); lugarNovaPeca('e', 7, new Torre(tabuleiro, Cor.BLACK));
		 * lugarNovaPeca('e', 8, new Torre(tabuleiro, Cor.BLACK)); lugarNovaPeca('d', 8,
		 * new Rei(tabuleiro, Cor.BLACK));
		 */
		lugarNovaPeca('h', 7, new Torre(tabuleiro, Cor.WHITE));
		lugarNovaPeca('d', 1, new Torre(tabuleiro, Cor.WHITE));
		lugarNovaPeca('e', 1, new Rei(tabuleiro, Cor.WHITE));
		
		lugarNovaPeca('b', 8, new Torre(tabuleiro, Cor.BLACK));
        lugarNovaPeca('a', 8, new Rei(tabuleiro, Cor.BLACK));
	}
}
