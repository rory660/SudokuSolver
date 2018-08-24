package sudokuSolver;

import java.util.Arrays;
import sudokuSolver.Sudoku;

import sudokuSolver.SudokuInterface;

public class SudokuSolve{
	private Sudoku sudoku;
	private boolean[][][] possibilities;

	public SudokuSolve(){}

	public Sudoku solve(Sudoku sudoku){
		this.sudoku = sudoku;
		this.possibilities = new boolean[9][9][9];
		for(boolean[][] possRow : this.possibilities){
			for(boolean[] possColumn : possRow){
				Arrays.fill(possColumn, true);
			}
		}
		
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
			changing = changing || this.solveRowsIfPossible();
			changing = changing || this.solveColumnsIfPossible();
			changing = changing || this.solveCellsIfPossible();
			// changing = changing || this.applyInlineWithinCellTrick();
		}
		return this.finishSolveBruteForce(sudoku);
	}

	private Sudoku finishSolveBruteForce(Sudoku sudoku){
		this.sudoku = sudoku;
		Sudoku initialSudoku = new Sudoku(sudoku);
		boolean[][][] initialPossibilities = new boolean[9][9][9];
		for(int x = 0; x < 9; x ++){
			for(int y = 0; y < 9; y ++){
				for(int digit = 0; digit < 9; digit ++){
					initialPossibilities[x][y][digit] = this.possibilities[x][y][digit];
				}
			}
		}
		for(int x = 0; x < 9; x++){
			for(int y = 0; y < 9; y++){
				if(sudoku.getDigit(x, y) == -1){
					for(int digit = 1; digit < 10; digit++){
						if(this.possibilities[x][y][digit - 1]){
							sudoku.setDigit(x, y, digit);
							boolean changing = true;
							while(changing){
								changing = false;
								for(int y2 = 0; y2 < 9; y2++){
									for(int x2 = 0; x2 < 9; x2++){
										if(this.sudoku.getDigit(x2,y2) == -1){
											if(this.solveDigitIfPossible(x2,y2)){
												changing = true;
											}
										}
									}
								}
								changing = changing || this.solveRowsIfPossible();
								changing = changing || this.solveColumnsIfPossible();
								changing = changing || this.solveCellsIfPossible();
							}
							
							if(!sudoku.isFilled()){
								sudoku = finishSolveBruteForce(sudoku);
							}
							if(sudoku.isSolved()){
								return sudoku;
							}
							else{
								sudoku = new Sudoku(initialSudoku);
								for(int x1 = 0; x1 < 9; x1 ++){
									for(int y1 = 0; y1 < 9; y1 ++){
										for(int digit1 = 0; digit1 < 9; digit1 ++){
											this.possibilities[x1][y1][digit1] = initialPossibilities[x1][y1][digit1];
										}
									}
								}
							}
						}
					}
					return sudoku;
				}
			}
		}
		return sudoku;
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
		this.possibilities[x][y] = this.mergePossiblities(this.possibilities[x][y], possibleDigits);
		if(totalPossible == 1){
			this.sudoku.setDigit(x,y, possibleDigit);
			return true;
		}
		return false;
	}

	private boolean solveRowsIfPossible(){
		boolean anythingChanged = false;
		for(int y = 0; y < 9; y++){
			for(int digit = 1; digit < 10; digit++){
				int possibleX = -1;
				for(int x = 0; x < 9; x++){
					if(this.sudoku.getDigit(x,y) == -1){
						if(this.possibilities[x][y][digit - 1]){
							if(possibleX == -1){
								possibleX = x;
							}
							else{
								possibleX = -1;
								break;
							}
						}
					}
				}
				if(possibleX != -1){
					this.sudoku.setDigit(possibleX, y, digit);
					anythingChanged = true;
				}
			}
		}
		return anythingChanged;
	}

	private boolean solveColumnsIfPossible(){
		boolean anythingChanged = false;
		for(int x = 0; x < 9; x++){
			for(int digit = 1; digit < 10; digit++){
				int possibleY = -1;
				for(int y = 0; y < 9; y++){
					if(this.sudoku.getDigit(x,y) == -1){
						if(this.possibilities[x][y][digit - 1]){
							if(possibleY == -1){
								possibleY = y;
							}
							else{
								possibleY = -1;
								break;
							}
						}
					}
				}
				if(possibleY != -1){
					this.sudoku.setDigit(x, possibleY, digit);
					anythingChanged = true;
				}
			}
		}
		return anythingChanged;
	}

	private boolean solveCellsIfPossible(){
		boolean anythingChanged = false;
		for(int cornerX = 0; cornerX < 7; cornerX+=3){
			for(int cornerY = 0; cornerY < 7; cornerY+=3){
				for(int digit = 1; digit < 10; digit++){
					int[] possibleCoords = {-1,-1};
					for(int cellX = 0; cellX < 3; cellX++){
						boolean breakFlag = false;
						for(int cellY = 0; cellY < 3; cellY++){
							int x = cornerX + cellX;
							int y = cornerY + cellY;
							if(this.sudoku.getDigit(x,y) == -1){
								if(this.possibilities[x][y][digit - 1]){
									if(possibleCoords[0] == -1){
										possibleCoords[0] = x;
										possibleCoords[1] = y;
									}
									else{
										possibleCoords[0] = -1;
										possibleCoords[1] = -1;
										breakFlag = true;
										break;
									}
								}
							}
						}
						if(breakFlag){
							break;
						}
					}
					if(possibleCoords[0] != -1){

						this.sudoku.setDigit(possibleCoords[0], possibleCoords[1], digit);
						anythingChanged = true;
					}
				}
			}
		}
		return anythingChanged;
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

	private boolean applyInlineWithinCellTrick(){
		boolean anythingChanged = false;
		for(int cornerX = 0; cornerX < 7; cornerX+=3){
			for(int cornerY = 0; cornerY < 7; cornerY+=3){
				for(int digit = 1; digit < 10; digit++){
					int lineX = -1;
					int lineY = -1;
					for(int cellX = 0; cellX < 3; cellX++){
						for(int cellY = 0; cellY < 3; cellY++){
							int x = cornerX + cellX;
							int y = cornerY + cellY;
							if(this.sudoku.getDigit(x,y) == -1){
								if(this.possibilities[x][y][digit - 1]){
									if(lineX == -1){
										lineX = x;
									}
									else if(lineX != x){
										lineX = -2;
									}
									if(lineY == -1){
										lineY = y;
									}
									else if(lineY != y){
										lineY = -2;
									}
								}
							}
						}
					}
					
					if(lineX > -1){
						boolean onlyPossibleCellsInLine = true;
						for(int y = 0; y < 9; y++){
							if(y < cornerY || y > cornerY + 3){
								if(this.sudoku.getDigit(lineX, y) == -1 && this.possibilities[lineX][y][digit - 1]){
									this.possibilities[lineX][y][digit - 1] = false;
									onlyPossibleCellsInLine = false;
									anythingChanged = true;
								}
							}
						}
						if(onlyPossibleCellsInLine){
							for(int cellX = 0; cellX < 3; cellX++){
								if(cellX + cornerX != lineX){
									for(int cellY = 0; cellY < 3; cellY++){
										if(this.sudoku.getDigit(cornerX + cellX, cornerY + cellY) == -1 && this.possibilities[cornerX + cellX][cornerY + cellY][digit - 1]){
											this.possibilities[cornerX + cellX][cornerY + cellY][digit - 1] = false;
											anythingChanged = true;
										}
									}
								}
							}
						}
					}
					else if(lineY > -1){
						boolean onlyPossibleCellsInLine = true;
						for(int x = 0; x < 9; x++){
							if(x < cornerX || x > cornerX + 3){
								if(this.sudoku.getDigit(x, lineY) == -1 && this.possibilities[x][lineY][digit - 1]){
									this.possibilities[x][lineY][digit - 1] = false;
									onlyPossibleCellsInLine = false;
									anythingChanged = true;
								}
							}
						}
						if(onlyPossibleCellsInLine){
							for(int cellY = 0; cellY < 3; cellY++){
								if(cellY + cornerY != lineY){
									for(int cellX = 0; cellX < 3; cellX++){
										if(this.sudoku.getDigit(cornerX + cellX, cornerY + cellY) == -1 && this.possibilities[cornerX + cellX][cornerY + cellY][digit - 1]){
											this.possibilities[cornerX + cellX][cornerY + cellY][digit - 1] = false;
											anythingChanged = true;
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return anythingChanged;
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
