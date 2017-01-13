package main;

import java.util.ArrayList;

import static choco.Choco.*;
import choco.cp.model.CPModel;
import choco.cp.solver.CPSolver;
import choco.kernel.model.variables.integer.IntegerExpressionVariable;
import choco.kernel.model.variables.integer.IntegerVariable;
import choco.kernel.solver.Solver;

public class NewChocoSolver
{
	int dim;
	int dimS;
	boolean[][] table;
	static ArrayList<boolean[][]> solutii;	
	boolean[][] celLibere;
	boolean[][] myTable;
	
	public NewChocoSolver(int n, boolean[][] celuleLibere, boolean[][] myTab)
	{
		solutii = new ArrayList<boolean[][]>();
		dim = n;
		dimS = n + 4;
		table = new boolean[dimS][dimS];
		
		celLibere = celuleLibere;
		myTable = myTab;
	}
	
	public void solve()
	{		
		CPModel m = new CPModel();

		IntegerVariable[][] square = new IntegerVariable[dimS][dimS];
		
		for (int i = 0; i < dimS; i++)
		{
			for (int j = 0; j < dimS; j++)
			{
				if(i == 0 || j == 0 || i == dimS - 1 || j == dimS - 1 || i == 1 || j == 1 || i == dimS - 2 || j == dimS - 2)
					square[i][j] = constant(0);

				else
				{
					if(celLibere[i-2][j-2] == true)
						square[i][j] = constant(0);

					else
						if(myTable[i-2][j-2] == true)
							square[i][j] = constant(1);

						else
							square[i][j] = makeIntVar("var_" + i + "_" + j, 0, 1);

				}
				m.addVariable(square[i][j]);
			}
		}

		for (int i = 0; i < dimS; i++)
		{
			for (int j = 0; j < dimS; j++)
			{
				if(!(i == 0 || j == 0 || i == dimS - 1 || j == dimS - 1))
				{
					IntegerExpressionVariable sum = sum(square[i][j - 1], square[i][j + 1], square[i - 1][j], square[i - 1][j + 1], square[i - 1][j - 1], square[i + 1][j], square[i + 1][j + 1], square[i + 1][j - 1]);

					m.addConstraint(implies(eq(square[i][j], 1), or(eq(sum, 2), eq(sum, 3))));
					m.addConstraint(implies(eq(sum, 3), eq(square[i][j], 1)));
				}
			}
		}
		
		int best = 0, scor = 0;

		Solver s = new CPSolver();
		s.read(m);		
		s.solve();


		for (int i = 0; i < dimS; i++)
			for (int j = 0; j < dimS; j++)
				if(s.getVar(square[i][j]).getVal() == 1)
					scor++;

		if(scor >= best)
		{
			if(scor > best)
			{
				solutii.clear();
				best = scor;
			}
			
			boolean[][] sol = new boolean[dimS][dimS];
			
			for (int i = 0; i < dimS; i++)
			{
				for (int j = 0; j < dimS; j++)
				{
					if(s.getVar(square[i][j]).getVal() == 1)
					{
						table[i][j] = true;
						sol[i][j] = true;
					}
					else
					{
						table[i][j] = false;
						sol[i][j] = false;
					}
				}
			}
			
			solutii.add(sol);			
		}


		while(s.nextSolution())
		{
			scor = 0;
			for (int i = 0; i < dimS; i++)
				for (int j = 0; j < dimS; j++)
					if(s.getVar(square[i][j]).getVal() == 1)
						scor++;
			if(scor >= best)
			{
				if(scor > best)
				{
					solutii.clear();
					best = scor;
				}
				
				boolean[][] sol = new boolean[dimS][dimS];

				for (int i = 0; i < dimS; i++)
				{
					for (int j = 0; j < dimS; j++)
					{
						if(s.getVar(square[i][j]).getVal() == 1)
						{
							table[i][j] = true;
							sol[i][j] = true;
						}
						else
						{
							table[i][j] = false;
							sol[i][j] = false;
						}
					}
				}
//				n1++;
				solutii.add(sol);
			}
		}
//		System.out.println(best);
	}

	public void solutii()
	{
		for(int i = 0; i < solutii.size(); i++)
		{
			for(int j = 0; j < solutii.get(i).length; j++)
			{
				for(int k = 0; k < solutii.get(i).length; k++)
					if(solutii.get(i)[j][k] == true)
						System.out.print("1 ");
					else
						System.out.print("- ");
				System.out.println();
			}
			System.out.println("\n");
		}
	}
	
	public boolean[][] getTable()
	{
		boolean[][] t = new boolean[dim][dim];
		for(int i = 0; i < dim; i++)
			for(int j = 0; j < dim; j++)
				t[i][j] = table[i+2][j+2];
		
//		System.out.println("solutii: ");
//		solutii();
		
		return t;
	}
}