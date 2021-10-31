package Sudoku;

import java.util.ArrayList;
import java.util.LinkedList;

public class SudokuSolver {

    /**
     * Solve a Sudoku puzzle!
     * input: SudokuBoard board (int [])
     */

    // board.length = ROWS x COLS, 0 stands for empty cell
    int [] board;
    private int [] binaryBoard;
    private final int ROWS;
    private final int COLS;
    private final int [] BINARY;
    private final int [] REGIONS;

    // region-id --- region-member
    private final ArrayList<ArrayList<Integer>> MYREGION;

    // stacks, saving the board situation and searching process.
    private LinkedList<int []> boardStack;
    private LinkedList<Integer> minCellStack;
    private LinkedList<Integer> idxStack;


    public SudokuSolver(SudokuBoard board) {
        this.ROWS = board.ROWS;
        this.COLS = board.COLS;
        this.REGIONS = board.REGIONS;
        // int [10], loc 1-9 means digit 1-9, loc 0 means all possible digits
        this.BINARY = new int [] {511,1,2,4,8,16,32,64,128,256};

        this.boardStack = new LinkedList<>();
        this.minCellStack = new LinkedList<>();
        this.idxStack = new LinkedList<>();

        this.board = board.board;

        // generate MYREGION; region-id --- region-member
        this.MYREGION = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            this.MYREGION.add(new ArrayList<>());
        }
        for (int i = 0; i < this.REGIONS.length; i++) {
            this.MYREGION.get(this.REGIONS[i]-1).add(i);
        }

        this.binaryBoard = new int [this.ROWS*this.COLS];
        _generateBinaryBoard();
    }


    // turn a Sudoku board into a binary board, a binary shows all possible numbers.
    // negative numbers mean confirmed.
    private void _generateBinaryBoard() {
        for (int i = 0; i < this.binaryBoard.length; i++) {
            this.binaryBoard[i] = this.BINARY[0];
        }
        for (int i = 0; i < this.binaryBoard.length; i++) {
            if (this.board[i] > 0) {
                this.binaryBoard[i] = -this.BINARY[this.board[i]];
                _reduce(i);
            }
        }
    }


    // remove a possible number in a cell in the binaryBoard.
    private void _removeNum(int i, int num) {
        this.binaryBoard[i] = this.binaryBoard[i] & (this.BINARY[0] - num);
    }


    // set a confirmed number (negative) for a cell
    private void _setNum(int i, int num) {
        this.binaryBoard[i] = -num;
        _reduce(i);
    }
    private void _setNum(int i) {
        this.binaryBoard[i] = -this.binaryBoard[i];
        _reduce(i);
    }


    // count the number of possibilities for a cell, -1 means confirmed, 0 means no solution
    private int _countPossibilities(int i) {
        int value = this.binaryBoard[i];
        if (value < 0) {
            return -1;
        }

        int count = 0;
        while (value > 0) {
            value = value & (value - 1);
            count++;
        }
        return count;
    }


    // find the cell with th minimal possibilities.
    // if a cell has only one possibility, just set it.
    // return cell loc, or -1 (solved), -2 (no solution)
    private int findMinCell() {
        // a big number
        int m = 20;

        int minCell = 0;
        int count;
        int emptyCellNum = this.binaryBoard.length;

        for (int i=0; i<this.binaryBoard.length; i++) {
            count = _countPossibilities(i);
            // already set
            if (count == -1) {
                emptyCellNum--;
            }
            // no solution
            else if (count == 0) {
                return -2;
            }
            // only one possibility, just set it
            else if (count == 1) {
                emptyCellNum--;
                _setNum(i);
            }
            else if (count < m) {
                m = count;
                minCell = i;
            }
        }


        // solved
        if (emptyCellNum==0) {
            return -1;
        }
        return minCell;
    }


    // reduce the possibilities of the related (the same row/col/region) cells
    private void _reduce(int i) {
        int row = i / this.COLS;
        int col = i % this.COLS;
        int reduceNum = -this.binaryBoard[i];

        // the same row
        for (int j = row*this.COLS; j < (row+1)*this.COLS; j++) {
            if ((j != i) && (this.binaryBoard[j] > 0)) {
                _removeNum(j, reduceNum);
            }
        }

        // the same col
        for (int j = col; j < col+this.ROWS*this.COLS; j += this.COLS) {
            if ((j != i) && (this.binaryBoard[j] > 0)) {
                _removeNum(j, reduceNum);
            }
        }

        // the same region
        for (int j:this.MYREGION.get(this.REGIONS[i]-1)) {
            if ((j != i) && (this.binaryBoard[j] > 0)) {
                _removeNum(j, reduceNum);
            }
        }
    }


    // main loop, search for the answer.
    public void search() {
        int minCell = findMinCell();
        // attention. idx starts from 1.
        int idx = 1;

        // attention: clone()
        this.boardStack.add(this.binaryBoard.clone());
        this.minCellStack.add(minCell);
        this.idxStack.add(idx);

        while (true) {
            // check the stacks
            if (this.minCellStack.size()==0) {
                System.out.println("The puzzle has no solution.");
                return;
            }

            // pop
            this.binaryBoard = this.boardStack.removeLast().clone();
            minCell = this.minCellStack.removeLast();
            idx = this.idxStack.removeLast();

            // no solution
            if (minCell == -2) {
                continue;
            }

            // solved
            if (minCell == -1) {
                _generateAnswer();
                System.out.println("Puzzle solved.");
                printBoard();
                return;
            }
            // try a number
            else {
                int tempNum = _findNum(minCell, idx);

                // the cell check over
                if (tempNum == -1) {
                    continue;
                }

                // push the old situation with idx+1
                this.boardStack.add(this.binaryBoard.clone());
                this.minCellStack.add(minCell);
                this.idxStack.add(idx+1);

                // try a number and find the next minimal cell
                _setNum(minCell, tempNum);
                minCell = findMinCell();
                // push the next situation
                this.boardStack.add(this.binaryBoard.clone());
                this.minCellStack.add(minCell);
                this.idxStack.add(1);
            }
        }
    }


    // find the number according to the cell and idx
    // initial idx >= 1
    private int _findNum(int cell, int idx) {
        int value = this.binaryBoard[cell];

        for (int j = 1; j < 10; j++) {
            if ((value & this.BINARY[j]) != 0) {
                idx--;
                // found
                if (idx == 0) {
                    return this.BINARY[j];
                }
            }
        }
        // idx Overflow
        return -1;
    }


    // turn binaryBoard into real digit board
    private void _generateAnswer() {
        for (int i = 0; i < this.binaryBoard.length; i++) {
            if (this.board[i] != 0) {
                continue;
            }
            switch (-this.binaryBoard[i]) {
                case 1 -> this.board[i] = 1;
                case 2 -> this.board[i] = 2;
                case 4 -> this.board[i] = 3;
                case 8 -> this.board[i] = 4;
                case 16 -> this.board[i] = 5;
                case 32 -> this.board[i] = 6;
                case 64 -> this.board[i] = 7;
                case 128 -> this.board[i] = 8;
                case 256 -> this.board[i] = 9;
                default -> System.out.println("Generate answer Error");
            }
        }
    }


    public void printBoard() {
        System.out.printf("The answer is:\n");

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < this.board.length; i++) {
            stringBuilder.append(this.board[i]+" ");
            if ((i+1) % this.COLS == 0) {
                System.out.printf("Line %d:  ", (i+1) / this.COLS);
                System.out.println(stringBuilder);
                stringBuilder = new StringBuilder();
            }
        }
    }
}
