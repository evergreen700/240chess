package consoleUI;

import chess.*;

import static chess.ChessGame.TeamColor.*;
import static consoleUI.EscapeSequences.*;

public class boardDrawer {
    public void drawBoard(ChessBoard layout, ChessGame.TeamColor side){
        String bgColor = "";
        if (side == WHITE || side == null) {
            //WHITE POV
            System.out.println(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK +
                    EMPTY + " a  b  c  d  e  f  g  h " + EMPTY +
                    RESET_BG_COLOR);
            for (int i = 8; i > 0; i--) {
                System.out.print(SET_BG_COLOR_LIGHT_GREY +
                        " " + i + " ");
                for (int j = 1; j < 9; j++) {
                    if ((i + j) % 2 == 0) {
                        bgColor = SET_BG_COLOR_BLACK;
                    } else {
                        bgColor = SET_BG_COLOR_WHITE;
                    }
                    System.out.print(bgColor + getPieceSymbol(layout.getPiece(new Position(i, j))));
                }
                System.out.println(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + RESET_TEXT_BOLD_FAINT +
                        " " + i + " " +
                        RESET_BG_COLOR);
            }
            System.out.println(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK +
                    EMPTY + " a  b  c  d  e  f  g  h " + EMPTY +
                    RESET_BG_COLOR+SET_TEXT_COLOR_WHITE);
            System.out.println("");
        }else {
            //BLACK POV
            System.out.println(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK +
                    EMPTY + " h  g  f  e  d  c  b  a " + EMPTY +
                    RESET_BG_COLOR);
            for (int i = 1; i < 9; i++) {
                System.out.print(SET_BG_COLOR_LIGHT_GREY +
                        " " + i + " ");
                for (int j = 8; j > 0; j--) {
                    if ((i + j) % 2 == 0) {
                        bgColor = SET_BG_COLOR_BLACK;
                    } else {
                        bgColor = SET_BG_COLOR_WHITE;
                    }
                    System.out.print(bgColor + getPieceSymbol(layout.getPiece(new Position(i, j))));
                }
                System.out.println(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + RESET_TEXT_BOLD_FAINT +
                        " " + i + " " +
                        RESET_BG_COLOR);
            }
            System.out.println(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK +
                    EMPTY + " h  g  f  e  d  c  b  a " + EMPTY +
                    RESET_BG_COLOR + SET_TEXT_COLOR_WHITE);
            System.out.println("");
        }
    }

    private String getPieceSymbol(ChessPiece p){
        if (p==null){
            return EMPTY;
        }else{
            StringBuilder pString = new StringBuilder(SET_TEXT_BOLD);
            if (p.getTeamColor() == BLACK){
                pString.append(SET_TEXT_COLOR_BLUE);
            }else{
                pString.append(SET_TEXT_COLOR_RED);
            }
            switch(p.getPieceType()) {
                case KING:
                    pString.append(" K ");
                    break;
                case QUEEN:
                    pString.append(" Q ");
                    break;
                case BISHOP:
                    pString.append(" B ");
                    break;
                case KNIGHT:
                    pString.append(" N ");
                    break;
                case ROOK:
                    pString.append(" R ");
                    break;
                case PAWN:
                    pString.append(" P ");
                    break;
            }
            return pString.toString();
        }
    }
}
