package sudokuSolver;

import java.util.Arrays;

public class Sudoku{
	private int[][] digits;

	public Sudoku(int[] digits){
		this.digits = new int[9][9];
		for(int i = 0; i < 9; i++){
			this.digits[i] = Arrays.copyOfRange(digits, i * 9, i * 9 + 9);
		}
	}

	public int[] toIntArray(){
		int[] returnDigits = new int[81];
		int i = 0;
		for(int y = 0; y < 9; y++){
			for(int x = 0; x < 9; x++){
				returnDigits[i] = this.digits[y][x];
				i += 1;
			}
		}
		return returnDigits;
	}

	public int getDigit(int x, int y){
		return this.digits[y][x];
	}

	public void setDigit(int x, int y, int value){
		this.digits[y][x] = value;
	}

	public boolean isFilled(){
		for(int y = 0; y < 9; y++){
			for(int x = 0; x < 9; x++){
				if(this.digits[y][x] == -1){
					return false;
				}
			}
		}
		return true;
	}
}