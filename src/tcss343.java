import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.*;

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
        brute(arr);
//        generateFile(5);
//        generateFile(100);
//        generateFile(200);
//        generateFile(400);
//        generateFile(600);
//        generateFile(800);
    }

    /**
     * Brute force solution for finding the cheapest path.
     */
    public static void brute(String[][] prices) {
        final int nVal = prices.length;
        int minVal = -1;
        Set<Integer> minSet = new HashSet<>();

        HashSet<Integer> startingSet = new HashSet<>();                         /* This set will hold 1 ..... n values. */
        for(int i = 1; i <= nVal; i++) {                                        /* Populates the starting set */
            startingSet.add(i);
        }

        Set<Set<Integer>> setsOSets = powerSet(startingSet);                    /* Gets the set of sets. */

        for(Set currSet : setsOSets) {
            int pathSum = 0;                                                    /* Total cost for this path. */
            ArrayList<Integer> setList = new ArrayList<>(currSet);

            if(currSet.contains(1) && currSet.contains(nVal)) {                 /* If it contains 1 and n, get min value. */
                for(int i = 0; i < currSet.size() - 1; i++) {                   /* i = Rx, i + 1 = Ry */
                    int priceRow = setList.get(i) - 1;                          /* Get the row in the array of the path cost. */
                    int priceCol = setList.get(i + 1) - 1;                      /* Get the col in the array of the path cost. */
                    pathSum += Integer.parseInt(prices[priceRow][priceCol]);
                }

                if(pathSum < minVal || minVal == -1) {                          /* Assign minVal if pathSum is smaller. */
                    minVal = pathSum;
                    minSet = currSet;
                }
            }
        }

        /* Display the minimum set. */
        System.out.println("Minimum path: " + minSet.toString() + ", Minimum cost: " + minVal);
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
        int rental[] = new int[SIZE];
        int price = 0;
        int total = 0;

        cost[0] = 0;
        for (int i = 1; i < SIZE; i ++) {

            if (isNumber(prices[i-1][i])) { price = Integer.parseInt(prices[i-1][i]); }
            cost[i] = cost[i-1] + price;
            rental[i] = i-1;

            for (int j = i - 1; j >= 1; j--) {

                if (isNumber(prices[j][i])) { price = Integer.parseInt(prices[j][i]); }
                if (cost[j] + price < cost[i]) {
                    cost[i] = cost[j] + price;
                    rental[i] = j;
                }
            }
        }
        System.out.println(Arrays.toString(cost));
        System.out.println(Arrays.toString(rental));

//        ArrayList<Integer> posts = new ArrayList<>(); //number of posts rented from
        for (int i = 0; i < rental.length; i++) {
            if (rental[i] != 1) {
                System.out.println(rental[i]);
                total += rental[i];

            }
        }

//        /*recover*/
//
//        int temp = 0;
//        for (int i = 1; i < posts.size(); i++) {
////            if (rental[i] != 1) {
////                if (isNumber(prices[rental[i-1]][rental[i]])) {
////                    temp = Integer.parseInt(prices[rental[i-1]][rental[i]]);
////                }
//            if (isNumber(prices[posts.get(i-1)][posts.get(i)])) {
//                temp = Integer.parseInt(prices[posts.get(i-1)][posts.get(i)]);
//                System.out.println("Adding: (" + posts.get(i-1) + ", " + posts.get(i) + ")");
//            }
//            total += temp;
////            }
//        }

        System.out.println("Total cost: " + total);

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
                int increment =  r.nextInt(8);
                if (i == j) {
                    out.write("0\t");
                } else if (i > j) {
                    out.write("NA\t");
                } else {
                    base += increment;
                    out.write(base + "\t");
                }
            }
            out.newLine();
            base = 2;

        }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Takes a set of integers, and returns a set of all power sets.
     * @param theStartingSet the starting set to be expanded.
     * @return the final set of subsets from theStartingSet.
     */
    public static Set<Set<Integer>> powerSet(Set<Integer> theStartingSet) {

        Set<Set<Integer>> setsOSets = new HashSet<>();

        if (theStartingSet.isEmpty()) {                                             /* BASE CASE: If the set is empty, return. */
            setsOSets.add(new HashSet<Integer>());
            return setsOSets;
        }

        List<Integer> list = new ArrayList<Integer>(theStartingSet);                /* Convert ti list for index access. */
        Integer first = list.get(0);                                                /* Get the first value of the set. */
        Set<Integer> rest = new HashSet<Integer>(list.subList(1, list.size()));     /* Get the rest of the set. */


        for (Set<Integer> currentSet : powerSet(rest)) {                            /* For each set within sets. */

            Set<Integer> newSet = new HashSet<Integer>();                           /* Create a new set to store the data. */
            newSet.add(first);                                                      /* Add the first elements. */
            newSet.addAll(currentSet);                                              /* Add the rest of the elements. */
            setsOSets.add(newSet);                                                  /* Add the new set to our set of sets. */
            setsOSets.add(currentSet);                                              /* Add the old set to our set of sets. */
        }

        return setsOSets;
    }
}
