package Sudoku;

import javax.swing.*;

public class SudokuBoard {

    /**
     * A Sudoku board generator, generate a Sudoku board.
     */

    // board.length = ROWS x COLS, 0 stands for empty cell
    int [] board;
    // BOXES.length = ROWS x COLS, 1-9 stands for different boxes
    final int [] BOXES;
    final int ROWS;
    final int COLS;

    // 3 basic attribute of the Sudoku board: length, width and boxes
    public SudokuBoard(int rows, int cols, int [] boxes) {
        this.ROWS = rows;
        this.COLS = cols;
        this.BOXES = boxes;
        this.board = new int [ROWS*COLS];
    }

    public SudokuBoard(int [] boxes) {
        this.ROWS = 9;
        this.COLS = 9;
        this.BOXES = boxes;
        this.board = new int [ROWS*COLS];
    }

    public SudokuBoard() {
        this.ROWS = 9;
        this.COLS = 9;
        this.BOXES = _standardBoxes();
        this.board = new int [ROWS*COLS];
    }

    // standard 3x3 boxes
    private int [] _standardBoxes() {
        int [] _standardBoxes = new int[]
                {1,1,1,2,2,2,3,3,3,1,1,1,2,2,2,3,3,3,1,1,1,2,2,2,3,3,3,
                4,4,4,5,5,5,6,6,6,4,4,4,5,5,5,6,6,6,4,4,4,5,5,5,6,6,6,
                7,7,7,8,8,8,9,9,9,7,7,7,8,8,8,9,9,9,7,7,7,8,8,8,9,9,9};
        return _standardBoxes;
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
