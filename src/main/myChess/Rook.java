package myChess;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class Rook extends Piece{
    public boolean CastlePotential;
    public Rook(ChessGame.TeamColor color, PieceType type) {
        super(color, type);
        CastlePotential = true;
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> possibleMoves = new HashSet<ChessMove>();
        //moving left
        ChessPosition newPosition = new Position(myPosition.getRow(), myPosition.getColumn()-1);
        while(isPossibleMove(board, newPosition)){
            possibleMoves.add(new Move(myPosition,newPosition,null));
            if (board.getPiece(newPosition) != null){
                break;
            }
            newPosition = new Position(newPosition.getRow(), newPosition.getColumn()-1);
        }
        //moving right
        newPosition = new Position(myPosition.getRow(), myPosition.getColumn()+1);
        while(isPossibleMove(board, newPosition)){
            possibleMoves.add(new Move(myPosition,newPosition,null));
            if (board.getPiece(newPosition) != null){
                break;
            }
            newPosition = new Position(newPosition.getRow(), newPosition.getColumn()+1);
        }
        //moving up
        newPosition = new Position(myPosition.getRow()+1, myPosition.getColumn());
        while(isPossibleMove(board, newPosition)){
            possibleMoves.add(new Move(myPosition,newPosition,null));
            if (board.getPiece(newPosition) != null){
                break;
            }
            newPosition = new Position(newPosition.getRow()+1, newPosition.getColumn());
        }
        //moving down
        newPosition = new Position(myPosition.getRow()-1, myPosition.getColumn());
        while(isPossibleMove(board, newPosition)){
            possibleMoves.add(new Move(myPosition,newPosition,null));
            if (board.getPiece(newPosition) != null){
                break;
            }
            newPosition = new Position(newPosition.getRow()-1, newPosition.getColumn());
        }

        return possibleMoves;
    }
}
