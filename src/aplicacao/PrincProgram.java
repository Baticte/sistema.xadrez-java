package aplicacao;

import java.util.Scanner;

import xadrez.PartidaXadrez;
import xadrez.PecaXadrez;
import xadrez.PosicaoXadrez;

public class PrincProgram {

	public static void main(String[] args) {

		Scanner input = new Scanner(System.in);
		
		PartidaXadrez partidaXadrez = new PartidaXadrez();

		while(true) {
			//vamos criar uma funcao que vai imprimir o tabuleiro
			UI.printTabuleiro(partidaXadrez.getPecas());
			System.out.println();
			System.out.print("Origem: ");
			PosicaoXadrez origem = UI.lerPosicaoPeca(input);
			
			System.out.println();
			System.out.print("Destino: ");
			PosicaoXadrez destino = UI.lerPosicaoPeca(input);
			
			PecaXadrez pecaCapturada = partidaXadrez.executarMovimXadrez(origem, destino);
		}
	}

}
