package com.mk.puzzle.sudoku;

import com.mk.puzzle.common.Puzzle;
import com.mk.puzzle.common.PuzzleState;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SudokuPuzzle implements Puzzle
{
    private final static Logger logger = Logger.getLogger(SudokuPuzzle.class);
    private SudokuState state;
    private String fileName;
    @Override
    public void load()
    {
        try
        {
            state = new SudokuState();
            SudokuBoard board = ((SudokuState) getState()).getBoard();
            Scanner scanner = new Scanner(new File(getFileName()));
            List<String> lines = new ArrayList<String>();
            int y = 1;
            while (scanner.hasNext())
            {
                String line = scanner.nextLine();
                String[] cells = line.split(",");
                for (int x = 0; x < cells.length; x++)
                {
                    int number = cells[x].trim().length() == 0 ? 0 : Integer.valueOf(cells[x]);
                    SudokuCell cell = board.setCell(x + 1, y, number);
                    if (number > 0) cell.setInitial(true);
                }
                y++;
            }
            logger.info(state);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void init()
    {

    }

    @Override
    public PuzzleState getState()
    {
        return state;
    }

    @Override
    public void setState(PuzzleState state)
    {

    }

    @Override
    public int getTimeLimit()
    {
        return 0;
    }

    @Override
    public String getMethod()
    {
        return "InPlace";
    }

    public String getFileName()
    {
        return fileName;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }
}
