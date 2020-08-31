package xadrez.peças;

import tabuleiroJogo.Posicao;
import tabuleiroJogo.Tabuleiro;
import xadrez.Cor;
import xadrez.PecaXadrez;

public class Torre extends PecaXadrez {

	public Torre(Tabuleiro tabuleiro, Cor cor) {
		super(tabuleiro, cor);
	}

	@Override
	public String toString() {
		return "R";
	}

	@Override
	public boolean[][] movimentosPossiveis() {
		boolean matriz[][] = new boolean[getTabuleiro().getLinhas()][getTabuleiro().getColunas()];

		Posicao p = new Posicao(0, 0);

		//verificar cima da peca
		p.setValores(posicao.getLinha() - 1, posicao.getColuna());
		while(getTabuleiro().posicaoExistente(p) && !getTabuleiro().existeUmaPeca(p)) {
			matriz[p.getLinha()][p.getColuna()] = true;
			p.setLinha(p.getLinha() - 1);
		}
		if(getTabuleiro().posicaoExistente(p) && existePecaAdversaria(p)) {
			matriz[p.getLinha()][p.getColuna()] = true;
		}
		//verificar para esquerda
		p.setValores(posicao.getLinha(), posicao.getColuna() - 1);
		while(getTabuleiro().posicaoExistente(p) && !getTabuleiro().existeUmaPeca(p)) {
			matriz[p.getLinha()][p.getColuna()] = true;
			p.setColuna(p.getColuna() - 1);
		}
		if(getTabuleiro().posicaoExistente(p) && existePecaAdversaria(p)) {
			matriz[p.getLinha()][p.getColuna()] = true;
		}
		//verificar para direita
		p.setValores(posicao.getLinha(), posicao.getColuna() + 1);
		while(getTabuleiro().posicaoExistente(p) && !getTabuleiro().existeUmaPeca(p)) {
			matriz[p.getLinha()][p.getColuna()] = true;
			p.setColuna(p.getColuna() + 1);
		}
		if(getTabuleiro().posicaoExistente(p) && existePecaAdversaria(p)) {
			matriz[p.getLinha()][p.getColuna()] = true;
		}
		//verificar baixo da peca
		p.setValores(posicao.getLinha() + 1, posicao.getColuna());
		while(getTabuleiro().posicaoExistente(p) && !getTabuleiro().existeUmaPeca(p)) {
			matriz[p.getLinha()][p.getColuna()] = true;
			p.setLinha(p.getLinha() + 1);
		}
		if(getTabuleiro().posicaoExistente(p) && existePecaAdversaria(p)) {
			matriz[p.getLinha()][p.getColuna()] = true;
		}

		return matriz;
	}
}
