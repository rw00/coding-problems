package com.rw.coding.nl.fintech.m;

import com.rw.coding.common.TestUtils;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;


class IndexOfSubstringLogParserTest {
    @Test
    void testBasic1() throws Exception {
        String inputFilename = "src/test/resources/test_input_log_parser_basic_1.txt";
        String outputFilename = "target/test/gif_requested_files_basic_1.txt";
        IndexOfSubstringLogParser.run(inputFilename, outputFilename);
        TestUtils.assertStreamsEqual(Files.lines(Paths.get(outputFilename)), //
            Files.lines(Paths.get("src/test/resources/test_expected_log_parser_basic_1.txt")));
    }
}
