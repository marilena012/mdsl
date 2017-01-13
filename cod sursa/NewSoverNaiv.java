package main;

public class NewSoverNaiv
{
	static int dim;
	static int dimS;
	static int max;
	static int nrCelLib;
	static boolean next = false;

	static boolean[][] solutie;
	static int[][] careu;
	static boolean[][] table;
	static boolean[][] celLibere;

	public NewSoverNaiv(int n, boolean[][] celLib, boolean[][] myTable)
	{
		dim = n;
		dimS = dim + 4;
		max = 0;
		nrCelLib = 0;

		solutie = new boolean[dim][dim];
		careu = new int[dimS][dimS];
		table = myTable;
		celLibere = celLib;
	}
	
	boolean[][] getTable()
	{
		return solutie;
	}
	
	void algoritm()
	{		
		for(int i = 0; i < dim; i ++)
			for(int j = 0; j < dim; j++)
				if(celLibere[i][j])
					nrCelLib++;
				
		for(int i = 0; i < dimS; i ++)
			for(int j = 0; j < dimS; j++)
			{
				if(i > 1 && j > 1 && i <= dim + 1 && j <= dim + 1 && table[i-2][j-2])
					careu[i][j] = 1;
				else
					careu[i][j] = 0;
			}
		
		
		while(aflaN() != dim * dim - nrCelLib)
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
		next = false;
		for(int i = 0; i < dim * dim; i++)
		{
			if(aux[i] == 0 && rest)
			{
				aux[i] = 1;
				rest = false;
				if(celLibere[i / dim][i % dim])
				{
					for(int j = 0; j < i; j++)
						aux[j] = 1;
					next = true;
				}
			}

			else 
				if(aux[i] == 1 && rest)
					aux[i] = 0;
			
			if(table[i / dim][i % dim])
				aux[i] = 1;
		}
		
		for(int i = 0; i < dim; i++)
			for(int j = 0; j < dim; j++)
				careu[i + 2][j + 2] = aux[dim * i + j];
		
		if(next)
			cautaUrmatorul();
	}

	boolean verifica()
	{
		if(max <= aflaN())
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