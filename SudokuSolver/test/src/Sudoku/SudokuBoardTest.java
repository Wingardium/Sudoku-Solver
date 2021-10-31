package Sudoku;

import javax.swing.*;
import java.util.Arrays;

public class SudokuBoardTest {

    public void setUp() {
        int [] boxes = {1,2,2,2,2,2,2,2,3,
                1,2,4,5,5,5,5,3,3,
                1,2,4,5,5,5,5,5,3,
                1,1,4,4,4,6,6,3,3,
                7,1,1,4,4,6,6,3,3,
                7,1,1,4,6,6,8,3,8,
                7,7,7,4,6,6,8,8,8,
                7,7,7,7,9,6,8,8,8,
                9,9,9,9,9,9,9,9,8};
        SudokuBoard board = new SudokuBoard(boxes);

        board.setLine(1,0,0,0,0,2,0,0,0,0);
        board.setLine(2,0,5,0,9,0,3,0,0,4);
        board.setLine(3,0,0,0,0,8,0,0,4,0);
        board.setLine(4,0,0,0,0,0,0,0,6,5);
        board.setLine(5,0,0,9,0,0,0,8,0,0);
        board.setLine(6,1,6,0,0,0,0,0,0,0);
        board.setLine(7,0,7,0,0,6,0,0,0,0);
        board.setLine(8,3,0,0,8,0,9,0,7,0);
        board.setLine(9,0,0,0,0,5,0,0,0,0);

        System.out.println(Arrays.toString(board.board));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                SudokuBoardTest testBoard = new SudokuBoardTest();
                testBoard.setUp();
            }
        });
    }
}
