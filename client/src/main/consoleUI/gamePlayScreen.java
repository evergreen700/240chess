package consoleUI;

import chess.*;
import serverFacade.ServerFacade;
import webSocketMessages.userCommands.MoveCommand;
import webSocketMessages.userCommands.SimpleCommand;
import webSocketMessages.userCommands.UserGameCommand;
import websocket.webSocketFacade;

import java.util.Scanner;

public class gamePlayScreen {
    public static void gamePlayLoop(Scanner uIn, ServerFacade server, webSocketFacade socketServer, String user, String auth, int gameID, ChessGame.TeamColor side){
        boolean gameComplete = false;
        ChessGame currentGame = new Game();
        OuterLoop:
        while(true) {
            System.out.print(String.format("Game>>>"));
            String userInput = uIn.nextLine().trim();
            String[] inputParts = userInput.split("\s+");
            switch (inputParts[0].toLowerCase()){
                case "redraw":
                    new boardDrawer().drawBoard(new Game().getBoard(), side);
                    break;
                case "drawMoves":
                    break;
                case "move":
                    if (socketServer.game.getTeamTurn() != side){
                        if (side == null){
                            System.out.println("You cannot make a move as an observer");
                        }else{
                            System.out.println("It is not your turn");
                        }
                    }
                    if (inputParts.length >= 3){
                        String startPos = inputParts[1].toLowerCase();
                        String endPos = inputParts[2].toLowerCase();
                        if (startPos.length() == 2 && endPos.length() == 2 &&
                                startPos.charAt(0) >= 'a' && startPos.charAt(0) <= 'h' &&
                                endPos.charAt(0) >= 'a' && endPos.charAt(0) <= 'h' &&
                                startPos.charAt(1) >= '1' && startPos.charAt(1) <= '8' &&
                                endPos.charAt(1) >= '1' && endPos.charAt(1) <= '8'){
                            try {
                                Move tentativeMove = new Move(new Position(startPos.charAt(1) - '1' + 1,startPos.charAt(0) - 'a' + 1), new Position(endPos.charAt(1) - '1' + 1, endPos.charAt(0) - 'a' + 1), null);
                                currentGame.makeMove(tentativeMove);
                                socketServer.sendCommand(new MoveCommand(auth, gameID, tentativeMove));
                            }catch(InvalidMoveException e){
                                System.out.println("Move not allowed");
                            }
                        }else{
                            System.out.println("Improper position format. Correct format is letter then number, for example \"b1\" or \"a3\"");
                        }
                    }else{
                        System.out.println("Improper move format. Correct format is move <startPosition> <endPosition>, for example \"move b1 a3\"");
                    }
                    break;
                case "resign":
                    if (gameComplete==true){
                        System.out.println("You cannot resign, game is already over");
                    }else{
                        socketServer.sendCommand(new SimpleCommand(UserGameCommand.CommandType.RESIGN, auth, gameID));
                    }
                    break;
                case "leave":
                    if (gameComplete==true){
                        socketServer.sendCommand(new SimpleCommand(UserGameCommand.CommandType.LEAVE, auth, gameID));
                        break OuterLoop;
                    }else{
                        System.out.println("You cannot leave while still in a game");
                    }
                    break;
                case "help":
                    break;
                default:

            }
        }
    }
}
