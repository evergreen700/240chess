package myChess;

public class Position implements chess.ChessPosition {

    private int row;
    private int column;

    public Position(int row, int column){
        this.row = row;
        this.column = column;
    }
    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    @Override
    public int getRow() {
        return row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    @Override
    public int getColumn() {
        return column;
    }
    @Override
    public String toString(){
        return "("+row+","+column+")";
    }
    @Override
    public boolean equals(Object o){
        if (o != null && o.getClass() == this.getClass()){
            Position other = (Position) o;
            return (this.getRow() == other.getRow() && this.getColumn() == other.getColumn());
        }else{
            return false;
        }
    }
    @Override
    public int hashCode(){
        return (column * 8)+row;
    }
}
