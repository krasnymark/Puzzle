package com.mk.puzzle.common;

import org.apache.log4j.Logger;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Stack;

public class PuzzleSolver
{
	private final static Logger logger = Logger.getLogger(PuzzleSolver.class);
	public final static String[] DELIMITERS = {"\t", "|", "~", "^", "!", "#", ";", ":", ",", "="};
	public final static DecimalFormat formatter = new DecimalFormat("00");
	private Puzzle puzzle;
	private long logInterval = 100000;
	private long startTime;
	private Stack <PuzzleMove> moves = new Stack<PuzzleMove>();
	private int moveNumber = 0;
	private int moveCount = 0;

	public void solve()
    {
        logger.info("PuzzleSolver Time Limit: " + getTimeLimit());
        puzzle.load();
        boolean done = false;
        while (!done)
        {
            puzzle.init();
            setStartTime();
            done = puzzle.getMethod().equalsIgnoreCase("Clone") ? solveClone(puzzle.getState()) : solveInPlace(puzzle.getState());
        }
        StringBuilder sb = new StringBuilder();
		sb.append("Solved in: " + getElapsedMs() + " ms \n" + "Moves:");
		moves.forEach(move -> sb.append("\n\t" + formatter.format(++moveCount) + " - " + move));
		logger.info(sb);
        logger.info(puzzle.getState());
    }

	private boolean solveClone(PuzzleState state)
	{
		if (state.isSolved())
		{
			puzzle.setState(state);
			return true;
		}
		List <PuzzleMove> availableMoves = state.getAvailableMoves();
		if (availableMoves.size() == 0) return false;
		for (PuzzleMove move : availableMoves)
		{
			PuzzleState newState = state.clone();
			newState.applyMove(move);
			moves.push(move);
			logger.debug(move + "\n" + newState);
			if (solveClone(newState)) return true;
			moves.pop();
			if (getTimeLimit() > 0 && getElapsedSec() > getTimeLimit())
			{
				logger.debug("Over time limit" + newState);
				return false;
			}
		}
		return false;
	}

	private boolean solveInPlace(PuzzleState state)
	{
		if (state.isSolved())
		{
			puzzle.setState(state);
			return true;
		}
		List <PuzzleMove> availableMoves = state.getAvailableMoves();
        logger.debug("availableMoves: " + availableMoves.size() + " - " + availableMoves);
		for (PuzzleMove move : availableMoves)
		{
			applyMove(state, move);
//			logger.debug(move + "" + state);
			if (solveInPlace(state)) return true;
            takeBack(state, move);
			if (getTimeLimit() > 0 && getElapsedSec() > getTimeLimit())
			{
				logger.debug("Over time limit" + state);
				return false;
			}
			if (isDeadEnd(state, move))
			{
				logger.info("DeadEnd: " + moveNumber + " depth: " + moves.size() + " move: " + move + ": " + state);
				break;
			}
		}
		return false;
	}

	private boolean isDeadEnd(PuzzleState state, PuzzleMove move)
	{
		return move.isDeadEnd(state);
	}

	private void applyMove(PuzzleState state, PuzzleMove move)
	{
		moveNumber++;
		state.applyMove(move);
		moves.push(move);
		if (moveNumber % logInterval == 0)
			logger.info("applyMove: " + moveNumber + " depth: " + moves.size() + " move: " + move + ": " + state);
		else
			logger.debug("applyMove: " + moveNumber + " depth: " + moves.size() + " move: " + move + ": " + state);
	}

    private void takeBack(PuzzleState state, PuzzleMove move)
    {
        moves.pop();
        state.takeBack(move);
		if (moveNumber % logInterval == 0)
        	logger.info("takeBack: " + moveNumber + " depth: " + moves.size() + " move: " + move);
		else
			logger.debug("takeBack: " + moveNumber + " depth: " + moves.size() + " move: " + move);
    }

	public Puzzle getPuzzle()
	{
		return puzzle;
	}

	public void setPuzzle(Puzzle puzzle)
	{
		this.puzzle = puzzle;
	}

	private long getTimeLimit()
	{
		return puzzle.getTimeLimit();
	}

	private long getElapsedSec()
	{
		return getElapsedMs() / 1000;
	}

	private long getElapsedMs()
	{
		return (System.currentTimeMillis() - this.startTime); // / 1000;
	}

	private void setStartTime()
	{
		this.startTime = System.currentTimeMillis();
	}
}
