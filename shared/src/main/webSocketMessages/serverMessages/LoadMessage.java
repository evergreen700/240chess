package webSocketMessages.serverMessages;

import chess.*;
import com.google.gson.*;

import java.lang.reflect.Type;

import static chess.ChessPiece.PieceType.*;
import static chess.ChessPiece.PieceType.KING;

public class LoadMessage extends ServerMessage {
    public Game game;
    public LoadMessage(Game game) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
    }

    public static Gson serializer(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Game.class, new Game.ChessGameAdapter());
        return gsonBuilder.create();
    }

    public static class ChessGameAdapter implements JsonDeserializer<Game> {
        public Game deserialize(JsonElement el, Type type, JsonDeserializationContext ctx){
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(Board.class, new Game.BoardAdapter());
            var serializer = gsonBuilder.create();
            return serializer.fromJson(el, Game.class);
        }
    }

    public static class BoardAdapter implements JsonDeserializer<Board>{
        public Board deserialize(JsonElement el, Type type, JsonDeserializationContext ctx){
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(ChessPiece.class, new Game.ChessPieceAdapter());
            var serializer = gsonBuilder.create();
            return serializer.fromJson(el, Board.class);
        }
    }

    public static class ChessPieceAdapter implements JsonDeserializer<ChessPiece>{
        public ChessPiece deserialize(JsonElement el, Type type, JsonDeserializationContext ctx){
            var newPiece = new Gson().fromJson(el, Piece.class);
            var pieceType = newPiece.getPieceType();
            var pieceColor = newPiece.getTeamColor();
            switch (pieceType){
                case PAWN:
                    return new Pawn(pieceColor, PAWN);
                //break;
                case ROOK:
                    return new Rook(pieceColor, ROOK);
                //break;
                case KNIGHT:
                    return new Knight(pieceColor, KNIGHT);
                //break;
                case BISHOP:
                    return new Bishop(pieceColor, BISHOP);
                //break;
                case QUEEN:
                    return new Queen(pieceColor, QUEEN);
                //break;
                case KING:
                    return new King(pieceColor, KING);
                //break;
            }
            return new Piece(pieceColor, pieceType);
        }
    }
}
