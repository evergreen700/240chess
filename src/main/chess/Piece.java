package chess;

import java.util.Collection;

public class Piece implements ChessPiece {

    //public enum PieceType {Pawn, Rook, Knight, Bishop, Queen, King};

    protected ChessGame.TeamColor teamColor;
    protected ChessPiece.PieceType type;

    public Piece(ChessGame.TeamColor color, ChessPiece.PieceType type){
        teamColor = color;
        this.type = type;
    }


    /**
     * @return Which team this chess piece belongs to
     */
    @Override
    public ChessGame.TeamColor getTeamColor() {
        return teamColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    @Override
    public PieceType getPieceType() {
        return type;
    }

    protected boolean isPossibleMove(ChessBoard board, ChessPosition newPosition){
        if (newPosition.getRow() < 1 || newPosition.getRow() > 8 || newPosition.getColumn() < 1 || newPosition.getColumn() > 8){
            return false;
        }else if(board.getPiece(newPosition) != null && board.getPiece(newPosition).getTeamColor() == this.teamColor){
            return false;
        }else{
            return true;
        }
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in danger
     *
     * @param board
     * @param myPosition
     * @return Collection of valid moves
     */
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        return null;
    }
    @Override
    public String toString(){
        return teamColor + " " + type;
    }

}
