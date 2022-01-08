package com.rw.coding.nl.fintech.m;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;


public class Paginator {
    private Paginator() {
    }

    /**
     * Given a List of items, where each element is a List of String detail about the item,
     * <br>return the List of items that belong to a specific page after sorting on one of the attributes of the item.
     *
     * @param items         the given items, each item represented as [name, numeric-attribute-values...]
     * @param sortParameter the item attribute to be used for sorting; a value from 0 to the (size-1) of the item info List
     * @param sortOrder     the sorting order; 0 for ascending and 1 for descending
     * @param itemsPerPage  the number of items that a page can contain; the last page may contain fewer items
     * @param pageNumber    the page index; a value that starts at 0
     *
     * @return the items' names that should be displayed on the given page after sorting
     */
    public static List<String> pageItems(List<List<String>> items, int sortParameter, int sortOrder, int itemsPerPage, int pageNumber) {
        items.sort((List<String> item1, List<String> item2) -> compare(item1, item2, sortParameter, sortOrder));
        int n = items.size();
        List<String> result = new ArrayList<>(itemsPerPage);
        int until = itemsPerPage * (pageNumber + 1);
        for (int i = (itemsPerPage * pageNumber); i < n && i < until; i++) {
            result.add(items.get(i).get(0));
        }
        return result;
    }

    private static int compare(List<String> item1, List<String> item2, int sortParameter, int sortOrder) {
        if (sortParameter == 0) {
            return compareInOrder(String::compareTo, item1.get(0), item2.get(0), sortOrder);
        }
        return compareInOrder(Integer::compare, Integer.parseInt(item1.get(sortParameter)), Integer.parseInt(item2.get(sortParameter)), sortOrder);
    }

    private static <T extends Comparable<T>> int compareInOrder(BiFunction<T, T, Integer> comparisonFunction, T param1, T param2, int sortOrder) {
        if (sortOrder == 0) {
            return comparisonFunction.apply(param1, param2);
        }
        return comparisonFunction.apply(param2, param1);
    }
}
