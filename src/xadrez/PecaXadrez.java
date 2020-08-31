package xadrez;

import tabuleiroJogo.Peca;
import tabuleiroJogo.Posicao;
import tabuleiroJogo.Tabuleiro;

public abstract class PecaXadrez extends Peca {
	private Cor cor;

	public PecaXadrez(Tabuleiro tabuleiro, Cor cor) {
		super(tabuleiro);
		this.cor = cor;
	}

	public Cor getCor() {
		return cor;
	}
	
	protected boolean existePecaAdversaria(Posicao posicao) {
		PecaXadrez peca =  (PecaXadrez) getTabuleiro().peca(posicao);
		return peca != null && peca.getCor() != cor;
	}
	
}
