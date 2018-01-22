package dal.clustering.document.shared.sparsification;

public class Alpha {
	
	private double value;
	private int row;
	private int col;
	
	public Alpha(double value, int row, int col) {
		this.value = value;
		this.row = row;
		this.col = col;
	}
	
	public Double getValue() {
		return value;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}
}
