package main;

public class SolverNaiv
{
	static int dim;
	static int dimS;
	static int max;

	static boolean[][] solutie;
	static int[][] careu;

	public SolverNaiv(int n)
	{
		dim = n;
		dimS = dim + 4;
		max = 0;

		solutie = new boolean[dim][dim];
		careu = new int[dimS][dimS];
		
		for(int i = 0; i < dimS; i ++)
			for(int j = 0; j < dimS; j++)
				careu[i][j] = 0;
	}
	
	boolean[][] getTable()
	{
		return solutie;
	}
	
	void algoritm()
	{
		while(aflaN() != dim * dim)
		{
			cautaUrmatorul();
			if(verifica())
			{
				max = aflaN();
				for(int i = 2; i < dim + 2; i++)
					for(int j = 2; j < dim + 2; j++)
						if(careu[i][j] == 1)
							solutie[i-2][j-2] = true;
						else
							solutie[i-2][j-2] = false;
			}
		}
	}

	int aflaN()
	{
		int n = 0;
		for(int i = 0; i < dimS; i ++)
			for(int j = 0; j < dimS; j++)
				n = n + careu[i][j];

		return n;
	}

	void cautaUrmatorul()
	{
		int[] aux = new int[dim * dim];

		for(int i = 0; i < dim; i++)
			for(int j = 0; j < dim; j++)
				aux[dim * i + j] = careu[i + 2][j + 2];

		boolean rest = true;
		for(int i = 0; i < dim * dim; i++)
		{
			if(aux[i] == 0 && rest)
			{
				aux[i] = 1;
				rest = false;
			}

			else 
				if(aux[i] == 1 && rest)
					aux[i] = 0;
		}
		
		for(int i = 0; i < dim; i++)
			for(int j = 0; j < dim; j++)
				careu[i + 2][j + 2] = aux[dim * i + j];
	}

	boolean verifica()
	{
		if(aflaN() >= (dim * dim) / 2 && aflaN() <= (dim * dim + 2 * dim + 1) / 2 && max <= aflaN())
		{
			for(int i = 1; i < dim + 3; i++)
				for(int j = 1; j < dim + 3; j++)
				{
					int sum = careu[i-1][j-1] + careu[i-1][j] + careu[i-1][j+1] +
							careu[i][j-1] + careu[i][j+1] +
							careu[i+1][j-1] + careu[i+1][j] + careu[i+1][j+1];
					
					if((careu[i][j] == 1 && (sum < 2 || sum > 3)) || (careu[i][j] == 0 && sum == 3))
						return false;
				}
			return true;
		}
		return false;
	}
}