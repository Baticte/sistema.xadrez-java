package aplicacao;

import xadrez.PartidaXadrez;

public class PrincProgram {

	public static void main(String[] args) {

		PartidaXadrez partXadres = new PartidaXadrez();
		//vamos criar uma funcao que vai imprimir o tabuleiro
		UI.printTabuleiro(partXadres.getPecas());
	}

}
