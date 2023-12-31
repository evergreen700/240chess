package chess;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashSet;

import static chess.ChessGame.TeamColor.*;
import static chess.ChessPiece.PieceType.*;

public class Game implements ChessGame {

    public Board gameBoard;
    public boolean active;
    private Board deepCopyBoard(){
        Board boardCopy = new Board();
        for(int i=1;i<=8;i++){
            for(int j=1;j<=8;j++){
                Position currentPosition = new Position(i,j);
                boardCopy.addPiece(currentPosition,gameBoard.getPiece(currentPosition));
            }
        }
        return boardCopy;
    }
    TeamColor currentTurn;

    public Game(){
        currentTurn = WHITE;
        gameBoard = new Board();
        gameBoard.resetBoard();
        active = true;
    }
    //enum TeamColor {White, Black};
    /**
     * @return Which team's turn it is
     */
    @Override
    public TeamColor getTeamTurn() {
        return currentTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    @Override
    public void setTeamTurn(TeamColor team) {
        currentTurn = team;
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at startPosition
     */
    @Override
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        HashSet<ChessMove> first;
        HashSet<ChessMove> last = new HashSet<ChessMove>();
        first = (HashSet<ChessMove>) gameBoard.getPiece(startPosition).pieceMoves(gameBoard, startPosition);
        for (ChessMove i : first){
            Move testMove = (Move) i;
            Board tempCopy = deepCopyBoard();
            tempCopy.addPiece(testMove.getEndPosition(), tempCopy.getPiece(testMove.getStartPosition()));
            tempCopy.addPiece(testMove.getStartPosition(), null);
            if(!isInCheck(gameBoard.getPiece(startPosition).getTeamColor())){
                last.add(testMove);
            }
        }
        return last;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    @Override
    public void makeMove(ChessMove move) throws InvalidMoveException {
        for (int coord : new int[]{move.getStartPosition().getRow(),move.getStartPosition().getColumn()}){
            if(coord < 1 || coord > 8){
                throw new InvalidMoveException("Start index out of bounds");
            }
        }
        Piece currentPiece = (Piece) gameBoard.getPiece(move.getStartPosition());
        if (currentPiece == null){
            throw new InvalidMoveException("No piece at start position");
        }
        if (currentPiece.getTeamColor() != currentTurn){
            throw new InvalidMoveException(currentPiece.getTeamColor() + " piece cannot be moved on " + currentTurn + "'s turn");
        }
        Collection<ChessMove> possibleMoves = validMoves(move.getStartPosition());
        if(possibleMoves == null){
            throw new InvalidMoveException(currentPiece.getPieceType() + " at " + move.getStartPosition().toString() + " has no valid moves");
        }
        if(possibleMoves.contains(move)){
            gameBoard.addPiece(move.getEndPosition(), gameBoard.getPiece(move.getStartPosition()));
            gameBoard.addPiece(move.getStartPosition(), null);

            if(move.getPromotionPiece() != null){
                switch(move.getPromotionPiece()){
                    case ROOK:
                        gameBoard.addPiece(move.getEndPosition(), new Rook(currentTurn,ROOK));
                        break;
                    case KNIGHT:
                        gameBoard.addPiece(move.getEndPosition(), new Knight(currentTurn,KNIGHT));
                        break;
                    case BISHOP:
                        gameBoard.addPiece(move.getEndPosition(), new Bishop(currentTurn,BISHOP));
                        break;
                    case QUEEN:
                        gameBoard.addPiece(move.getEndPosition(), new Queen(currentTurn,QUEEN));
                        break;
                }
            }
            if (currentTurn == BLACK){
                currentTurn = WHITE;
            }else{
                currentTurn = BLACK;
            }
        }else{
            throw new chess.InvalidMoveException(currentPiece.getPieceType() + " " + move.toString());
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    @Override
    public boolean isInCheck(TeamColor teamColor) {
        Position teamKing = null;
        HashSet<ChessMove> possibleMoves = new HashSet<ChessMove>();
        for(int i = 1; i<=8; i++){
            for(int j = 1; j<=8; j++){
                Position current = new Position(i,j);
                if(gameBoard.getPiece(current) != null) {
                    if (gameBoard.getPiece(current).getPieceType() == KING && gameBoard.getPiece(current).getTeamColor() == teamColor) {
                        teamKing = current;
                    }
                    if (gameBoard.getPiece(current).getTeamColor() != teamColor) {
                        possibleMoves.addAll(gameBoard.getPiece(current).pieceMoves(gameBoard, current));
                    }
                }
            }
        }
        for(ChessMove i : possibleMoves){
            if(i.getEndPosition().equals(teamKing)){
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    @Override
    public boolean isInCheckmate(TeamColor teamColor) {
        if(isInCheck(teamColor)){
            for(int i = 1; i<=8; i++){
                for(int j = 1; j<=8; j++) {
                    Position testPosition = new Position(i,j);
                    if(gameBoard.getPiece(testPosition) != null && gameBoard.getPiece(testPosition).getTeamColor() == teamColor){
                        if(validMoves(testPosition).size() > 0){
                            return false;
                        }
                    }
                }
                }
            return true;
        }else {
            return false;
        }
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    @Override
    public boolean isInStalemate(TeamColor teamColor) {
        if(!isInCheck(teamColor)){
            for(int i = 1; i<=8; i++){
                for(int j = 1; j<=8; j++) {
                    Position testPosition = new Position(i,j);
                    if(gameBoard.getPiece(testPosition) != null && gameBoard.getPiece(testPosition).getTeamColor() == teamColor){
                        if(validMoves(testPosition).size() > 0){
                            return false;
                        }
                    }
                }
            }
            return true;
        }else {
            return false;
        }
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    @Override
    public void setBoard(ChessBoard board) {
        gameBoard = (Board) board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    @Override
    public ChessBoard getBoard() {
        return gameBoard;
    }

    public static Gson serializer(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Game.class, new ChessGameAdapter());
        return gsonBuilder.create();
    }

    public static class ChessGameAdapter implements JsonDeserializer<Game>{
        public Game deserialize(JsonElement el, Type type, JsonDeserializationContext ctx){
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(Board.class, new BoardAdapter());
            var serializer = gsonBuilder.create();
            return serializer.fromJson(el, Game.class);
        }
    }

    public static class BoardAdapter implements JsonDeserializer<Board>{
        public Board deserialize(JsonElement el, Type type, JsonDeserializationContext ctx){
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(ChessPiece.class, new ChessPieceAdapter());
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
