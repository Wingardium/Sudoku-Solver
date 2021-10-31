package Sudoku;

import javax.swing.*;

public class SudokuBoard {

    /**
     * A Sudoku board generator, generate a Sudoku board.
     */

    // board.length = ROWS x COLS, 0 stands for empty cell
    int [] board;
    // REGIONS.length = ROWS x COLS, 1-9 stands for different regions
    final int [] REGIONS;
    final int ROWS;
    final int COLS;

    // 3 basic attribute of the Sudoku board: length, width and regions
    public SudokuBoard(int rows, int cols, int [] regions) {
        this.ROWS = rows;
        this.COLS = cols;
        this.REGIONS = regions;
        this.board = new int [ROWS*COLS];
    }

    public SudokuBoard(int [] regions) {
        this.ROWS = 9;
        this.COLS = 9;
        this.REGIONS = regions;
        this.board = new int [ROWS*COLS];
    }

    public SudokuBoard() {
        this.ROWS = 9;
        this.COLS = 9;
        this.REGIONS = _standardRegions();
        this.board = new int [ROWS*COLS];
    }

    // standard 3x3 regions
    private int [] _standardRegions() {
        int [] _standardRegions = new int[]
                {1,1,1,2,2,2,3,3,3,1,1,1,2,2,2,3,3,3,1,1,1,2,2,2,3,3,3,
                4,4,4,5,5,5,6,6,6,4,4,4,5,5,5,6,6,6,4,4,4,5,5,5,6,6,6,
                7,7,7,8,8,8,9,9,9,7,7,7,8,8,8,9,9,9,7,7,7,8,8,8,9,9,9};
        return _standardRegions;
    }

    // initialize the board manually
    public void setLine(int row, int...line) {
        try {
            for (int i = 0; i < this.COLS; i++) {
                this.board[(row-1)*this.COLS+i] = line[i];
            }
        }
        catch (Exception e){
            System.out.println("Line input ERROR!");
        }
    }
}
