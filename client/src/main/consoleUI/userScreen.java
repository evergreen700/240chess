package consoleUI;

import chess.ChessGame;
import chess.Game;
import reqRes.baseResponse;
import serverFacade.ServerFacade;

import webSocketMessages.userCommands.*;
import websocket.webSocketFacade;

import java.util.Arrays;
import java.util.Scanner;


import static consoleUI.EscapeSequences.*;

public class userScreen {
    public static void userLoop(Scanner uIn, ServerFacade server, String user, String auth){
        baseResponse.gameInfo[] gameList = server.listGames(auth);
        int gameRank = 0;
        OuterLoop:
        while(true) {
            System.out.print(String.format("(%s)>>>", user));
            String userInput = uIn.nextLine().trim();
            String[] inputParts = userInput.split("\s+");
            switch (inputParts[0].toLowerCase()){
                case "logout":
                    System.out.println("Logging out");
                    server.logout(auth);
                    break OuterLoop;
                case "create":
                    if (inputParts.length < 2){
                        System.out.println("Not enough arguments provided. Correct format is \"register <game name>\"");
                        continue;
                    }
                    String gameName = String.join(" ", Arrays.copyOfRange(inputParts,1, inputParts.length));
                    System.out.println("Creating game "+ gameName);
                    server.createGame(auth, gameName);
                    gameList = server.listGames(auth);
                    continue;
                case "list":
                    gameList = server.listGames(auth);
                    System.out.println("Listing all games");
                    System.out.println(String.format("%-6s %-20s %-15s %-15s", "ID", "Game Name", "White Player", "Black Player"));
                    int gameNumber = 0;
                    for (baseResponse.gameInfo i : gameList){
                        gameNumber += 1;
                        System.out.println(String.format("%-6s %-20.20s %-15.15s %-15.15s", gameNumber, i.gameName, i.whiteUsername, i.blackUsername));
                    }
                    continue;
                case "join":
                    if (inputParts.length < 3){
                        System.out.println("Not enough arguments provided. Correct format is \"join <ID> <playerColor>\". Valid player colors are BLACK and WHITE");
                        continue;
                    }
                    if (!inputParts[1].matches("[0-9]+")){
                        System.out.println("\"ID\" argument is not a valid number.");
                        continue;
                    }
                    gameRank = Integer.parseInt(inputParts[1]);
                    if (gameRank>gameList.length || gameRank <= 0){
                        System.out.println("\"ID\" argument is not within the allowed range. Use the \"list\" argument to see which IDs are valid.");
                        continue;
                    }
                    String playerColor = inputParts[2].toUpperCase();
                    ChessGame.TeamColor teamColor;
                    if (playerColor.equals("BLACK")){
                        teamColor = ChessGame.TeamColor.BLACK;
                    }else if(playerColor.equals("WHITE")) {
                        teamColor = ChessGame.TeamColor.WHITE;
                    }else{
                        System.out.println("Invalid player color. Valid colors are WHITE and BLACK.");
                        continue;
                    }
                    System.out.println("Joining game "+gameList[gameRank-1].gameName);

                    if (server.joinGame(auth,gameList[gameRank-1].gameID,playerColor)){
                        webSocketFacade socketServer = new webSocketFacade("localhost", "8080");
                        System.out.println("Successfully joined game "+gameList[gameRank-1].gameName + " as " + playerColor);
                        socketServer.sendCommand(new JoinPCommand(auth, gameList[gameRank-1].gameID, teamColor));
                        gamePlayScreen.gamePlayLoop(uIn, server, socketServer, user, auth, gameList[gameRank-1].gameID, teamColor);
                        new boardDrawer().drawBoard(new Game().getBoard(), teamColor);
                    }else{
                        System.out.println(playerColor + " is already taken for game "+gameList[gameRank-1].gameName);
                    }
                    continue;
                case "watch":
                    if (inputParts.length < 2){
                        System.out.println("Not enough arguments provided. Correct format is \"observe <ID> <playerColor>\".");
                        continue;
                    }
                    if (!inputParts[1].matches("[0-9]+")){
                        System.out.println("\"ID\" argument is not a valid number.");
                        continue;
                    }
                    gameRank = Integer.parseInt(inputParts[1]);
                    if (gameRank>gameList.length || gameRank <= 0){
                        System.out.println("\"ID\" argument is not within the allowed range. Use the \"list\" argument to see which IDs are valid.");
                        continue;
                    }
                    if (server.joinGame(auth,gameList[gameRank-1].gameID, null)){
                        webSocketFacade socketServer = new webSocketFacade("localhost", "8080");
                        System.out.println("Successfully joined game "+gameList[gameRank-1].gameName + " as a spectator");
                        socketServer.sendCommand(new SimpleCommand(UserGameCommand.CommandType.JOIN_OBSERVER, auth, gameList[gameRank-1].gameID));
                        gamePlayScreen.gamePlayLoop(uIn, server, socketServer, user, auth, gameList[gameRank-1].gameID, null);
                        new boardDrawer().drawBoard(new Game().getBoard(), ChessGame.TeamColor.WHITE);
                    }else{
                        System.out.println("Something went wrong");
                    }
                    continue;
                case "help":
                    printHelp();
                    continue;
                default:
                    System.out.println("Invalid option. Type \"help\" for more options");
            }
        }
    }
    private static void printHelp(){
        System.out.println("LOGIN HELP");
        System.out.println(SET_TEXT_COLOR_GREEN+"create <GAME_NAME>"+SET_TEXT_COLOR_WHITE+" - Creates new game with given name");
        System.out.println(SET_TEXT_COLOR_GREEN+"list"+SET_TEXT_COLOR_WHITE+" - Lists all games");
        System.out.println(SET_TEXT_COLOR_GREEN+"join <GAMEID> <PLAYER_COLOR>"+SET_TEXT_COLOR_WHITE+" - Joins a game as a player");
        System.out.println(SET_TEXT_COLOR_GREEN+"watch <GAMEID>"+SET_TEXT_COLOR_WHITE+" - Joins a game as a spectator");
        System.out.println(SET_TEXT_COLOR_GREEN+"help"+SET_TEXT_COLOR_WHITE+" - Display these options");
        System.out.println(SET_TEXT_COLOR_GREEN+"logout"+SET_TEXT_COLOR_WHITE+" - Log out");
        System.out.println();
    }
}
