package xadrez.peças;

import tabuleiroJogo.Posicao;
import tabuleiroJogo.Tabuleiro;
import xadrez.Cor;
import xadrez.PartidaXadrez;
import xadrez.PecaXadrez;

public class Rei extends PecaXadrez {
	
	private PartidaXadrez partidaXadrez;

	public Rei(Tabuleiro tabuleiro, Cor cor, PartidaXadrez partidaXadrez) {
		super(tabuleiro, cor);
		this.partidaXadrez = partidaXadrez;
	}

	public boolean podeMover(Posicao posicao) {
		PecaXadrez p = (PecaXadrez) getTabuleiro().peca(posicao);
		return p == null || p.getCor() != getCor();
	}
	
	/**
	 * Metodo auxiliar da jogada
	 * especial do rei e torre
	 * que se chama jogada Rook
	 */
	private boolean testeTorreCastling(Posicao posicao) {
		PecaXadrez p = (PecaXadrez) getTabuleiro().peca(posicao);
		return p != null && p instanceof Torre && p.getCor() == getCor() && p.getContMovimentos() == 0;
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
		//SpecialMove Castling
		if(getContMovimentos() == 0 && !partidaXadrez.isCheck()) {
			//SpecialMove Castling KingSide Rook
			Posicao posTorre1 = new Posicao(posicao.getLinha(), posicao.getColuna() + 3);
			if(testeTorreCastling(posTorre1)) {
				Posicao p1 = new Posicao(posicao.getLinha(), posicao.getColuna() + 1);
				Posicao p2 = new Posicao(posicao.getLinha(), posicao.getColuna() + 2);
				if(getTabuleiro().peca(p1) == null && getTabuleiro().peca(p2) == null) {
					matriz[posicao.getLinha()][posicao.getColuna() + 2] = true;
				}
			}
			//SpecialMove Castling QueenSide Rook
			Posicao posTorre2 = new Posicao(posicao.getLinha(), posicao.getColuna() - 4);
			if(testeTorreCastling(posTorre2)) {
				Posicao p1 = new Posicao(posicao.getLinha(), posicao.getColuna() - 1);
				Posicao p2 = new Posicao(posicao.getLinha(), posicao.getColuna() - 2);
				Posicao p3 = new Posicao(posicao.getLinha(), posicao.getColuna() - 3);
				if(getTabuleiro().peca(p1) == null && getTabuleiro().peca(p2) == null && getTabuleiro().peca(p3) == null) {
					matriz[posicao.getLinha()][posicao.getColuna() - 2] = true;
				}
			}
		}
		

		return matriz;
	}

	@Override
	public String toString() {
		return "K";
	}
}
