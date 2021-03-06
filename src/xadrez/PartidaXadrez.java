package xadrez;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import tabuleiroJogo.Peca;
import tabuleiroJogo.Posicao;
import tabuleiroJogo.Tabuleiro;
import xadrez.pe�as.Bispo;
import xadrez.pe�as.Cavalo;
import xadrez.pe�as.Peao;
import xadrez.pe�as.Rainha;
import xadrez.pe�as.Rei;
import xadrez.pe�as.Torre;

public class PartidaXadrez {

	private int turno;
	private Cor jogadorAtual;
	private boolean check;
	private boolean checkMate;
	private PecaXadrez enPassantVulneravel;
	private PecaXadrez promovida;

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

	public PecaXadrez getEnPassanVulneravel() {
		return enPassantVulneravel;
	}

	public PecaXadrez getPromocao() {
		return promovida;
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

		PecaXadrez pecaMovida = (PecaXadrez) tabuleiro.peca(destino);
		
		//SpecialMove promotion
		promovida = null;
		if(pecaMovida instanceof Peao) {
			if(pecaMovida.getCor() == Cor.WHITE && destino.getLinha() == 0 || pecaMovida.getCor() == Cor.BLACK && destino.getLinha() == 7) {
				promovida = (PecaXadrez) tabuleiro.peca(destino);
				promovida = substituirParaPecaPromocao("Q");
			}
		}

		check = (testeCheck(jogadorOponente(jogadorAtual))) ? true : false;

		if(testCheckMate(jogadorOponente(jogadorAtual))) {
			checkMate = true;
		} else {
			proximoTurno();
		}

		//SpecialMove En Passant
		if(pecaMovida instanceof Peao && destino.getLinha() == origem.getLinha() - 2 || destino.getLinha() == origem.getLinha() + 2) {
			enPassantVulneravel = pecaMovida;
		} else {
			enPassantVulneravel = null;
		}

		return (PecaXadrez) pecaCapturada;
	}

	public PecaXadrez substituirParaPecaPromocao(String string) {
		if(promovida == null) {
			throw new IllegalStateException("N�o h� pe�a para ser promovida.");
		}
		if(!string.equals("B") && !string.equals("N") && !string.equals("R") && !string.equals("Q")) {
			return promovida;
		}
		
		Posicao pos = promovida.getPosicaoXadrez().toPosicao();
		Peca p = tabuleiro.removePeca(pos);
		pecasNoTabuleiro.remove(p);
		
		PecaXadrez novaPeca = novaPeca(string, promovida.getCor());
		tabuleiro.lugarPeca(novaPeca, pos);
		pecasNoTabuleiro.add(novaPeca);
		
		return novaPeca;
	}
	
	/**
	 * @param m�todo auxiliar 
	 * @param que vai retornar
	 * @return uma pe�a de xadrez
	 */
	private PecaXadrez novaPeca(String string, Cor cor) {
		if(string.equals("B")) return new Bispo(tabuleiro, cor);
		if(string.equals("N")) return new Cavalo(tabuleiro, cor);
		if(string.equals("Q")) return new Rainha(tabuleiro, cor);
		return new Torre(tabuleiro, cor);
		
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
		//SpecialMove castling KingSide Rook
		if(p instanceof Rei && destino.getColuna() == origem.getColuna() + 2) {
			Posicao origemTorre = new Posicao(origem.getLinha(), origem.getColuna() + 3);
			Posicao destinoTorre = new Posicao(origem.getLinha(), origem.getColuna() + 1);
			PecaXadrez torre = (PecaXadrez) tabuleiro.removePeca(origemTorre);
			tabuleiro.lugarPeca(torre, destinoTorre);
			torre.incContMovimentos();
		}
		//SpecialMove castling QueenSide Rook
		if(p instanceof Rei && destino.getColuna() == origem.getColuna() - 2) {
			Posicao origemTorre = new Posicao(origem.getLinha(), origem.getColuna() - 4);
			Posicao destinoTorre = new Posicao(origem.getLinha(), origem.getColuna() - 1);
			PecaXadrez torre = (PecaXadrez) tabuleiro.removePeca(origemTorre);
			tabuleiro.lugarPeca(torre, destinoTorre);
			torre.incContMovimentos();
		}

		//SpecialMove en passant 
		if(p instanceof Peao) {
			if(origem.getColuna() != destino.getColuna() && pecaCapturada == null) {
				Posicao posicaoPeao;
				if(p.getCor() == Cor.WHITE) {
					posicaoPeao = new Posicao(destino.getLinha() + 1, destino.getColuna());
				} else {
					posicaoPeao = new Posicao(destino.getLinha() - 1, destino.getColuna());
				}
				pecaCapturada = tabuleiro.removePeca(posicaoPeao);
				pecasCapturada.add(pecaCapturada);
				pecasNoTabuleiro.remove(pecaCapturada);
			}
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
		//SpecialMove castling KingSide Rook
		if(p instanceof Rei && destino.getColuna() == origem.getColuna() + 2) {
			Posicao origemTorre = new Posicao(origem.getLinha(), destino.getColuna() + 3);
			Posicao destinoTorre = new Posicao(origem.getLinha(), destino.getColuna() + 1);
			PecaXadrez torre = (PecaXadrez) tabuleiro.removePeca(destinoTorre);
			tabuleiro.lugarPeca(torre, origemTorre);
			torre.decContMovimentos();
		}
		//SpecialMove castling QueenSide Rook
		if(p instanceof Rei && destino.getColuna() == origem.getColuna() - 2) {
			Posicao origemTorre = new Posicao(origem.getLinha(), destino.getColuna() - 4);
			Posicao destinoTorre = new Posicao(origem.getLinha(), destino.getColuna() - 1);
			PecaXadrez torre = (PecaXadrez) tabuleiro.removePeca(destinoTorre);
			tabuleiro.lugarPeca(torre, origemTorre);
			torre.decContMovimentos();
		}

		//SpecialMove en passant 
		if(p instanceof Peao) {
			if(origem.getColuna() != destino.getColuna() && pecaCapturada == enPassantVulneravel) {
				PecaXadrez peao = (PecaXadrez) tabuleiro.removePeca(destino);
				Posicao posicaoPeao;
				if(p.getCor() == Cor.WHITE) {
					posicaoPeao = new Posicao(3, destino.getColuna());
				} else {
					posicaoPeao = new Posicao(4, destino.getColuna());
				}
				tabuleiro.lugarPeca(peao, posicaoPeao);
			}
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

	private PecaXadrez rei(Cor cor) {
		List<Peca> lista = pecasNoTabuleiro.stream().filter(x -> ((PecaXadrez) x).getCor() == cor).collect(Collectors.toList());
		for (Peca peca : lista) {
			if(peca instanceof Rei) {
				return (PecaXadrez)peca;
			}
		}
		throw new IllegalStateException("N�o existe o rei de cor " + cor + " no tabuleiro.");
	}

	private boolean testeCheck(Cor cor) {
		Posicao posicaoRei = rei(cor).getPosicaoXadrez().toPosicao();
		List<Peca> pecasOponentes = pecasNoTabuleiro.stream().filter(x -> ((PecaXadrez) x).getCor() == jogadorOponente(cor)).collect(Collectors.toList());
		for (Peca peca : pecasOponentes) {
			boolean matriz[][] = peca.movimentosPossiveis();
			if(matriz[posicaoRei.getLinha()][posicaoRei.getColuna()]) {
				return true;
			}
		}
		return false;
	}

	private boolean testCheckMate(Cor cor) {
		if (!testeCheck(cor)) {
			return false;
		}
		List<Peca> list = pecasNoTabuleiro.stream().filter(x -> ((PecaXadrez)x).getCor() == cor).collect(Collectors.toList());
		for (Peca p : list) {
			boolean[][] mat = p.movimentosPossiveis();
			for (int i = 0; i < tabuleiro.getLinhas(); i++) {
				for (int j = 0; j < tabuleiro.getColunas(); j++) {
					if (mat[i][j]) {
						Posicao origem = ((PecaXadrez)p).getPosicaoXadrez().toPosicao();
						Posicao destino = new Posicao(i, j);
						Peca pecaCapturada = fazMovimento(origem, destino);
						boolean testeCheck = testeCheck(cor);
						desfazerMovimento(origem, destino, pecaCapturada);
						if (!testeCheck) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}

	private void configInicial() {
		lugarNovaPeca('a', 1, new Torre(tabuleiro, Cor.WHITE));
		lugarNovaPeca('h', 1, new Torre(tabuleiro, Cor.WHITE));
		lugarNovaPeca('b', 1, new Cavalo(tabuleiro, Cor.WHITE));
		lugarNovaPeca('g', 1, new Cavalo(tabuleiro, Cor.WHITE));
		lugarNovaPeca('c', 1, new Bispo(tabuleiro, Cor.WHITE));
		lugarNovaPeca('f', 1, new Bispo(tabuleiro, Cor.WHITE));
		lugarNovaPeca('e', 1, new Rei(tabuleiro, Cor.WHITE, this));
		lugarNovaPeca('d', 1, new Rainha(tabuleiro, Cor.WHITE));
		lugarNovaPeca('a', 2, new Peao(tabuleiro, Cor.WHITE, this));
		lugarNovaPeca('b', 2, new Peao(tabuleiro, Cor.WHITE, this));
		lugarNovaPeca('c', 2, new Peao(tabuleiro, Cor.WHITE, this));
		lugarNovaPeca('d', 2, new Peao(tabuleiro, Cor.WHITE, this));
		lugarNovaPeca('e', 2, new Peao(tabuleiro, Cor.WHITE, this));
		lugarNovaPeca('f', 2, new Peao(tabuleiro, Cor.WHITE, this));
		lugarNovaPeca('g', 2, new Peao(tabuleiro, Cor.WHITE, this));
		lugarNovaPeca('h', 2, new Peao(tabuleiro, Cor.WHITE, this));

		lugarNovaPeca('a', 8, new Torre(tabuleiro, Cor.BLACK));
		lugarNovaPeca('h', 8, new Torre(tabuleiro, Cor.BLACK));
		lugarNovaPeca('b', 8, new Cavalo(tabuleiro, Cor.BLACK));
		lugarNovaPeca('g', 8, new Cavalo(tabuleiro, Cor.BLACK));
		lugarNovaPeca('c', 8, new Bispo(tabuleiro, Cor.BLACK));
		lugarNovaPeca('f', 8, new Bispo(tabuleiro, Cor.BLACK));
		lugarNovaPeca('e', 8, new Rei(tabuleiro, Cor.BLACK, this));
		lugarNovaPeca('d', 8, new Rainha(tabuleiro, Cor.BLACK));
		lugarNovaPeca('a', 7, new Peao(tabuleiro, Cor.BLACK, this));
		lugarNovaPeca('b', 7, new Peao(tabuleiro, Cor.BLACK,this));
		lugarNovaPeca('c', 7, new Peao(tabuleiro, Cor.BLACK, this));
		lugarNovaPeca('d', 7, new Peao(tabuleiro, Cor.BLACK, this));
		lugarNovaPeca('e', 7, new Peao(tabuleiro, Cor.BLACK, this));
		lugarNovaPeca('f', 7, new Peao(tabuleiro, Cor.BLACK, this));
		lugarNovaPeca('g', 7, new Peao(tabuleiro, Cor.BLACK, this));
		lugarNovaPeca('h', 7, new Peao(tabuleiro, Cor.BLACK, this));
	}
}
