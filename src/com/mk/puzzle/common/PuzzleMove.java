package com.mk.puzzle.common;

public interface PuzzleMove
{
	String getKey();

    boolean isDeadEnd(PuzzleState state);
}
