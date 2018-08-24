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

	public Sudoku(Sudoku sudoku){
		this.digits = new int[9][9];
		for(int x = 0; x < 9; x++){
			for(int y = 0; y < 9; y++){
				this.setDigit(x, y, sudoku.getDigit(x, y));
			}
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
				if(this.digits[y][x] < 0 || this.digits[y][x] > 9){
					return false;
				}
			}
		}
		return true;
	}

	public boolean isSolved(){
		if(!this.isFilled()){
			return false;
		}
		for(int coord1 = 0; coord1 < 9; coord1++){
			boolean[] digitsHorizontal = new boolean[9];
			boolean[] digitsVertical = new boolean[9];
			for(int coord2 = 0; coord2 < 9; coord2++){
				digitsHorizontal[this.getDigit(coord1, coord2) - 1] = true;
				digitsVertical[this.getDigit(coord2, coord1) - 1] = true;
			}
			for(boolean digitFound : digitsHorizontal){
				if(!digitFound){
					return false;
				}
			}
			for(boolean digitFound : digitsVertical){
				if(!digitFound){
					return false;
				}
			}
		}
		for(int cornerX = 0; cornerX < 7; cornerX += 3){
			for(int cornerY = 0; cornerY < 7; cornerY += 3){
				boolean[] digits = new boolean[9];
				for(int cellX = 0; cellX < 3; cellX++){
					for(int cellY = 0; cellY < 3; cellY++){
						digits[this.getDigit(cornerX + cellX, cornerY + cellY) - 1] = true;
					}
				}
				for(boolean digitFound : digits){
					if(!digitFound){
						return false;
					}
				}
			}
		}
		return true;
	}
}