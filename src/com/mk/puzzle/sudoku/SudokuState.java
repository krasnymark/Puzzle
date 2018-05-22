package com.mk.puzzle.sudoku;

import com.mk.puzzle.common.PuzzleMove;
import com.mk.puzzle.common.PuzzleState;
import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SudokuState implements PuzzleState
{
    private final static Logger logger = Logger.getLogger(SudokuState.class);
    private SudokuBoard board;

    public SudokuState()
    {
        this(9);
    }

    public SudokuState(int size)
    {
        super();
        board = new SudokuBoard(size);
    }

    public SudokuState(SudokuState state)
    {
        super();
        board = new SudokuBoard(state.getBoard());
    }

    @Override
    public PuzzleState clone()
    {
        return new SudokuState(this);
    }

    @Override
    public String toString()
    {
        return board.toString();
    }

    public SudokuBoard getBoard()
    {
        return board;
    }

    @Override
    public List<PuzzleMove> getAvailableMoves()
    {
        List<PuzzleMove> availableMoves = new ArrayList<>();
        for (int x = 1; x <= board.getSize(); x++)
        for (int y = 1; y <= board.getSize(); y++)
        {
            SudokuCell cell = board.getCell(x, y);
            if (cell.getNumber() > 0) continue;
            List<PuzzleMove> cellMoves = board.getCell(x, y).getAvailableMoves();
            if (CollectionUtils.isEmpty(cellMoves))
            {
                logger.debug("No moves for: " + cell);
                return cellMoves;
            }
            availableMoves.addAll(cellMoves);
        }
        return availableMoves.stream().sorted((left,right) -> left.getKey().compareTo(right.getKey())).collect(Collectors.toList());
    }

    @Override
    public void applyMove(PuzzleMove move)
    {
        SudokuMove sudokuMove = (SudokuMove) move;
        sudokuMove.apply();
    }

    @Override
    public void takeBack(PuzzleMove move)
    {
        SudokuMove sudokuMove = (SudokuMove) move;
        sudokuMove.takeBack();
    }

    @Override
    public boolean isSolved()
    {
        return board.getCells().stream().filter(cell -> cell != null).allMatch(cell -> cell.getNumber() > 0);
    }
}
