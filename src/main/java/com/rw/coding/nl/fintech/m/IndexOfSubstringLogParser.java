package com.rw.coding.nl.fintech.m;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;


public class IndexOfSubstringLogParser {
    private IndexOfSubstringLogParser() {
    }

    public static void run(String inputFilename, String outputFilename) throws Exception {
        Set<String> outputLines = new LinkedHashSet<>();
        Set<String> requests = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilename))) {
            reader.lines().forEach(line -> {
                LogRecord logRecord = parseLogRecord(line);
                if (isRelevantLogRecord(logRecord) && requests.add(logRecord.requestLine.requestPath.toUpperCase())) {
                    outputLines.add(finalRequestFileName(logRecord.requestLine.requestPath));
                }
            });
        }
        writeOutputFile(outputFilename, outputLines);
    }

    private static LogRecord parseLogRecord(String line) {
        char[] charArray = line.toCharArray();
        int firstQuoteIndex = charArrayIndexOf(charArray, 0, '"');
        int firstSpaceAfterFirstQuoteIndex = charArrayIndexOf(charArray, firstQuoteIndex + 1, ' ');
        String requestMethod = charArraySubString(charArray, firstQuoteIndex + 1, firstSpaceAfterFirstQuoteIndex);
        int secondSpaceAfterFirstQuoteIndex = charArrayIndexOf(charArray, firstSpaceAfterFirstQuoteIndex + 1, ' ');
        String requestPath = charArraySubString(charArray, firstSpaceAfterFirstQuoteIndex + 1, secondSpaceAfterFirstQuoteIndex);
        int secondQuoteIndex = charArrayIndexOf(charArray, firstQuoteIndex + 1, '"');
        int lastSpaceIndex = charArrayLastIndexOf(charArray, charArray.length - 1, ' ');
        String responseCode = charArraySubString(charArray, secondQuoteIndex + 2, lastSpaceIndex);
        return new LogRecord(new RequestLine(requestMethod, requestPath), responseCode);
    }

    private static boolean isRelevantLogRecord(LogRecord logRecord) {
        return "200".equals(logRecord.responseCode) //
            && "GET".equals(logRecord.requestLine.requestMethod) //
            && logRecord.requestLine.requestPath.toLowerCase().endsWith(".gif");
    }

    private static String finalRequestFileName(String requestFilePath) {
        int i = requestFilePath.lastIndexOf('/');
        return (i < 0) ? requestFilePath : requestFilePath.substring(i + 1);
    }

    private static void writeOutputFile(String fileName, Collection<String> dataLines) throws IOException {
        Files.createDirectories(Paths.get(fileName).getParent());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (String line : dataLines) {
                writer.write(line);
                writer.write('\n');
            }
        }
    }

    private static int charArrayIndexOf(char[] charArray, int startIndex, char ch) {
        for (int i = startIndex; i < charArray.length; i++) {
            if (charArray[i] == ch) {
                return i;
            }
        }
        throw new IllegalStateException();
    }

    private static String charArraySubString(char[] charArray, int startIndex, int endIndex) {
        // using a Builder incurs the same cost
        return new String(charArray, startIndex, endIndex - startIndex);
    }

    private static int charArrayLastIndexOf(char[] charArray, int startIndex, char ch) {
        for (int i = startIndex; i >= 0; i--) {
            if (charArray[i] == ch) {
                return i;
            }
        }
        throw new IllegalStateException();
    }

    static class LogRecord {
        final RequestLine requestLine;
        final String responseCode;

        LogRecord(RequestLine requestLine, String responseCode) {
            this.requestLine = requestLine;
            this.responseCode = responseCode;
        }
    }

    static class RequestLine {
        final String requestMethod;
        final String requestPath;

        RequestLine(String requestMethod, String requestPath) {
            this.requestMethod = requestMethod;
            this.requestPath = requestPath;
        }
    }
}
