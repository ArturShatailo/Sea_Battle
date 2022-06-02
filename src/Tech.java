import java.util.Scanner;

public class Tech implements Symbols, CoordinateTurns {

    //"GetInputFunction" is a scanner of input that returns Integer entered by user
    public static int GetInputFunction() {
        Scanner scan = new Scanner(System.in);
        return scan.nextInt();
    }

    //"GetInputStringFunction" is a scanner of input that returns String entered by user
    public static String GetInputStringFunction() {
        Scanner scan = new Scanner(System.in);
        return scan.nextLine();
    }

    public static Integer getRandom(int min, int max) {
        return (int) (Math.random() * (max - min + 1)) + min;
    }

    /*
    Pairing X and Y value into coordinates pair and collect them into collection allCoordinates,
    that is implemented from interface CoordinateTurns.

    @param c1: Y axis of 2D coordinates net
    @param c2: X axis of 2D coordinates net
    */
    public static void getAllCoordinates(int c1, int c2) {

        for (int i = 0; i < c1; i++) {
            for (int k = 0; k < c2; k++) {
                int[] p = new int[2];
                p[0] = i;
                p[1] = k;
                allCoordinates.add(p);
            }
        }
    }
}

