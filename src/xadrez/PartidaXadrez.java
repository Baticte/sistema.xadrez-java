package xadrez;

import tabuleiroJogo.Peca;
import tabuleiroJogo.Posicao;
import tabuleiroJogo.Tabuleiro;
import xadrez.pe�as.Rei;
import xadrez.pe�as.Torre;

public class PartidaXadrez {

	private Tabuleiro tabuleiro;

	public PartidaXadrez() {
		tabuleiro = new Tabuleiro(8, 8);
		configInicial();
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
	
	public PecaXadrez executarMovimXadrez(PosicaoXadrez posOrigem, PosicaoXadrez posDestino) {
		//Converter as nossas duas posicoes para posicoes da matriz
		Posicao origem = posOrigem.toPosicao();
		Posicao destino = posDestino.toPosicao();
		validaPosicaoOrigem(origem);
		Peca pecaCapturada = fazMovimento(origem, destino);
		return (PecaXadrez) pecaCapturada;
	}

	private Peca fazMovimento(Posicao origem, Posicao destino) {
		Peca p = tabuleiro.removePeca(origem);
		Peca pecaCapturada = tabuleiro.removePeca(destino);
		tabuleiro.lugarPeca(p, destino);
		return pecaCapturada;
	}

	private void validaPosicaoOrigem(Posicao posicao) {
		if(!tabuleiro.existeUmaPeca(posicao)) {
			throw new XadrezException("N�o existe nenhuma pe�a na posi��o de origem");
		}
		if(!tabuleiro.peca(posicao).existeUmMovimentoPossivel()) {
			throw new XadrezException("N�o existe nenhum movimento para a pe�a escolhida");
		}
	}

	private void lugarNovaPeca(char coluna, int linha, PecaXadrez peca) {
		tabuleiro.lugarPeca(peca, new PosicaoXadrez(coluna, linha).toPosicao());
	}

	private void configInicial() {
		lugarNovaPeca('c', 1, new Torre(tabuleiro, Cor.WHITE));
		lugarNovaPeca('c', 2, new Torre(tabuleiro, Cor.WHITE));
		lugarNovaPeca('d', 2, new Torre(tabuleiro, Cor.WHITE));
		lugarNovaPeca('e', 2, new Torre(tabuleiro, Cor.WHITE));
		lugarNovaPeca('e', 1, new Torre(tabuleiro, Cor.WHITE));
		lugarNovaPeca('d', 1, new Rei(tabuleiro, Cor.WHITE));

		lugarNovaPeca('c', 7, new Torre(tabuleiro, Cor.BLACK));
		lugarNovaPeca('c', 8, new Torre(tabuleiro, Cor.BLACK));
		lugarNovaPeca('d', 7, new Torre(tabuleiro, Cor.BLACK));
		lugarNovaPeca('e', 7, new Torre(tabuleiro, Cor.BLACK));
		lugarNovaPeca('e', 8, new Torre(tabuleiro, Cor.BLACK));
		lugarNovaPeca('d', 8, new Torre(tabuleiro, Cor.BLACK));
	}
}
