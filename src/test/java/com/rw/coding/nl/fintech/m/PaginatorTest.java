package com.rw.coding.nl.fintech.m;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;


class PaginatorTest {
    @Test
    void testBasic() {
        @SuppressWarnings("unchecked") //
        List<List<String>> items = mutableList( //
            mutableList("item1", "9", "1000"), //
            mutableList("item2", "5", "2000"), //
            mutableList("item3", "1", "1500") //
        );
        List<String> result = Paginator.pageItems(items, 2, 0, 2, 1);
        assertEquals(Collections.singletonList("item2"), result);
    }

    private static <T> List<T> mutableList(T... values) {
        List<T> list = new ArrayList<>(values.length);
        for (T value : values) {
            list.add(value);
        }
        return list;
    }
}
