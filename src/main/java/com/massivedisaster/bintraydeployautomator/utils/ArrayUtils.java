package com.massivedisaster.bintraydeployautomator.utils;

/**
 * Array Utils
 */
public class ArrayUtils {

    /**
     * Adds all the elements of the given arrays into a new array.
     * The new array contains all of the element of array1 followed by all of the elements array2. When an array
     * is returned, it is always a new array.
     *
     * @param array1 the first array whose elements are added to the new array.
     * @param array2 the second array whose elements are added to the new array.
     * @return The new String[] array.
     */
    public static String[] addAll(String[] array1, String[] array2) {
        if (array1 == null) {
            return clone(array2);
        } else if (array2 == null) {
            return clone(array1);
        }
        String[] joinedArray = new String[array1.length + array2.length];
        System.arraycopy(array1, 0, joinedArray, 0, array1.length);
        System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
        return joinedArray;
    }

    /**
     * Clones an array returning a typecast result and handling null.
     * This method returns null for a null input array.
     *
     * @param array the array to clone, may be null
     * @return the cloned array, null if null input
     */
    private static String[] clone(String[] array) {
        if (array == null) {
            return null;
        }
        return array.clone();
    }

}
