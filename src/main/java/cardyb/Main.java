package cardyb;

import cardyb.games.Game;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter number of players: ");
        int numPlayers = scanner.nextInt();
        Game game = new Game(numPlayers);
        game.start();
    }
}
