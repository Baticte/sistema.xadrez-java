package xadrez.peças;

import tabuleiroJogo.Posicao;
import tabuleiroJogo.Tabuleiro;
import xadrez.Cor;
import xadrez.PecaXadrez;

public class Bispo extends PecaXadrez {

	public Bispo(Tabuleiro tabuleiro, Cor cor) {
		super(tabuleiro, cor);
	}

	@Override
	public boolean[][] movimentosPossiveis() {
		boolean matriz[][] = new boolean[getTabuleiro().getLinhas()][getTabuleiro().getColunas()];
		Posicao p = new Posicao(0, 0);

		//verificar diagonal acima esquerdo da peca
		p.setValores(posicao.getLinha() - 1, posicao.getColuna() -1);
		while(getTabuleiro().posicaoExistente(p) && !getTabuleiro().existeUmaPeca(p)) {
			matriz[p.getLinha()][p.getColuna()] = true;
			p.setValores(p.getLinha() - 1, p.getColuna() - 1);
		}
		if(getTabuleiro().posicaoExistente(p) && existePecaAdversaria(p)) {
			matriz[p.getLinha()][p.getColuna()] = true;
		}
		//verificar para cima direito da peca
		p.setValores(posicao.getLinha() - 1, posicao.getColuna() + 1);
		while(getTabuleiro().posicaoExistente(p) && !getTabuleiro().existeUmaPeca(p)) {
			matriz[p.getLinha()][p.getColuna()] = true;
			p.setValores(p.getLinha() - 1, p.getColuna() + 1);;
		}
		if(getTabuleiro().posicaoExistente(p) && existePecaAdversaria(p)) {
			matriz[p.getLinha()][p.getColuna()] = true;
		}
		//verificar diagonal abaixo esquerdo
		p.setValores(posicao.getLinha() + 1, posicao.getColuna() - 1);
		while(getTabuleiro().posicaoExistente(p) && !getTabuleiro().existeUmaPeca(p)) {
			matriz[p.getLinha()][p.getColuna()] = true;
			p.setValores(p.getLinha() + 1, p.getColuna() - 1);;
		}
		if(getTabuleiro().posicaoExistente(p) && existePecaAdversaria(p)) {
			matriz[p.getLinha()][p.getColuna()] = true;
		}
		//verificar diagonal abaixo direito
		p.setValores(posicao.getLinha() + 1, posicao.getColuna() + 1);
		while(getTabuleiro().posicaoExistente(p) && !getTabuleiro().existeUmaPeca(p)) {
			matriz[p.getLinha()][p.getColuna()] = true;
			p.setValores(p.getLinha() + 1, p.getColuna() + 1);;
		}
		if(getTabuleiro().posicaoExistente(p) && existePecaAdversaria(p)) {
			matriz[p.getLinha()][p.getColuna()] = true;
		}

		return matriz;
	}
	
	@Override
	public String toString() {
		return "B";
	}

}
