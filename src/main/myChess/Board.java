package myChess;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import static chess.ChessPiece.PieceType.*;

public class Board implements ChessBoard {
    private ChessPiece[][] pieces;

    public Board(){
        pieces = new ChessPiece[8][8];
    }
    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    @Override
    public void addPiece(ChessPosition position, ChessPiece piece) {

                pieces[position.getRow()-1][position.getColumn()-1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that position
     */
    @Override
    public ChessPiece getPiece(ChessPosition position) {
        return pieces[position.getRow()-1][position.getColumn()-1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    @Override
    public void resetBoard() {
        pieces = new ChessPiece[8][8];
        //row 1 white
        addPiece(new Position(1,1), new Rook(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        addPiece(new Position(1,2), new Knight(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        addPiece(new Position(1,3), new Bishop(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        addPiece(new Position(1,4), new Queen(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN));
        addPiece(new Position(1,5), new King(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));
        addPiece(new Position(1,6), new Bishop(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        addPiece(new Position(1,7), new Knight(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        addPiece(new Position(1,8), new Rook(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));

        //row 8 black
        addPiece(new Position(8,1), new Rook(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        addPiece(new Position(8,2), new Knight(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        addPiece(new Position(8,3), new Bishop(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        addPiece(new Position(8,4), new Queen(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN));
        addPiece(new Position(8,5), new King(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));
        addPiece(new Position(8,6), new Bishop(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        addPiece(new Position(8,7), new Knight(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        addPiece(new Position(8,8), new Rook(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));

        for(int i = 1; i <= 8; i++){
            addPiece(new Position(2,i), new Pawn(ChessGame.TeamColor.WHITE, PAWN));
            addPiece(new Position(7,i), new Pawn(ChessGame.TeamColor.BLACK, PAWN));
        }
    }
}
