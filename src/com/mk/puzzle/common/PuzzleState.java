package com.mk.puzzle.common;

import java.util.List;

public interface PuzzleState
{
	List<PuzzleMove> getAvailableMoves();
	void applyMove(PuzzleMove move);
	void takeBack(PuzzleMove move);
	boolean isSolved();
	PuzzleState clone();
}
