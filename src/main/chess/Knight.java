package chess;

import java.util.Collection;
import java.util.HashSet;

public class Knight extends Piece{
    public Knight(ChessGame.TeamColor color, PieceType type) {
        super(color, type);
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition){
        HashSet<ChessMove> possibleMoves = new HashSet<ChessMove>();

        for (int i : new int[]{-1, 1}){
            for (int j : new int[]{-1, 1}){
                ChessPosition positionA = new Position(myPosition.getRow() + (2*i), myPosition.getColumn() + j);
                ChessPosition positionB = new Position(myPosition.getRow() + i, myPosition.getColumn() + (2*j));
                if (isPossibleMove(board, positionA)){
                    possibleMoves.add(new Move(myPosition, positionA, null));
                }
                if (isPossibleMove(board, positionB)){
                    possibleMoves.add(new Move(myPosition, positionB, null));
                }
            }
        }

        return possibleMoves;
    }
}
