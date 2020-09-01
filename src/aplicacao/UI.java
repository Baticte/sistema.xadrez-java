package aplicacao;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import xadrez.Cor;
import xadrez.PartidaXadrez;
import xadrez.PecaXadrez;
import xadrez.PosicaoXadrez;

public class UI {

	// link de onde o codigo foi pego, com o objetivo de usar o terminal de Git Bash porque e colorido
	// https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println

	//Codigos de cores das letras
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";

	//Codigos de cores dos fundos
	public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
	public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
	public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
	public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
	public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
	public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
	public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
	public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

	public static void clearScreen() {  
		try {
			//Thread.sleep(1000); // Espera 1 segundo para ver a mágica acontecer
			new ProcessBuilder("clear").inheritIO().start().waitFor();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}	

	public static PosicaoXadrez lerPosicaoPeca(Scanner input) {
		try {
			String s = input.nextLine();
			char coluna = s.charAt(0);
			int linha = Integer.parseInt(s.substring(1));
			return new PosicaoXadrez(coluna, linha);
		} catch (RuntimeException e) {
			throw new InputMismatchException("Erro lendo a posição de xadrez, valores válidos são de a1 à h8");
		}
	}

	public static void printPartida(PartidaXadrez partidaXadrez, List<PecaXadrez> capturada) {
		printTabuleiro(partidaXadrez.getPecas());
		System.out.println();
		printPecasCapturadas(capturada);
		System.out.println();
		System.out.println("Turno : " + partidaXadrez.getTurno());
		if(!partidaXadrez.isCheckMate()) {
			System.out.println("Esperando o jogador: " + partidaXadrez.getJogadorAtual());
			if(partidaXadrez.isCheck()) {
				System.out.println("Você está em CHECK!!!");
			}
		} else {
			System.out.println("CHECKMATE!!!");
			System.out.println("VENCEDOR: " + partidaXadrez.getJogadorAtual());
		}
	}

	public static void printTabuleiro(PecaXadrez[][] pecas) {
		for(int i = 0; i < pecas.length; i++) {
			System.out.print((8 - i) + " ");
			for(int j = 0; j < pecas.length; j++) {
				printPeca(pecas[i][j], false);
			}
			System.out.println();
		}
		System.out.println("  a b c d e f g h");
	}
	public static void printTabuleiro(PecaXadrez[][] pecas, boolean[][] possiveisMovimento) {
		for(int i = 0; i < pecas.length; i++) {
			System.out.print((8 - i) + " ");
			for(int j = 0; j < pecas.length; j++) {
				printPeca(pecas[i][j], possiveisMovimento[i][j]);
			}
			System.out.println();
		}
		System.out.println("  a b c d e f g h");
	}

	private static void printPeca(PecaXadrez peca, boolean fundoPeca) {
		if(fundoPeca) {
			System.out.print(ANSI_BLUE_BACKGROUND);
		}
		if (peca == null) {
			System.out.print("-" + ANSI_RESET);
		}
		else {
			if (peca.getCor() == Cor.WHITE) {
				System.out.print(ANSI_WHITE + peca + ANSI_RESET);
			}
			else {
				System.out.print(ANSI_YELLOW + peca + ANSI_RESET);
			}
		}
		System.out.print(" ");
	}

	private static void printPecasCapturadas(List<PecaXadrez> capturado) {
		List<PecaXadrez> brancas = capturado.stream().filter(x -> x.getCor() == Cor.WHITE).collect(Collectors.toList());
		List<PecaXadrez> pretas = capturado.stream().filter(x -> x.getCor() == Cor.BLACK).collect(Collectors.toList());
		System.out.println("Peças capturadas:");
		System.out.print("Branca: ");
		System.out.print(ANSI_WHITE);
		System.out.println(Arrays.toString(brancas.toArray()));
		System.out.print(ANSI_RESET);

		System.out.print("Preta: ");
		System.out.print(ANSI_YELLOW);
		System.out.println(Arrays.toString(pretas.toArray()));
		System.out.print(ANSI_RESET);
	}

}
