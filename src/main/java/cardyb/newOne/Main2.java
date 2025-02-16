package cardyb.newOne;

import cardyb.newOne.models.ActionEntity;
import cardyb.newOne.models.GameEntity;
import cardyb.newOne.repository.GameRepository;
import cardyb.newOne.models.PlayerEntity;

import java.util.Scanner;

import java.util.*;

public class Main2 {


    //simulate some game state and player state creation

    // wait for user actions
    // user actions  = gameID, playerId, ActionName, ordered action inputs
    //e.g
    // game123, player456, PLAY_CARD, 1
    // the card could be CHANGE_POT, select a player to skip turn, select a player to discard 2 chosen cards
    // game123, player456, CHOOSE_PLAYER_FOR_ACTION_1 , player567
    //note CHOOSE_PLAYER_FOR_ACTION contains a list action to give to chosen user i.e. two skip turns, or discard random cards (A)
    // game123, player456, CHOOSE_PLAYER_FOR_ACTION , player789


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        GameRepository gameRepository = new GameRepository();
        //TODO save this somewhere
        for (int i = 0; i < 5; i++) {
            GameEntity game = GameFactory.createGame(4);
            System.out.println("Creating game:" + game.getId());
            System.out.println("With players:" + game.getPlayers());

            gameRepository.putGame(game);
        }

        System.out.println("Players and their actions:");
        for (PlayerEntity player : game.getPlayers()) {
            System.out.println("Player\n " + player.getId());

            for (ActionEntity nowAction : player.getNowActions()) {
                System.out.println("--" + nowAction.getName());
            }
        }


        while (true) {
            System.out.println("Enter playerI, action ID, and action inputs (comma separated) or 'exit' to quit:");
            String inputLine = scanner.nextLine();
            if (inputLine.equalsIgnoreCase("exit")) break;

            String[] inputs = inputLine.split(",");
            if (inputs.length < 2) {
                System.out.println("Invalid input. Please enter at least  playerId, and actionID.");
                continue;
            }

            String playerId = inputs[0].trim();
            String actionId = inputs[1].trim();

            List<Object> actionInputs = new ArrayList<>();
            for (int i = 2; i < inputs.length; i++) {
                String input = inputs[i].trim();
                Object parsedInput;

                if (input.matches("\\d+")) {
                    parsedInput = Integer.parseInt(input);
                } else if (input.matches("\\d+\\.\\d+")) {
                    parsedInput = Double.parseDouble(input);
                } else {
                    parsedInput = input;
                }

                actionInputs.add(parsedInput);
            }

            // Print the collected information
            System.out.println("Player ID: " + playerId);
            System.out.println("Action ID: " + actionId);
            System.out.println("Action Inputs: " + actionInputs);
            System.out.println("--------------------------\n");



            System.out.println("Process user action.");

        }

        scanner.close();
        System.out.println("Game exited.");
    }


}
