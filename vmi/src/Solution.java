import java.util.Scanner;


final class Alphabet{
    final Letter[] alphabet = new Letter[26];
    final int letterWidth;
    final int letterHeight;
    
    
    Alphabet(int letterWidth, int letterHeight) {
		this.letterWidth = letterWidth;
		this.letterHeight = letterHeight;
		Scanner cin = new Scanner(System.in);
		
		String[] abc = new String[letterHeight];
		for (int i = 0; i < letterHeight; i++) {
			abc[i] = cin.nextLine();
		}
		
		for (int i = 0; i < 26; i++) {
			String[] lines = new String[letterHeight];
			for (int j = 0; j < letterHeight; j++) {
				lines[j] = abc[j].substring(i*letterWidth, (i+1)*letterWidth-1);
			}
			alphabet[i] = new Letter(lines);
		}
		cin.close();
	}

	final class Letter{
		final private String[] lines;
		
		Letter(String[] l) {
			lines = new String[l.length];
			for (int i = 0; i < l.length; i++) {
				lines[i] = new String(l[i]);
			}
		}
		
		String getLine(int x){
			return new String(lines[x]);
		}
	    
	}
	final class Printer{
		final private String text;
		final private int[] tNum;

		Printer(String text) {
			this.text = text.toLowerCase();
			tNum = new int[text.length()];
			for(int i = 0; i < text.length();i++){
				tNum[i] = ((int)text.getBytes()[i]) - 97;
				System.err.println(tNum);
			}
			
			
		}
		
		
	}
    
}

class Solution {

    public static void main(String args[]) {
       /* Scanner in = new Scanner(System.in);
        int L = in.nextInt();
        int H = in.nextInt();
        in.nextLine();
        String T = in.nextLine().toUpperCase();
        System.err.println(T);
        String ABC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int[] index = new int[T.length()];
        for (int i = 0; i < T.length(); i++) {
            index[i] = ABC.indexOf(T.charAt(i));
            
            if (index[i]==-1)
            {
                index[i]+=27;
            }
            System.err.println(index[i]);
        }
        
        Alphabet.Printer nyomtato = new Alphabet.Printer(T);*/
    	
    	
        
        /*for (int i = 0; i < H; i++) {
            String ROW = in.nextLine();
            for (int j = 0; j < T.length(); j++) {
                System.out.print(ROW.substring(index[j]*L,(index[j]+1)*L));
            }
            System.out.print("\n");
        }*/

        // Write an action using System.out.println()
        // To debug: System.err.println("Debug messages...");

        //System.out.println("answer");
    }
}