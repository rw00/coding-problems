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


public abstract class AbstractLogParser {
    public void run(String inputFilename, String outputFilename) throws IOException {
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

    protected abstract LogRecord parseLogRecord(String line);

    private boolean isRelevantLogRecord(LogRecord logRecord) {
        return "200".equals(logRecord.responseCode) //
            && "GET".equals(logRecord.requestLine.requestMethod) //
            && logRecord.requestLine.requestPath.toLowerCase().endsWith(".gif");
    }

    private String finalRequestFileName(String requestFilePath) {
        int i = requestFilePath.lastIndexOf('/');
        return (i < 0) ? requestFilePath : requestFilePath.substring(i + 1);
    }

    private void writeOutputFile(String fileName, Collection<String> dataLines) throws IOException {
        Files.createDirectories(Paths.get(fileName).getParent());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (String line : dataLines) {
                writer.write(line);
                writer.write('\n');
            }
        }
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
