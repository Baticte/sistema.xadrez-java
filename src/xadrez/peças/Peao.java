package xadrez.peças;

import tabuleiroJogo.Posicao;
import tabuleiroJogo.Tabuleiro;
import xadrez.Cor;
import xadrez.PartidaXadrez;
import xadrez.PecaXadrez;

public class Peao extends PecaXadrez {

	private PartidaXadrez partidaXadrez;

	public Peao(Tabuleiro tabuleiro, Cor cor, PartidaXadrez partidaXadrez) {
		super(tabuleiro, cor);
		this.partidaXadrez = partidaXadrez;
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

			//SpecialMove En Passant para pecas brancas
			if(posicao.getLinha() == 3) {
				Posicao esquerda = new Posicao(posicao.getLinha(), posicao.getColuna() - 1);
				if(getTabuleiro().posicaoExistente(esquerda) && existePecaAdversaria(esquerda) && getTabuleiro().peca(esquerda) == partidaXadrez.getEnPassanVulneravel()) {
					matriz[esquerda.getLinha() - 1][esquerda.getColuna()] = true;
				}
				Posicao direita = new Posicao(posicao.getLinha(), posicao.getColuna() + 1);
				if(getTabuleiro().posicaoExistente(direita) && existePecaAdversaria(direita) && getTabuleiro().peca(direita) == partidaXadrez.getEnPassanVulneravel()) {
					matriz[direita.getLinha() - 1][direita.getColuna()] = true;
				}			
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

			//SpecialMove En Passant para pecas brancas
			if(posicao.getLinha() == 4) {
				Posicao esquerda = new Posicao(posicao.getLinha(), posicao.getColuna() - 1);
				if(getTabuleiro().posicaoExistente(esquerda) && existePecaAdversaria(esquerda) && getTabuleiro().peca(esquerda) == partidaXadrez.getEnPassanVulneravel()) {
					matriz[esquerda.getLinha() + 1][esquerda.getColuna()] = true;
				}
				Posicao direita = new Posicao(posicao.getLinha(), posicao.getColuna() + 1);
				if(getTabuleiro().posicaoExistente(direita) && existePecaAdversaria(direita) && getTabuleiro().peca(direita) == partidaXadrez.getEnPassanVulneravel()) {
					matriz[direita.getLinha() + 1][direita.getColuna()] = true;
				}			
			}
		}

		return matriz;
	}

	@Override
	public String toString() {
		return "P";
	}
}
