package webSocketMessages.userCommands;

import chess.*;
import com.google.gson.*;

import java.lang.reflect.Type;

public class SimpleCommand extends UserGameCommand{
    public int gameID;
    public SimpleCommand(CommandType type, String authToken, int gameID) {
        super(authToken);
        this.commandType = type;
        this.gameID = gameID;
    }

}
