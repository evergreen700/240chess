package chess;

import java.util.Collection;
import java.util.HashSet;

import static chess.ChessPiece.PieceType.*;

public class Queen extends Piece {
    public Queen(ChessGame.TeamColor color, PieceType type) {
        super(color, type);
    }
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> possibleMoves = new HashSet<ChessMove>();
        Bishop pseudoBishop = new Bishop(this.teamColor, BISHOP);
        possibleMoves.addAll(pseudoBishop.pieceMoves(board,myPosition));
        Rook pseudoRook = new Rook(this.teamColor, ROOK);
        possibleMoves.addAll(pseudoRook.pieceMoves(board,myPosition));
        return possibleMoves;
    }
}
