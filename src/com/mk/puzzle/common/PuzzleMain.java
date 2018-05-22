package com.mk.puzzle.common;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author MK
 *
 */
public class PuzzleMain
{
	private PuzzleSolver solver;

	/**
	 * @param args - Spring context
	 */
	public static void main(String[] args)
	{
		if (args.length > 0)
		{
			ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(args[0]);
			PuzzleSolver solver = (PuzzleSolver) context.getBean("solver");
			solver.solve();
		}
	}

	public void setSolver(PuzzleSolver solver)
	{
		this.solver = solver;
	}
}
