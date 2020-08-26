package xadrez;

import tabuleiroJogo.Posicao;
import tabuleiroJogo.Tabuleiro;
import xadrez.peças.Rei;
import xadrez.peças.Torre;

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
	private void configInicial() {
		tabuleiro.lugarPeca(new Torre(tabuleiro, Cor.WHITE), new Posicao(7, 7));
		tabuleiro.lugarPeca(new Torre(tabuleiro, Cor.WHITE), new Posicao(7, 0));
		tabuleiro.lugarPeca(new Rei(tabuleiro, Cor.WHITE), new Posicao(7, 4));
	}
}
