package hujj.quadratic;

import java.util.Arrays;

class Quadra {
    private static double[] sqroots(final double a, final double b, final double c) {
        if (a==0 || (b*b - 4*a*c) < 0) {
            return new double[0];
        }
        
        if ((b*b - 4*a*c) == 0) {
            return new double[]{0};
        }
        
        double[] result = new double[2];
        result[0] = (-b + Math.sqrt(b*b - 4*a*c))/2*a;
        result[1] = (-b - Math.sqrt(b*b - 4*a*c))/2*a;
        return result;
    }

    public static void main(String[] args) {
        double a = 0;
        double b = 0;
        double c = 0;
        try {
            a = Double.parseDouble(args[0]);
            b = Double.parseDouble(args[1]);
            c = Double.parseDouble(args[2]);
        } catch (NumberFormatException e) {
            System.out.println("Wrong format. Please provide three doubles.");
            System.exit(-1);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Please provide arguments.");
            System.exit(-1);
        };
        System.out.println(Arrays.toString(sqroots(a, b, c)));
    }
}