package xadrez.peças;

import tabuleiroJogo.Posicao;
import tabuleiroJogo.Tabuleiro;
import xadrez.Cor;
import xadrez.PecaXadrez;

public class Rei extends PecaXadrez {

	public Rei(Tabuleiro tabuleiro, Cor cor) {
		super(tabuleiro, cor);
	}
	
	@Override
	public String toString() {
		return "K";
	}
	
	public boolean podeMover(Posicao posicao) {
		PecaXadrez p = (PecaXadrez) getTabuleiro().peca(posicao);
		return p == null || p.getCor() != getCor();
	}

	@Override
	public boolean[][] movimentosPossiveis() {
		boolean matriz[][] = new boolean[getTabuleiro().getLinhas()][getTabuleiro().getColunas()];

		Posicao p = new Posicao(0, 0);
		//verificar cima da peca
		p.setValores(posicao.getLinha() - 1, posicao.getColuna());
		if(getTabuleiro().posicaoExistente(p) && podeMover(p)) {
			matriz[p.getLinha()][p.getColuna()] = true;
		}
		//verificar baixo da peca
		p.setValores(posicao.getLinha() + 1, posicao.getColuna());
		if(getTabuleiro().posicaoExistente(p) && podeMover(p)) {
			matriz[p.getLinha()][p.getColuna()] = true;
		}
		//verificar a esquerda da peca
		p.setValores(posicao.getLinha(), posicao.getColuna() - 1);
		if(getTabuleiro().posicaoExistente(p) && podeMover(p)) {
			matriz[p.getLinha()][p.getColuna()] = true;
		}
		//verificar a direita da peca
		p.setValores(posicao.getLinha() , posicao.getColuna() + 1);
		if(getTabuleiro().posicaoExistente(p) && podeMover(p)) {
			matriz[p.getLinha()][p.getColuna()] = true;
		}
		//verificar diagonal acima esquerdo
		p.setValores(posicao.getLinha() - 1, posicao.getColuna() -1);
		if(getTabuleiro().posicaoExistente(p) && podeMover(p)) {
			matriz[p.getLinha()][p.getColuna()] = true;
		}
		//verificar diagonal abaixo esquerdo
		p.setValores(posicao.getLinha() + 1, posicao.getColuna() - 1);
		if(getTabuleiro().posicaoExistente(p) && podeMover(p)) {
			matriz[p.getLinha()][p.getColuna()] = true;
		}
		//verificar diagonal acima direito
		p.setValores(posicao.getLinha() - 1, posicao.getColuna() + 1);
		if(getTabuleiro().posicaoExistente(p) && podeMover(p)) {
			matriz[p.getLinha()][p.getColuna()] = true;
		}
		//verificar diagonal abaixo direiro
		p.setValores(posicao.getLinha() + 1, posicao.getColuna() + 1);
		if(getTabuleiro().posicaoExistente(p) && podeMover(p)) {
			matriz[p.getLinha()][p.getColuna()] = true;
		}

		return matriz;
	}
}
