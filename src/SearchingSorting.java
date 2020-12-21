import java.util.LinkedList;
import java.util.List;

public class SearchingSorting {
    /*
     * This method performs a binary search on a List with Object that implements
     * the Comparable interface. It returns the index of the element or -1 if it is
     * not found.
     */
    public static <T extends Comparable<T>> int searchIndex(List<T> array, T target) {
        int aryStart = 0; // Start of array
        int aryEnd = array.size() - 1; // End of array
        while ((aryEnd - aryStart) > 1) {
            int half = (aryStart + aryEnd) / 2;
            if (array.get(half).equals(target)) {
                return aryStart;
            } else if (target.compareTo(array.get(half)) == 1) {
                aryStart = half;
            } else {
                aryEnd = half;
            }
        }
        return -1;
    }

    /*
     * This method performs a binary search on a List with Object that implements
     * the Comparable interface. It returns the element or null if it is not found.
     */
    public static <T extends Comparable<T>> T search(List<T> array, T target) {
        int aryStart = 0; // Start of array
        int aryEnd = array.size() - 1; // End of array
        while ((aryEnd - aryStart) > 1) {
            int half = (aryStart + aryEnd) / 2;
            if (array.get(half).equals(target)) {
                return array.get(half);
            } else if (target.compareTo(array.get(half)) == 1) {
                aryStart = half;
            } else {
                aryEnd = half;
            }
        }
        return null;
    }

    public static <T extends Comparable<T>> void sortList(List<T> array) {
        List<T> sorted = sortListRecursive(array);
        array.clear();
        for (T t : sorted) {
            array.add(t);
        }
    }

    public static <T extends Comparable<T>> void sortListReverse(List<T> array) {
        List<T> sorted = sortListRecursiveReverse(array);
        array.clear();
        for (T t : sorted) {
            array.add(t);
        }
    }

    /*
     * This method sorts the given List that extends comparable using the quick sort
     * algorithm
     */
    public static <T extends Comparable<T>> List<T> sortListRecursive(List<T> array) {
        if (array.size() <= 0)
            return array;
        List<T> ret = new LinkedList<>(); // Return value of the method
        List<T> right = new LinkedList<>(); // Values on the right side of the pivot
        List<T> left = new LinkedList<>(); // Values on the left side of the pivot

        T pivot = array.get(array.size() / 2); // The pivot is the middle element
        // Iterate through the array and compare values and put them into the
        // appropriate List
        for (T t : array) {
            int compare = t.compareTo(pivot); // Compare to pivot
            if (compare > 0)
                right.add(t); // Add to right side array if current is bigger than pivot
            else if (compare < 0)
                left.add(t); // Add to left side array if current is smaller than pivot
        }

        if (right.size() > 1)
            right = sortListRecursive(right); // Recursivley sort the right side. If there is only one element it is
                                              // already sorted
        if (left.size() > 1)
            left = sortListRecursive(left); // Recursivley sort the left side. If there is only one element it is
                                            // already sorted

        for (T lval : left) { // Add all left values to the return array
            ret.add(lval);
        }
        ret.add(pivot); // Add pivot to the return array
        for (T rval : right) { // Add all right values to the return array
            ret.add(rval);
        }

        return ret; // Return the return array
    }

    /*
     * This method reversly sorts the given List that extends comparable using the
     * quick sort algorithm
     */
    public static <T extends Comparable<T>> List<T> sortListRecursiveReverse(List<T> array) {
        if (array.size() <= 0)
            return array;
        List<T> ret = new LinkedList<>(); // Return value of the method
        List<T> right = new LinkedList<>(); // Values on the right side of the pivot
        List<T> left = new LinkedList<>(); // Values on the left side of the pivot

        T pivot = array.get(array.size() / 2); // The pivot is the middle element
        // Iterate through the array and compare values and put them into the
        // appropriate List
        for (T t : array) {
            int compare = t.compareTo(pivot); // Compare to pivot
            if (compare > 0)
                right.add(t); // Add to right side array if current is bigger than pivot
            else if (compare < 0)
                left.add(t); // Add to left side array if current is smaller than pivot
        }

        if (right.size() > 1)
            right = sortListRecursive(right); // Recursivley sort the right side. If there is only one element it is
                                              // already sorted
        if (left.size() > 1)
            left = sortListRecursive(left); // Recursivley sort the left side. If there is only one element it is
                                            // already sorted

        for (T rval : right) { // Add all right values to the return array
            ret.add(rval);
        }
        ret.add(pivot); // Add pivot to the return array
        for (T lval : left) { // Add all left values to the return array
            ret.add(lval);
        }
        return ret; // Return the return array
    }
}
