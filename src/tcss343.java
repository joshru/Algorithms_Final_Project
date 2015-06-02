import sun.awt.image.ImageWatched;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.*;
import java.util.concurrent.Exchanger;

/**
 * TCSS 343 Final Project
 * @author Alexander Cherry
 * @author Brandon Bell
 * @author Josh Rueschenberg
 */
public class tcss343 {
    //test comment
    public static final Random r = new Random();
    public static final int SIZE = 25;
    public static int n;
    public static Integer[][] prices;

    public static void main(String[] args) {
        prices = new Integer[SIZE][SIZE];

        n = SIZE;//prices[0].length;                          /* Gets the n size of the set. */

        //System.out.println(Arrays.deepToString(arr));
        System.out.println("Array obtained from file");
        generateFile(25);
        readFile();
        for (int i = 0; i < n; i++) {

            for (int j = 0; j < n; j++) {
                System.out.print(prices[i][j] + "\t");
            }
            System.out.println();
        }
        System.out.println();


        int[] arr = new int[n + 1];

        arr[0] = 0;

        brute();
        //brandonDynamic();
       /* int[] minCost = divide(arr[0]);

        System.out.println(Arrays.toString(minCost));
          //Display the minimum set.
        String minPathDivide = buildDividePath(minCost);
        System.out.println("Divide and Conquer Algorithm");
        System.out.println("Minimum Path: " + minPathDivide + ", Minimum cost: " + minCost[0]);
        System.out.println();*/


//        generateFile(5);
//        generateFile(100);
//        generateFile(200);
//        generateFile(400);
//        generateFile(600);
//        generateFile(800);
    }

    private static void readFile() {
        FileReader inputStream = null;
        String fileName = "25input.txt";
        try {
            inputStream = new FileReader(fileName);
            int c;
            StringBuilder message = new StringBuilder();

            while ((c = inputStream.read()) != -1) {
                message.append((char) c);
            }

            //populate array
            int start = 0;
            int end = 0;
            int i = 0, j = 0;

            while (end < message.length()) {
                while (isValidInput(message.charAt(end))) {
                    end++;
                }
                String nextVal = message.substring(start, end);
                if (isNumber(nextVal)) {
                    prices[i][j] = Integer.parseInt(nextVal);
                } else {
                    prices[i][j] = -1;
                }
                j++;

                if (j == n) {
                    j = 0;
                    i++;
                }

                while (!isValidInput(message.charAt(end))) end++;
                start = end;

            }

            inputStream.close();

        } catch (Exception e) {

        }
        prices[n-1][n-1] = 0;

    }
    private static boolean isValidInput(char c) {
        return "NA".contains(""+c) || isNumber(""+c);
    }

    private static String buildDividePath(int[] minCost) {
        StringBuilder sb = new StringBuilder();
        sb.append("[1");
        for(int i = 1; i < minCost.length; i++) {
            if(minCost[i] > 0) {
                sb.append(", " + minCost[i]);
            }
        }
        sb.append("]");

        return sb.toString();
    }

    /**
     * Obtains and prints the lowest possible cost of canoe-ing down the river,
     * uses a brute force method of generating all the power sets and then choosing
     * the optimal one.
     * Asymptotic growth is roughly O(2^n)
     */
    public static void brute() {
        final int nVal = prices.length;
        int minVal = -1;
        Set<Integer> minSet = new HashSet<>();

        HashSet<Integer> startingSet = new HashSet<>();                         /* This set will hold 1 ..... n values. */
        for(int i = 1; i <= nVal; i++) {                                        /* Populates the starting set */
            startingSet.add(i);
        }

//        Set<Set<Integer>> setsOSets = getPowerSet(startingSet);                 /* Gets the set of sets. */
        Set<Set<Integer>> setsOSets = getPowerSetIterative(startingSet);
        //System.out.println("Sets generated");
        System.out.println("Brute Force Algorithm");
        for(Set<Integer> currSet : setsOSets) {
            Integer pathSum = 0;                                                /* Total cost for this path. */
            ArrayList<Integer> setList = new ArrayList<>(currSet);

            if(currSet.contains(1) && currSet.contains(nVal)) {                 /* If it contains 1 and n, get min value. */
                //TODO uncomment following line to view valid subsets.
                //System.out.println(currSet);
                for(int i = 0; i < currSet.size() - 1; i++) {                   /* i = Rx, i + 1 = Ry */
                    int priceRow = setList.get(i) - 1;                          /* Get the row in the array of the path cost. */
                    int priceCol = setList.get(i + 1) - 1;                      /* Get the col in the array of the path cost. */
                    pathSum += prices[priceRow][priceCol];
                }

                if(pathSum < minVal || minVal == -1) {                          /* Assign minVal if pathSum is smaller. */
                    minVal = pathSum;
                    minSet = currSet;
                }
            }
        }

        /* Display the minimum set. */
        System.out.println("Minimum path: " + minSet.toString() + ", Minimum cost: " + minVal);
        System.out.println();
    }

    /**
     * Divide and conquer solution for finding the cheapest pat
     * Asymptotic complexity is O(n^2)
     */
    public static int[] divide(int i) {
        int retVal = 0;
        int minVal = Integer.MAX_VALUE;
        int minJ = Integer.MAX_VALUE;
        int[] arr = new int[n + 1];

        if(i == n - 1) {        /* BASE CASE */
            arr[0] = 0;
            return arr;
        } else {
            for(int j = i + 1; j < n; j++) {
                int[] curArr = divide(j);
                int curVal = curArr[0] + prices[i][j];//getVal(i, j);

                if (curVal < minVal) {
                    minVal = curVal;
                    minJ = j;
                    for (int k = 0; k < arr.length; k++) {
                        arr[k] = curArr[k];
                    }
                }
            }
        }
        arr[0] = minVal;                /* Updates the minimum value. */

        /* Add the current solution j value to the argument. */
        arr[i + 1] = minJ + 1;
        //System.out.println("i: " + (i + 1) + ", j: " + (minJ + 1) + ", return value: " + retVal);
        return arr;
    }



    /**
     * Obtains and prints the lowest possible cost of canoe-ing down the river,
     * uses a dynamic programming algorithm.
     * Assymptotic growth is roughly O(n^2)
     */
    public static void brandonDynamic() {
        int n = prices[0].length;
        Integer[][] solutionArr = new Integer[n][n];

        /* Fill in top row of solution array
         * Will always be the same as the top row of the input
         */
        for (int i = 0 ; i < n; i++) {
           // if (isNumber(prices[0][i]))
                solutionArr[0][i] = prices[0][i];
        }

        //Top to bottom
        for (int i = 1; i < n; i++) {
            //Left to right
            for (int j = i; j < n; j++) {
                int minValue = -1;

                //Find the minimum value of all values to the left of the current cell [i][j]
                //added onto the current cell. That is, the most optimal previous value plus the price
                //of renting a canoe in this particular column.
                for (int k = i; k < j; k++) {
                    if (solutionArr[i][k]
                            + prices[i][j] < minValue
                            || minValue == -1) {
                        minValue = solutionArr[i][k] + prices[i][j];
                    }
                }
                //find the minimum value of all cells above in the same column of the current cell
                //if any of these values are less than the current minimum obtained from looking to the left,
                //update the minimum to the value above as it is more optimal.
                for (int k = 0; k < i; k++) {
                    if (prices[k][j] != -1) {

                        if (solutionArr[k][j] < minValue || minValue == -1) {
                            minValue = solutionArr[k][j];
                        }
                    }
                }
                //Finally, update the current cell to the most optimal value obtained from the above loops.
                solutionArr[i][j] = minValue;
            }
        }

        System.out.println("Dynamic Programming Algorithm");

        //TODO Uncomment out for loop to view dynamic array
        //Print out the resulting solution array.
        /*
        for (int i = 0; i < n; i++) {

            for (int j = 0; j < n; j++) {
                if (solutionArr[i][j] == null) System.out.print("-1\t"); //replace nulls with -1's for readability.
                else System.out.print(solutionArr[i][j] + "\t");

            }
            System.out.println();
        }
        */

        System.out.println("Minimum path: " + recover(solutionArr).toString() + ", Minimum cost: " + solutionArr[n - 1][n - 1]);
        System.out.println();
    }

    /**
     * Recovers the optimal path from the solution array obtained from the
     * dynamic programming approach.
     *
     * @param solutionArr the solution array
     * @return a set of the winning indexes. (not zero based)
     */
    public static Set<Integer> recover(Integer[][] solutionArr) {
        //fackin recover time bud
        //algorithm:
        //add first and last col # to solution set

        //start in bottom right cell of solution array

        //if cell above is equal to current cell
        //    go up
        //else
        //    go left, record new col in solution
        int n = solutionArr[0].length;
        Set<Integer>  winSet = new HashSet<>();

        winSet.add(1); winSet.add(n); //add first and last column to winning set
        int row = n - 1, col = n - 1; //very last cell

        //while we're still recovering the path
        while (row > 0) {

            int current = solutionArr[row][col];
            int above   = solutionArr[row-1][col];
          //  int left    = solutionArr[row][col-1];

            if (current == above) {
                row--; //go up
            } else {                 //optimal path comes from the left, add previous column to solution path
                int min = Integer.MAX_VALUE;
                int minIndex = Integer.MAX_VALUE;
                int i;
                for (i = row; i < col; i++) {

                    if (solutionArr[row][i] < min) {
                        min = solutionArr[row][i];
                        minIndex = i;
                    }

                }

                winSet.add(minIndex + 1);
                col = minIndex; //go back one column and restart the loop

            }
        }
      //  System.out.printf("Winning indexes = %s\n", winSet.toString());
        return winSet;
    }


    /**
     * Determines whether a position in our input array is a valid number or not.
     * Possibly find a different method, this may not be the most efficient approach.
     * @param test the potential number
     * @return whether or not the input was a number.
     */
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
     * PowerSet references were taken from wiki and Stackoverflow.
     * @param theStartingSet the starting set to be expanded.
     * @return the final set of subsets from theStartingSet.
     */
    public static Set<Set<Integer>> getPowerSet(Set<Integer> theStartingSet) {

        Set<Set<Integer>> setsOSets = new HashSet<>();

        if (theStartingSet.isEmpty()) {                                             /* BASE CASE: If the set is empty, return. */
            setsOSets.add(new HashSet<Integer>());
            return setsOSets;
        }

        List<Integer> list = new ArrayList<Integer>(theStartingSet);                /* Convert to list for index access. */
        Integer first = list.get(0);                                                /* Get the first value of the set. */
        Set<Integer> rest = new HashSet<Integer>(list.subList(1, list.size()));     /* Get the rest of the set. */


        for (Set<Integer> currentSet : getPowerSet(rest)) {                         /* For each set within sets. */

            Set<Integer> newSet = new HashSet<Integer>();                           /* Create a new set to store the data. */
            newSet.add(first);                                                      /* Add the first element. */
            newSet.addAll(currentSet);                                              /* Add the rest of the elements. */
            setsOSets.add(newSet);                                                  /* Add the new set to our set of sets. */
            setsOSets.add(currentSet);                                              /* Add the old set to our set of sets. */
        }
       //
        return setsOSets;
    }

    public static Set<Set<Integer>> getPowerSetIterative(Set<Integer> theStartingSet) {
        Set<Set<Integer>> powerSet = new HashSet<>();
        powerSet.add(new HashSet<Integer>());

        for (Integer currentInt : theStartingSet) {
            Set<Set<Integer>> newSet = new HashSet<>();

            for (Set<Integer> subset : powerSet) {
                newSet.add(subset);

                Set<Integer> newSubset = new HashSet<>(subset);
                newSubset.add(currentInt);
                newSet.add(newSubset);

            }
            powerSet = newSet;
        }
        System.out.println("Subsets generated.");
        return powerSet;

    }
}
