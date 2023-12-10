package webSocketMessages.userCommands;

import chess.ChessMove;
import chess.ChessPosition;
import chess.Move;
import chess.Position;
import com.google.gson.*;

import java.lang.reflect.Type;

public class MoveCommand extends UserGameCommand{
    public int gameID;
    public Move move;
    public MoveCommand(String authToken, int gameID, Move move) {
        super(authToken);
        this.commandType = CommandType.MAKE_MOVE;
        this.gameID = gameID;
        this.move = move;
    }

    public static Gson serializer(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Move.class, new MoveAdapter());
        return gsonBuilder.create();
    }

    public static class MoveAdapter implements JsonDeserializer<Move> {
        public Move deserialize(JsonElement el, Type type, JsonDeserializationContext ctx){
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(ChessPosition.class, new PositionAdapter());
            var serializer = gsonBuilder.create();
            return serializer.fromJson(el, Move.class);
        }
    }

    public static class PositionAdapter implements JsonDeserializer<ChessPosition> {
        public ChessPosition deserialize(JsonElement el, Type type, JsonDeserializationContext ctx) {
            return new Gson().fromJson(el, Position.class);
        }
    }
}
