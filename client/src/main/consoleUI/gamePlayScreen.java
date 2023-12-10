package consoleUI;

import chess.*;
import serverFacade.ServerFacade;
import webSocketMessages.userCommands.JoinPCommand;
import webSocketMessages.userCommands.MoveCommand;
import webSocketMessages.userCommands.SimpleCommand;
import webSocketMessages.userCommands.UserGameCommand;
import websocket.webSocketFacade;

import java.util.Scanner;

import static consoleUI.EscapeSequences.SET_TEXT_COLOR_GREEN;
import static consoleUI.EscapeSequences.SET_TEXT_COLOR_WHITE;


public class gamePlayScreen {
    public static void gamePlayLoop(Scanner uIn, ServerFacade server, webSocketFacade socketServer, String user, String auth, int gameID, ChessGame.TeamColor side){
        if(side != null) {
            socketServer.sendCommand(new JoinPCommand(auth, gameID, side));
        }else{
            socketServer.sendCommand(new SimpleCommand(UserGameCommand.CommandType.JOIN_OBSERVER, auth, gameID));
        }
        OuterLoop:
        while(true) {
            System.out.print(String.format("Game>>>"));
            String userInput = uIn.nextLine().trim();
            String[] inputParts = userInput.split("\s+");
            switch (inputParts[0].toLowerCase()){
                case "redraw":
                    new boardDrawer().drawBoard(socketServer.game.getBoard(), side);
                    break;
                case "drawmoves":
                    if (inputParts.length >= 2){
                        String startPos = inputParts[1].toLowerCase();
                        if (startPos.length() == 2 &&
                                startPos.charAt(0) >= 'a' && startPos.charAt(0) <= 'h' &&
                                startPos.charAt(1) >= '1' && startPos.charAt(1) <= '8'){
                            new boardDrawer().highlightMoves(socketServer.game.getBoard(), new Position(startPos.charAt(1) - '1' + 1,startPos.charAt(0) - 'a' + 1), side);
                        }else{
                            System.out.println("Improper position format. Correct format is letter then number, for example \"b1\" or \"a3\"");
                        }
                    }else{
                        System.out.println("Improper move format. Correct format is move <startPosition> <endPosition>, for example \"move b1 a3\"");
                    }
                    break;
                case "move":
                    if (inputParts.length >= 3){
                        String startPos = inputParts[1].toLowerCase();
                        String endPos = inputParts[2].toLowerCase();
                        if (startPos.length() == 2 && endPos.length() == 2 &&
                                startPos.charAt(0) >= 'a' && startPos.charAt(0) <= 'h' &&
                                endPos.charAt(0) >= 'a' && endPos.charAt(0) <= 'h' &&
                                startPos.charAt(1) >= '1' && startPos.charAt(1) <= '8' &&
                                endPos.charAt(1) >= '1' && endPos.charAt(1) <= '8'){
                            Move tentativeMove = new Move(new Position(startPos.charAt(1) - '1' + 1,startPos.charAt(0) - 'a' + 1), new Position(endPos.charAt(1) - '1' + 1, endPos.charAt(0) - 'a' + 1), null);
                            socketServer.sendCommand(new MoveCommand(auth, gameID, tentativeMove));
                        }else{
                            System.out.println("Improper position format. Correct format is letter then number, for example \"b1\" or \"a3\"");
                        }
                    }else{
                        System.out.println("Improper move format. Correct format is move <startPosition> <endPosition>, for example \"move b1 a3\"");
                    }
                    break;
                case "resign":
                    socketServer.sendCommand(new SimpleCommand(UserGameCommand.CommandType.RESIGN, auth, gameID));
                    break;
                case "leave":
                    socketServer.sendCommand(new SimpleCommand(UserGameCommand.CommandType.LEAVE, auth, gameID));
                    break OuterLoop;
                case "help":
                    printHelp();
                    break;
                default:
                    System.out.println("Invalid option. Type \"help\" for more options");
            }
        }
    }
    private static void printHelp(){
        System.out.println("GAME HELP");
        System.out.println(SET_TEXT_COLOR_GREEN+"makeMove <START> <END>"+SET_TEXT_COLOR_WHITE+" - Make a move from start and end positions indicated with numbers on the board");
        System.out.println(SET_TEXT_COLOR_GREEN+"redraw"+SET_TEXT_COLOR_WHITE+" - Show the board again");
        System.out.println(SET_TEXT_COLOR_GREEN+"drawMoves <START>"+SET_TEXT_COLOR_WHITE+" - Shows possible moves for a given piece");
        System.out.println(SET_TEXT_COLOR_GREEN+"resign"+SET_TEXT_COLOR_WHITE+" - Forfeit the game");
        System.out.println(SET_TEXT_COLOR_GREEN+"help"+SET_TEXT_COLOR_WHITE+" - Display these options");
        System.out.println(SET_TEXT_COLOR_GREEN+"leave"+SET_TEXT_COLOR_WHITE+" - Leave the game. The game can be continued if you or another player joins");
        System.out.println();
    }
}
