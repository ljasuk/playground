package hujj.euclid;

class Euclid {

    private static int gcd(final int a, final int b) {
        if (b != 0) {
            if (a > b) {
                return gcd (b, a-b);
            }

            if (b > a) {
                return gcd (a, b-a);
            }
        }
        return b;
    }

    public static void main(String[] args) {
        int a = 0;
        int b = 0;
        try {
            a = Integer.parseInt(args[0]);
            b = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("Wrong format. Please provide two integers.");
            System.exit(-1);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Please provide arguments.");
            System.exit(-1);
        };
        
        if (a*b == 0) {
            System.out.println("Division by zero no good.");
            System.exit(-1);
        }
        
        System.out.println(gcd(a,b));
    }
}