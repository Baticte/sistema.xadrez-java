package xadrez;

import tabuleiroJogo.Posicao;

public class PosicaoXadrez {
	private char coluna;
	private int linha;

	public PosicaoXadrez(char coluna, int linha) {
		if(coluna < 'a' || coluna > 'h' || linha < 1 || linha > 8) {
			throw new XadrezException("Erro de instacia��o da posi��o, valores v�lidos s�o de a1 � h8");
		}
		this.coluna = coluna;
		this.linha = linha;
	}

	public char getColuna() {
		return coluna;
	}

	public int getLinha() {
		return linha;
	}
	
	protected Posicao toPosicao() {
		return new Posicao(8 - linha, coluna - 'a');
	}
	
	protected static Posicao dePosicao(Posicao posicao) {
		return new Posicao((char) ('a' - posicao.getColuna()), 8 - posicao.getLinha());
	}
	
	@Override
	public String toString() {
		return "" + coluna + linha;
	}
}
