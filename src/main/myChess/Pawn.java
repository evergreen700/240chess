package myChess;

import chess.*;

import java.util.Collection;
import java.util.HashSet;

import static chess.ChessGame.TeamColor.*;
import static chess.ChessPiece.PieceType.*;

public class Pawn extends Piece {
    public Pawn(ChessGame.TeamColor color, PieceType type) {
        super(color, type);
    }
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> possibleMoves = new HashSet<ChessMove>();
        int direction;
        int startRow;
        int lastRow;
        if (teamColor == WHITE){
            direction = 1;
            startRow = 2;
            lastRow = 8;
        }else{
            direction = -1;
            startRow = 7;
            lastRow = 1;
        }
        ChessPosition regMove = new Position(myPosition.getRow() + direction, myPosition.getColumn());
        if (isPossibleMove(board, regMove) && board.getPiece(regMove) == null){
            if(regMove.getRow() == lastRow){
                for (ChessPiece.PieceType t : new ChessPiece.PieceType[]{QUEEN, BISHOP, ROOK, KNIGHT}){
                    possibleMoves.add(new Move(myPosition, regMove, t));
                }
            }else {
                possibleMoves.add(new Move(myPosition, regMove, null));
            }
        }
        if(myPosition.getRow() == startRow && possibleMoves.size() > 0){
            regMove = new Position(myPosition.getRow() + (direction*2), myPosition.getColumn());
            if (isPossibleMove(board, regMove) && board.getPiece(regMove) == null){
                possibleMoves.add(new Move(myPosition, regMove, null));
            }
        }
        for (int i : new int[]{-1, 1}) {
            regMove = new Position(myPosition.getRow() + direction, myPosition.getColumn() +i);
            if (isPossibleMove(board, regMove) && board.getPiece(regMove) != null) {
                if(regMove.getRow() == lastRow){
                    for (ChessPiece.PieceType t : new ChessPiece.PieceType[]{QUEEN, BISHOP, ROOK, KNIGHT}){
                        possibleMoves.add(new Move(myPosition, regMove, t));
                    }
                }else {
                    possibleMoves.add(new Move(myPosition, regMove, null));
                }
            }
        }

        return possibleMoves;
    }

}
