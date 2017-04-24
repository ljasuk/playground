import java.util.*;

class ttt {
	
	public static boolean[][] makeboard(boolean[][] board)
	{
		for (boolean[] a : board)
		{
			Arrays.fill(a, false);
		}
		
		int x =(int) (Math.random()*board[0].length);
		int y =(int) (Math.random()*board.length);
		System.err.println("x: "+x+" y: "+y);
		board[y][x] = true;
		
		x =(int) (Math.random()*board[0].length);
		y =(int) (Math.random()*board.length);
		board[y][x] = true;
		
		return board;
	}
	
	public static void drawboard(boolean[][] board, int x, int y)
	{
		
		for (int i = 0; i < board.length; i++)
		{
			System.out.print("[");
			for (int j = 0; j < board[i].length; j++)
				if ((i==y)&&(j==x)) System.out.print("X"); else
				if (board[i][j]) System.out.print("0"); else System.out.print("_"); 
			System.out.print("]\n");
		}
		System.out.print("\n\n");
	}
	
	public static int[][] seek(int x, int y, boolean[][] nemjo,int[][] ut, int celX, int celY)
	{
		drawboard(nemjo, x, y);
		//System.err.println("X: "+x+" Y: "+y);
		int[][] valasz = new int[ut.length+1][2];
		for (int i=0;i<ut.length;i++)
		{
			valasz[i][0] = ut[i][0];
			
			valasz[i][1] = ut[i][1];
			
			//System.err.println("valasz: "+Arrays.toString(valasz[i]));
		}
		valasz[ut.length][0]=x;
		valasz[ut.length][1]=y;		
		nemjo[y][x]=true;
		if ((celX==x)&&(celY==y)) return valasz;
		int[][] rossz = new int[1][1];
		rossz[0][0] = 0;
		
		
		for (int i=0;i<=7;i++) {
			//System.err.println(i);
			switch (i){
			case 0:
				if (y-1<0) continue;
				if (nemjo[y-1][x]==true) continue;
				//if ((celX==x)&&(celY==y-1)) return valasz;
				int[][] ami = seek(x, y-1, nemjo, valasz, celX, celY);
				if (Arrays.equals((ami), rossz)) continue; else return ami;
				
				//break;
			case 1:
				if (y-1<0) continue;				
				if (x+1>=nemjo[y].length) continue;
				if (nemjo[y-1][x+1]==true) continue;
				//if ((celX==x+1)&&(celY==y-1)) return valasz;
				int[][] bmi = seek(x+1, y-1, nemjo, valasz, celX, celY);
				if (Arrays.equals((bmi), rossz)) continue; else return bmi;
				
				//break;
			case 2: 				
				if (x+1>=nemjo[y].length) continue;
				if (nemjo[y][x+1]==true) continue;
				//if ((celX==x+1)&&(celY==y)) return valasz;
				int[][] cmi = seek(x+1, y, nemjo, valasz, celX, celY);
				if (Arrays.equals(cmi, rossz)) continue; else return cmi;
				//break;
			case 3: 				
				if (x+1>=nemjo[y].length) continue;
				if (y+1>=nemjo.length) continue;
				if (nemjo[y+1][x+1]==true) continue;
				//if ((celX==x+1)&&(celY==y+1)) return valasz;
				int[][] dmi = seek(x+1, y+1, nemjo, valasz, celX, celY);
				if (Arrays.equals(dmi, rossz)) continue; else return dmi;
				//break;
			case 4: 					
				if (y+1>=nemjo.length) continue;
				if (nemjo[y+1][x]==true) continue;
				//if ((celX==x)&&(celY==y+1)) return valasz;
				int[][] emi =seek(x, y+1, nemjo, valasz, celX, celY);
				if (Arrays.equals((emi), rossz)) continue; else return emi;
				//break;
			case 5:
				if (x-1<0) continue;
				if (y+1>=nemjo.length) continue;
				if (nemjo[y+1][x-1]==true) continue;
				//if ((celX==x-1)&&(celY==y+1)) return valasz;
				int[][] fmi = seek(x-1, y+1, nemjo, valasz, celX, celY);
				if (Arrays.equals((fmi), rossz)) continue; else return fmi;
				//break;
			case 6:
				if (x-1<0) continue;
				if (nemjo[y][x-1]==true) continue;
				
				//System.err.println("X-1: "+(x-1)+" Y: "+y);
				//System.err.println("celX: "+celX+" celY: "+celY);
				/*if ((celX==x-1)&&(celY==y)) 
					{
					System.err.println("itt vagyunk");
					return valasz;
					}*/
				int[][] gmi = seek(x-1, y, nemjo, valasz, celX, celY);
				if (Arrays.equals((gmi), rossz)) break; else return gmi;
				//break;
			case 7:
				if (x-1<0) continue;
				if (y-1<0) continue;
				if (nemjo[y-1][x-1]==true) continue;
				//if ((celX==x-1)&&(celY==y-1)) return valasz;
				int[][] hmi = seek(x-1, y-1, nemjo, valasz, celX, celY);
				if (Arrays.equals((hmi), rossz)) continue;else return hmi;
				//break;
			case 8: 
				System.err.println("itt a baj");
				return rossz;
				
			}
				
		}
		System.err.println("valasz.length: "+valasz.length);
		return valasz;
		
		/*rounds+=1;
		if (rounds<7) return valasz; else return {{0, 0},{0, 0}};*/
		//return valasz;
	}
	
	public static void main (String args[]) 
	{	
		
		Scanner inpa = new Scanner(System.in);
		System.out.print("X: ");
		int x = inpa.nextInt();
		System.out.print("Y: ");
		int y = inpa.nextInt();
		System.out.print("celX: ");
		int celX = inpa.nextInt();
		System.out.print("celY: ");
		int celY = inpa.nextInt();
		System.out.print("boardX: ");
		int boardX = inpa.nextInt();
		System.out.print("boardY: ");
		int boardY = inpa.nextInt();
		boolean[][] v = new boolean[boardY][boardX];
		v = makeboard(v);
		v[y][x] = false;
		/*for (boolean[] a : v)
		{
			System.out.println(Arrays.toString(a));
		}*/
		int[][] ut = new int[0][2];
		int[][] solution = seek(x, y, v, ut, celX, celY);
		//solution = seek(x, y, v, ut, 2, 3);
		//System.out.println(Arrays.toString(seek(x, y, v, ut, 2, 3)));
		//System.out.println(Arrays.toString(solution));
		//seek(x, y, v, ut, 2, 3);
		
		for (int[] r : solution)
		{
			System.out.println(Arrays.toString(r));
		}
	}
	
}