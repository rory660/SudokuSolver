package sudokuSolver;

import sudokuSolver.SudokuInterface;
import sudokuSolver.SudokuIO;
import sudokuSolver.Sudoku;

public class SudokuRun{

	public static void main(String[] args){
		SudokuInterface sudokuInterface = new SudokuInterface();
		SudokuIO sudokuIO = new SudokuIO();
		String inputFilename = sudokuInterface.getFilenameInput();
		System.out.println(inputFilename);
		Sudoku unsolvedSudoku = sudokuIO.getSudokuFromFile(inputFilename);
		sudokuInterface.printSudoku(unsolvedSudoku);
		System.out.println("solving...");
		SudokuSolve solver = new SudokuSolve();
		Sudoku solvedSudoku = solver.solve(unsolvedSudoku);
		sudokuInterface.printSudoku(solvedSudoku);
		sudokuIO.writeSudokuToFile(solvedSudoku, "solved_" + inputFilename);
	}

	
}
