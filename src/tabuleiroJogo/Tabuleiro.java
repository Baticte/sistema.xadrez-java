package tabuleiroJogo;

public class Tabuleiro {
	
	private int linhas;
	private int colunas;
	private Peca pecas[][];
	
	public Tabuleiro(int linhas, int colunas) {
		if(linhas < 1 || colunas < 1) {
			throw new TabuleiroException("Erro na criação do tabuleiro, é necessário que haja pelo menos uma linha ou coluna");
		}
		this.linhas = linhas;
		this.colunas = colunas;
		pecas = new Peca[linhas][colunas];
	}

	public int getLinhas() {
		return linhas;
	}

	public int getColunas() {
		return colunas;
	}
	
	public Peca peca(int linha, int coluna) {
		if(!posicaoExistente(linha, coluna)) {
			throw new TabuleiroException("Posição inexistente!");
		}
		return pecas[linha][coluna];
	}
	
	public Peca peca(Posicao posicao) {
		if(!posicaoExistente(posicao)) {
			throw new TabuleiroException("Posição inexistente!");
		}
		return pecas[posicao.getLinha()][posicao.getColuna()];
	}
	
	public void lugarPeca(Peca peca, Posicao pos) {
		if(existeUmaPeca(pos)) {
			throw new TabuleiroException("Já existe uma peça na posição " + pos);
		}
		pecas[pos.getLinha()][pos.getColuna()] = peca;
		peca.posicao = pos;
	}
	
	private boolean posicaoExistente(int linha, int coluna) {
		/* condicao completa para ver se uma posica existe */
		return linha >=0 && linha < linhas && coluna >= 0 && coluna < colunas;
	}
	
	public boolean posicaoExistente(Posicao posicao) {
		return posicaoExistente(posicao.getLinha(), posicao.getColuna());
	}
	
	public boolean existeUmaPeca(Posicao posicao) {
		if(!posicaoExistente(posicao)) {
			throw new TabuleiroException("Posição inexistente!");
		}
		return peca(posicao) != null;
	}
	
	public Peca removePeca(Posicao posicao) {
		if(!posicaoExistente(posicao)) {
			throw new TabuleiroException("Posição inexistente no tabuleiro!");
		}
		if(peca(posicao) == null) {
			return null;
		}
		Peca aux = peca(posicao);
		aux.posicao = null;
		pecas[posicao.getLinha()][posicao.getColuna()] = null;
		return aux;
	}
}
