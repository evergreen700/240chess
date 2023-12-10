package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPCommand extends UserGameCommand{
    public ChessGame.TeamColor playerColor;
    public int gameID;
    public JoinPCommand(String authToken, int gameID, ChessGame.TeamColor playerColor) {
        super(authToken);
        this.commandType = CommandType.JOIN_PLAYER;
        this.gameID = gameID;
        this.playerColor = playerColor;
    }
}
