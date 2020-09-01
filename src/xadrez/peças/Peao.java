package xadrez.peças;

import tabuleiroJogo.Posicao;
import tabuleiroJogo.Tabuleiro;
import xadrez.Cor;
import xadrez.PecaXadrez;

public class Peao extends PecaXadrez {

	public Peao(Tabuleiro tabuleiro, Cor cor) {
		super(tabuleiro, cor);
	}

	@Override
	public boolean[][] movimentosPossiveis() {
		boolean matriz[][] = new boolean[getTabuleiro().getLinhas()][getTabuleiro().getColunas()];

		Posicao p = new Posicao(0, 0);
		if(getCor() == Cor.WHITE) {
			p.setValores(posicao.getLinha() - 1, posicao.getColuna());
			if(getTabuleiro().posicaoExistente(p) && !getTabuleiro().existeUmaPeca(p) ) {
				matriz[p.getLinha()][p.getColuna()] = true;

			}
			p.setValores(posicao.getLinha() - 2, posicao.getColuna());
			Posicao pos = new Posicao(posicao.getLinha() - 1, posicao.getColuna());
			if(getTabuleiro().posicaoExistente(p) && !getTabuleiro().existeUmaPeca(p) && getTabuleiro().posicaoExistente(pos)
					&& !getTabuleiro().existeUmaPeca(pos) && getContMovimentos() == 0 ) {
				matriz[p.getLinha()][p.getColuna()] = true;

			}
			p.setValores(posicao.getLinha() - 1, posicao.getColuna() - 1);
			if(getTabuleiro().posicaoExistente(p) && existePecaAdversaria(p)) {
				matriz[p.getLinha()][p.getColuna()] = true;

			}
			p.setValores(posicao.getLinha() - 1, posicao.getColuna() + 1);
			if(getTabuleiro().posicaoExistente(p) && existePecaAdversaria(p) ) {
				matriz[p.getLinha()][p.getColuna()] = true;

			}	
		} else {
			p.setValores(posicao.getLinha() + 1, posicao.getColuna());
			if(getTabuleiro().posicaoExistente(p) && !getTabuleiro().existeUmaPeca(p) ) {
				matriz[p.getLinha()][p.getColuna()] = true;

			}
			p.setValores(posicao.getLinha() + 2, posicao.getColuna());
			Posicao pos = new Posicao(posicao.getLinha() + 1, posicao.getColuna());
			if(getTabuleiro().posicaoExistente(p) && !getTabuleiro().existeUmaPeca(p) && getTabuleiro().posicaoExistente(pos)
					&& !getTabuleiro().existeUmaPeca(pos) && getContMovimentos() == 0 ) {
				matriz[p.getLinha()][p.getColuna()] = true;

			}
			p.setValores(posicao.getLinha() + 1, posicao.getColuna() - 1);
			if(getTabuleiro().posicaoExistente(p) && existePecaAdversaria(p)) {
				matriz[p.getLinha()][p.getColuna()] = true;

			}
			p.setValores(posicao.getLinha() + 1, posicao.getColuna() + 1);
			if(getTabuleiro().posicaoExistente(p) && existePecaAdversaria(p) ) {
				matriz[p.getLinha()][p.getColuna()] = true;
			}	
		}

		return matriz;
	}

	@Override
	public String toString() {
		return "P";
	}
}
