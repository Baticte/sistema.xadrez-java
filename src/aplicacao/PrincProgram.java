package aplicacao;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import xadrez.PartidaXadrez;
import xadrez.PecaXadrez;
import xadrez.PosicaoXadrez;
import xadrez.XadrezException;

public class PrincProgram {

	public static void main(String[] args) {

		Scanner input = new Scanner(System.in);
		PartidaXadrez partidaXadrez = new PartidaXadrez();
		List<PecaXadrez> capturada = new ArrayList<>();

		while(!partidaXadrez.isCheckMate()) {
			try {
				UI.clearScreen();
				UI.printPartida(partidaXadrez, capturada);
				System.out.println();
				System.out.print("Origem: ");
				PosicaoXadrez origem = UI.lerPosicaoPeca(input);
				
				boolean possiveisMovimento[][] = partidaXadrez.movimentosPossivel(origem);
				UI.clearScreen();
				UI.printTabuleiro(partidaXadrez.getPecas(), possiveisMovimento);

				System.out.println();
				System.out.print("Destino: ");
				PosicaoXadrez destino = UI.lerPosicaoPeca(input);

				PecaXadrez pecaCapturada = partidaXadrez.executarMovimXadrez(origem, destino);
				
				if(pecaCapturada != null) {
					capturada.add(pecaCapturada);
				}

				if(partidaXadrez.getPromocao() != null) {
					System.out.print("Digite a peca para promoção (B/N/R/Q): ");
					String string = input.nextLine().toUpperCase();
					while((!string.equals("B") && !string.equals("N") && !string.equals("R") && !string.equals("Q"))) {
						System.out.print("Valor inválido tente novamente (B/N/R/Q): ");
						string = input.nextLine().toUpperCase();					
					}
					partidaXadrez.substituirParaPecaPromocao(string);
				}

			} catch (XadrezException e) {
				System.out.println(e.getMessage());
				input.nextLine();
				
			} catch (InputMismatchException e) {
				System.out.println(e.getMessage());
				input.nextLine();
			}
		}
		UI.clearScreen();
		UI.printPartida(partidaXadrez, capturada);
	}

}
