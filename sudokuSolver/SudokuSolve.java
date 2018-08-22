package sudokuSolver;

import java.util.Arrays;
import sudokuSolver.Sudoku;

public class SudokuSolve{
	private Sudoku sudoku;

	public SudokuSolve(){}

	public Sudoku solve(Sudoku sudoku){
		this.sudoku = sudoku;
		boolean changing = true;
		while(changing && !sudoku.isFilled()){
			changing = false;
			for(int y = 0; y < 9; y++){
				for(int x = 0; x < 9; x++){
					if(this.sudoku.getDigit(x,y) == -1){
						if(this.solveDigitIfPossible(x,y)){
							changing = true;
						}
					}
				}
			}
		}
		
		return this.sudoku;
	}

	private boolean solveDigitIfPossible(int x,int y){
		boolean[] possibleDigits = new boolean[9];
		Arrays.fill(possibleDigits, true);

		boolean[] resultPossibleDigits = this.checkOrthogonals(x,y);
		possibleDigits = this.mergePossiblities(possibleDigits, resultPossibleDigits);

		resultPossibleDigits = this.checkCells(x,y);
		possibleDigits = this.mergePossiblities(possibleDigits, resultPossibleDigits);

		int totalPossible = 0;
		int possibleDigit = -1;
		int i = 0;
		for(boolean possible : possibleDigits){
			i++;
			if(possible){
				possibleDigit = i;
				totalPossible++;
			}
		}
		if(totalPossible == 1){
			this.sudoku.setDigit(x,y, possibleDigit);
			return true;
		}
		return false;
	}

	private boolean[] checkOrthogonals(int x, int y){
		boolean[] possibleDigits = new boolean[9];
		Arrays.fill(possibleDigits, true);
		int digit = -1;
		for(int linearIndex = 0; linearIndex < 9; linearIndex ++){
			digit = this.sudoku.getDigit(linearIndex, y);
			if(digit != -1){
				possibleDigits[digit - 1] = false;
			}
			
			digit = this.sudoku.getDigit(x, linearIndex);
			if(digit != -1){
				possibleDigits[digit - 1] = false;
			}
			
		}
		return possibleDigits;
	}

	private boolean[] checkCells(int x, int y){
		int topCornerX = x / 3 * 3;
		int topCornerY = y / 3 * 3;
		boolean[] possibleDigits = new boolean[9];
		Arrays.fill(possibleDigits, true);
		for(int cellX = 0; cellX < 3; cellX++){
			for(int cellY = 0; cellY < 3; cellY++){
				int digit = this.sudoku.getDigit(topCornerX + cellX, topCornerY + cellY);
				if(digit != -1){
					possibleDigits[digit - 1] = false;
				}
			}
		}
		return possibleDigits;
	}

	private boolean[] mergePossiblities(boolean[] array1, boolean[] array2){
		int i = 0;
		for(boolean b : array2){
			if(!b){
				array1[i] = false;
			}
			i ++;
		}
		return array1;
	}
	
}
