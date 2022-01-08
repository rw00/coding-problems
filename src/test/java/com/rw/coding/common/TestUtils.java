package com.rw.coding.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Iterator;
import java.util.stream.BaseStream;


public class TestUtils {
    private TestUtils() {
    }

    public static void assertStreamsEqual(BaseStream<?, ?> stream1, BaseStream<?, ?> stream2) {
        Iterator<?> iterator1 = stream1.iterator(), iterator2 = stream2.iterator();
        while (iterator1.hasNext() && iterator2.hasNext()) {
            assertEquals(iterator1.next(), iterator2.next());
        }
        assertTrue(!iterator1.hasNext() && !iterator2.hasNext());
    }
}
