package myChess;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class Bishop extends Piece {
    public Bishop(ChessGame.TeamColor color, PieceType type) {
        super(color, type);
    }
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> possibleMoves = new HashSet<ChessMove>();
        for (int i : new int[]{-1, 1}) {
            for (int j : new int[]{-1, 1}) {
                ChessPosition newPosition = new Position(myPosition.getRow() + i, myPosition.getColumn() - j);
                while (isPossibleMove(board, newPosition)) {
                    possibleMoves.add(new Move(myPosition, newPosition, null));
                    if (board.getPiece(newPosition) != null) {
                        break;
                    }
                    newPosition = new Position(newPosition.getRow() + i, newPosition.getColumn() - j);
                }
            }
        }
        return possibleMoves;
    }
}
