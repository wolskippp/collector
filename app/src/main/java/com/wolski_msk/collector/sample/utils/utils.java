package com.wolski_msk.collector.sample.utils;

import java.util.ArrayList;

/**
 * Created by wpiot on 08.08.2016.
 */
public class utils {

    public static boolean checkSorting(String[] arraylist) {
        boolean isSorted = true;
        for (int i = 1; i < arraylist.length; i++) {
            if (arraylist[i - 1].compareTo(arraylist[i]) > 0) {
                isSorted = false;
                break;
            }
        }
        return isSorted;
    }

}
