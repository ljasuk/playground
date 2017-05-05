package hujj;

class ArraysEx {
    private static int sum(int[] numbers) {
        int sum = 0;
        for (int n : numbers) {
            sum+=n;
        }
        return sum;
    }
    
    private static void consume(int[] numbers) {
        int sum = 0;
        for (int n : numbers) {
            sum+=n;
        }
        System.out.println("Sum: " + sum);
        System.out.println("avg: " + ((double)sum) / ((double)numbers.length) );
    }
    
    private static double[] norm(int[] numbers) {
        double[] retVal = new double[numbers.length];
        int sum = 0;
        for (int n : numbers) {
            sum+=n;
        }
        
        for (int i = 0; i < numbers.length; i++) {
            retVal[i] = (double) numbers[i] / (double)sum;
            
        }
        return retVal;
    }
    
    private static String joinArray(int[] array,String separator) {
        StringBuilder joinedArray = new StringBuilder("");
        
        for (int i = 0; i < array.length; i++) {
            joinedArray.append(array[i]);
            if (i < array.length-1) {
                joinedArray.append(" " + separator);
            }
        }        
        return joinedArray.toString();
    }
    
    private static void printMultiA(int[][] mArray) {
        for (int[] numbers : mArray) {
            System.out.println(joinArray(numbers, ""));
        }
    }
    
    private static void calcMulti(int[][] mArray) {
        for (int[] numbers : mArray) {
            System.out.println(joinArray(numbers, "") + " | " + sum(numbers));
        }
        for (int i = 0; i < mArray[0].length; i++) {
            System.out.print("--");
        }
        System.out.println("/  ");
        for (int i = 0; i < mArray[0].length; i++) {
            int column = 0;
            for (int[] line : mArray) {
                column += line[i]; 
            }
            System.out.print(column + " ");
        }
        int diag = 0;
        for (int i = 0; i < mArray.length; i++) {
           diag += mArray[i][i];
        }
        System.out.println("  " + diag);
        
    }
    
    public static void main(String[] args) {
        int[][] m = new int[][]{
                {0,1,2},
                {1,2,3},
                {2,3,4}
        };
        calcMulti(m);
    }
    
}