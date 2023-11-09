package chess;

import java.util.Collection;
import java.util.HashSet;


public class King extends Piece{
    public boolean CastlePotential;
    public King(ChessGame.TeamColor color, PieceType type) {
        super(color, type);
        CastlePotential = true;
    }
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> possibleMoves = new HashSet<ChessMove>();
        for (int i : new int[]{-1,0, 1}) {
            for (int j : new int[]{-1,0, 1}) {
                if (i != 0 || j != 0){
                    ChessPosition newPosition = new Position(myPosition.getRow() + i, myPosition.getColumn() - j);
                    if (isPossibleMove(board, newPosition)) {
                        possibleMoves.add(new Move(myPosition, newPosition, null));
                    }
                }
            }
        }
        return possibleMoves;
    }
}
