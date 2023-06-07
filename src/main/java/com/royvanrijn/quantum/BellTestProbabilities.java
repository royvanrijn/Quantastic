package com.royvanrijn.quantum;

import java.util.Arrays;
import java.util.Random;

public class BellTestProbabilities {

    public static void main(String[] args) {
        new BellTestProbabilities().run();
    }

    private void run() {


        Random random = new Random();

        long tests = 10000;
        long matches = 0;
        for(int i = 0; i < tests; i++) {
            // Random predetermined state:
            boolean[] state1 = new boolean[] {
                    random.nextBoolean(), random.nextBoolean(),random.nextBoolean(),random.nextBoolean(), random.nextBoolean(),random.nextBoolean()
                    //true, false, false
            };
            // Opposite particle:
            boolean[] state2 = new boolean[] {
                    !state1[0], !state1[1], !state1[2],!state1[3], !state1[4], !state1[5]
            };

            System.out.println(Arrays.toString(state1));
            System.out.println(Arrays.toString(state2));
            boolean measurement1 = state1[random.nextInt(6)];
            boolean measurement2 = state2[random.nextInt(6)];
            if(measurement1 != measurement2) {
                matches++;
            }
        }

        System.out.println("Different:  "+ matches +" of "+tests );

        System.out.println("Percentage: " + matches*(100.0/tests));



    }

}
