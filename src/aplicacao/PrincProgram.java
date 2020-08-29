package aplicacao;

import java.util.InputMismatchException;
import java.util.Scanner;

import xadrez.PartidaXadrez;
import xadrez.PecaXadrez;
import xadrez.PosicaoXadrez;
import xadrez.XadrezException;

public class PrincProgram {

	public static void main(String[] args) {

		Scanner input = new Scanner(System.in);
		PartidaXadrez partidaXadrez = new PartidaXadrez();

		while(true) {
			try {
				UI.clearScreen();
				UI.printTabuleiro(partidaXadrez.getPecas());
				System.out.println();
				System.out.print("Origem: ");
				PosicaoXadrez origem = UI.lerPosicaoPeca(input);

				System.out.println();
				System.out.print("Destino: ");
				PosicaoXadrez destino = UI.lerPosicaoPeca(input);

				PecaXadrez pecaCapturada = partidaXadrez.executarMovimXadrez(origem, destino);
			} catch (XadrezException e) {
				System.out.println(e.getMessage());
				input.nextLine();
			} catch (InputMismatchException e) {
				System.out.println(e.getMessage());
				input.nextLine();
			}
		}
	}

}
