package xadrez;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import tabuleiroJogo.Peca;
import tabuleiroJogo.Posicao;
import tabuleiroJogo.Tabuleiro;
import xadrez.peças.Bispo;
import xadrez.peças.Cavalo;
import xadrez.peças.Peao;
import xadrez.peças.Rainha;
import xadrez.peças.Rei;
import xadrez.peças.Torre;

public class PartidaXadrez {

	private int turno;
	private Cor jogadorAtual;
	private boolean check;
	private boolean checkMate;
	private PecaXadrez enPassantVulneravel;

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
			throw new XadrezException("Você não pode se colocar em xeque!");
		}
		
		PecaXadrez pecaMovida = (PecaXadrez) tabuleiro.peca(destino);

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

	private void validaPosicaoDestino(Posicao origem, Posicao destino) {
		if(!tabuleiro.peca(origem).movimentoPossivel(destino)) {
			throw new XadrezException("A peça escolhida não pode mover para posição do destino!!!");
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
	}

	private void validaPosicaoOrigem(Posicao posicao) {
		if(!tabuleiro.existeUmaPeca(posicao)) {
			throw new XadrezException("Não existe nenhuma peça na posição de origem");
		}
		if(jogadorAtual != ((PecaXadrez) tabuleiro.peca(posicao)).getCor()) {
			throw new XadrezException("Não pode mover a peça adversária!!!");
		}
		if(!tabuleiro.peca(posicao).existeUmMovimentoPossivel()) {
			throw new XadrezException("Não existe nenhum movimento para a peça escolhida");
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
		throw new IllegalStateException("Não existe o rei de cor " + cor + " no tabuleiro.");
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
