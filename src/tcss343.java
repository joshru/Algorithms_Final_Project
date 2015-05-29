import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

/**
 * TCSS 343 Final Project
 * @author Alexander Cherry
 * @author Brandon Bell
 * @author Josh Rueschenberg
 */
public class tcss343 {

    public static final Random r = new Random();
    public static final int SIZE = 4;

    public static void main(String[] args) {
        String arr[][] = new String[SIZE][SIZE];

        File in = new File("sample_input.txt");
        Scanner scn = null;
        try {
            scn = new Scanner(in).useDelimiter("\\t|\\n"); //delimiter will be tab OR newline
        } catch(Exception e) {
            e.printStackTrace();
        }

        for(int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                arr[i][j] = scn.next();
            }
            scn.nextLine();
        }

        System.out.println(Arrays.deepToString(arr));
        dynamic(arr);
//        generateFile(10);
    }

    /**
     * Brute force solution for finding the cheapest path
     */
    public static void brute() {

    }

    /**
     * Divide and conquer solution for finding the cheapest pat
     */
    public static void divide() {

    }

    /**
     * Dynamic programming solution for finding cheapest path
     */
    public static void dynamic(String[][] prices) {
        int cost[] = new int[SIZE];
        cost[0] = 0;
        int price = 0;
        for (int i = 1; i < SIZE; i ++) {

            if (isNumber(prices[i-1][i])) { price = Integer.parseInt(prices[i-1][i]); }
            cost[i] = cost[i-1] + price;
            for (int j = i - 2; j >= 1; j--) {
                if (isNumber(prices[j][i])) { price = Integer.parseInt(prices[j][i]); }

                if (cost[j] + price < cost[i]) {
                    cost[i] = cost[j] + price;
                }
            }
        }
        System.out.println(Arrays.toString(cost));

    }

    public static boolean isNumber(String test) {
        try {
            Integer.parseInt(test);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * Generates a random file of costs for use by our algorithms
     * @param theDim the dimensions of the file to be written
     */
    public static void generateFile(final int theDim) {
        String filename = "" + theDim + "input.txt";
        BufferedWriter out = null;
        int base = 2;
        try {
            out = new BufferedWriter(new FileWriter(filename));


        for (int i = 0; i < theDim; i++) {
            for (int j = 0; j < theDim; j++) {
                int increment =  r.nextInt(4);
                if (i == j) {
                    out.write("0\t");
                } else if (i > j) {
                    out.write("NA\t");
                } else {
                    out.write(base + increment + "\t");
                }
            }
            out.newLine();

        }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
