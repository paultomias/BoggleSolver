/*Class which has the letter, row index and column index of a boggle piece*/
class BogglePiece{
	public char letter;
	public int row_index;
	public int col_index;

	public BogglePiece(char c, int row, int col){
		letter = c;
		row_index = row;
		col_index = col;
	}

	public char getChar(){
		return letter;
	}

	public int getRowIndex(){
		return row_index;
	}

	public int getColIndex(){
		return col_index;
	}

	@Override
   	public String toString() {
        return (this.getChar()+" "+this.getRowIndex()+" "+this.getColIndex());
    }
}