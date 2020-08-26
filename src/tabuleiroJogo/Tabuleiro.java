package tabuleiroJogo;

public class Tabuleiro {
	
	private int linhas;
	private int colunas;
	private Peca pecas[][];
	
	public Tabuleiro(int linhas, int colunas) {
		if(linhas < 0 || colunas < 1) {
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
			throw new TabuleiroException("Já existe uma peça nesta posição!");
		}
		pecas[pos.getLinha()][pos.getColuna()] = peca;
		peca.posicao = pos;
	}
	
	public boolean posicaoExistente(int linha, int coluna) {
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
}
