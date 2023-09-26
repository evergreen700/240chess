package myChess;

import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

public class Move implements ChessMove {
    private ChessPosition startPosition;
    private ChessPosition endPosition;
    private ChessPiece.PieceType pieceType;

    public Move(ChessPosition startPos, ChessPosition endPos, ChessPiece.PieceType pieceType){
        startPosition = startPos;
        endPosition = endPos;
        this.pieceType = pieceType;
    }

    /**
     * @return ChessPosition of starting location
     */
    @Override
    public ChessPosition getStartPosition() {
        return startPosition;
    }

    /**
     * @return ChessPosition of ending location
     */
    @Override
    public ChessPosition getEndPosition() {
        return endPosition;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    @Override
    public ChessPiece.PieceType getPromotionPiece() {
        return pieceType;
    }

    @Override
    public boolean equals(Object o){
        if (o.getClass() != this.getClass()){
            return false;
        }else{
            Move otherMove = (Move) o;
            return otherMove.getStartPosition().equals(startPosition) && otherMove.getEndPosition().equals(endPosition) && otherMove.getPromotionPiece() == this.getPromotionPiece();
        }
    }

    @Override
    public int hashCode(){
        return startPosition.hashCode() + (endPosition.hashCode() * 8);
    }
    @Override
    public String toString() {
        return startPosition.toString()+"->"+endPosition.toString();
    }
}
