package sudokuSolver;
import java.util.Scanner;

import sudokuSolver.Sudoku;

public class SudokuInterface{
	private Scanner scanner;
	public SudokuInterface(){
		scanner = new Scanner(System.in);
	}

	public String getFilenameInput(){
		System.out.print("Enter the filename of the sudoku puzzle to be solved:\n> ");
		return scanner.next();
	}

	public void printSudoku(Sudoku sudoku){
		int[] digits = sudoku.toIntArray();
		int line = 0;
		for(int i = 0; i < 81; i++){
				if(i % 9 == 0 && i > 0){
					System.out.println();
					line++;
					if(line == 3 || line == 6){
						System.out.println("\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588");
					}
				}
				if((i - 3) % 9 == 0 || (i - 6) % 9 == 0){
					System.out.print("\u2588");
				}
				int digit = digits[i];
				if(digit == -1){
					System.out.print("_");
				}
				else{
					System.out.print(Integer.toString(digits[i]));
				}
			}
		System.out.println();
	}
}
