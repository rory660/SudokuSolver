package sudokuSolver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import sudokuSolver.Sudoku;

public class SudokuIO{

	public SudokuIO(){
	}

	public Sudoku getSudokuFromFile(String filepath){
		int[] digits = new int[81];
		try(BufferedReader br = new BufferedReader(new FileReader(filepath))){
			for(int i = 0; i < 81; i++){
				if(i % 9 == 0 && i > 0){
					br.read();
					br.read();
				}
				int digit = br.read();
				if(digit < 49 || digit > 57){
					digits[i] = -1;
				}
				else{
					digits[i] = digit - 48;
				}
			}
		}
		catch(IOException e){
			System.out.println(e);
			System.out.println("error reading file");
		}

		return new Sudoku(digits);
	}

	public void writeSudokuToFile(Sudoku sudoku, String filepath){
		int[] digits = sudoku.toIntArray();
		try(BufferedWriter bw = new BufferedWriter(new FileWriter(filepath))){
			for(int i = 0; i < 81; i++){
				if(i % 9 == 0 && i > 0){
					bw.newLine();
				}
				if(digits[i] == -1){
					bw.write("_");
				}
				else{
					bw.write(Integer.toString(digits[i]));
				}
			}
		}
		catch(IOException e){
			System.out.println(e);
			System.out.println("error writing file");
		}
	}
}
