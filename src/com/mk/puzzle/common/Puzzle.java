package com.mk.puzzle.common;


/**
 * @author MK
 */
public interface Puzzle
{
    void load();

    void init();

    PuzzleState getState();

    void setState(PuzzleState state);

    int getTimeLimit();

    String getMethod();
}
