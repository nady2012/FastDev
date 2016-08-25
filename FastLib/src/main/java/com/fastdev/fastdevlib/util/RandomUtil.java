package com.fastdev.fastdevlib.util;

/**
 * The random number util
 * 
 * @author Shecan
 * 
 */
public class RandomUtil {

    /**
     * Return a random number of a specified range.
     * 
     * @param from
     *            the random number range start
     * @param to
     *            the random number range end
     * @return
     */
    public static int getRandom(int from, int to) {

        int dist = to - from + 1;
        int result = (int) (Math.random() * dist + from);
        return result;

    }
}
