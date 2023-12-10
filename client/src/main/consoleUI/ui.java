package consoleUI;

import serverFacade.ServerFacade;
import java.util.Scanner;

public class ui {
    public static void main(String[] args){
        ServerFacade chessServer = new ServerFacade("localhost", "8080");
        Scanner userIn = new Scanner(System.in);
        loginScreen.loginLoop(userIn, chessServer);
    }
}
