package consoleUI;

import serverFacade.ServerFacade;
import java.util.Scanner;
import websocket.*;

import static consoleUI.EscapeSequences.*;

public class loginScreen {
    public static void loginLoop(Scanner uIn, ServerFacade server){
        while(true){
            System.out.print(RESET_BG_COLOR+SET_TEXT_COLOR_WHITE+"(login)>>>");
            String userInput = uIn.nextLine().trim();
            String[] inputParts = userInput.split("\s+");
            String authToken = null;
            switch (inputParts[0].toLowerCase()){
                case "register":
                    if (inputParts.length < 4){
                        System.out.println("Not enough arguments provided. Correct format is \"register <username> <password> <email>\"");
                        continue;
                    }
                    authToken = server.register(inputParts[1], inputParts[2], inputParts[3]);
                    if (authToken!=null){
                        System.out.println("Registering new user");
                        userScreen.userLoop(uIn, server, inputParts[1], authToken);
                    }else{
                        System.out.println("Username already in use. Please try another.");
                    }
                    continue;
                case "login":
                    if (inputParts.length < 3){
                        System.out.println("Not enough arguments provided. Correct format is \"login <username> <password>\"");
                        continue;
                    }
                    authToken = server.login(inputParts[1], inputParts[2]);
                    if (authToken!=null){
                        System.out.println("Logging in existing user");
                        userScreen.userLoop(uIn, server, inputParts[1], authToken);
                    }else{
                        System.out.println("Invalid username or password.");
                    }
                    continue;
                case "help":
                    printHelp();
                    continue;
                case "quit":
                    System.out.println("Good bye");
                    System.exit(0);
                case "!!!clear":
                    server.clearAll();
                    System.out.println("Database cleared");
                    continue;
                default:
                    System.out.println("Invalid option. Type \"help\" for more options");
            }
        }
    }
    private static void printHelp(){
        System.out.println("USER HELP");
        System.out.println(SET_TEXT_COLOR_GREEN+"register <USERNAME> <PASSWORD> <EMAIL>"+SET_TEXT_COLOR_WHITE+" - Register a new user");
        System.out.println(SET_TEXT_COLOR_GREEN+"login <USERNAME> <PASSWORD>"+SET_TEXT_COLOR_WHITE+" - Login an existing user");
        System.out.println(SET_TEXT_COLOR_GREEN+"help"+SET_TEXT_COLOR_WHITE+" - Display these options");
        System.out.println(SET_TEXT_COLOR_GREEN+"quit"+SET_TEXT_COLOR_WHITE+" - Quit program");
        System.out.println();
    }
}
