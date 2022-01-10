package com.rw.coding.nl.fintech.m;

public class StateMachineLogParser extends AbstractLogParser {
    @Override
    protected LogRecord parseLogRecord(String line) {
        var requestMethod = new StringBuilder();
        var requestPath = new StringBuilder();
        var responseCode = new StringBuilder();
        ParsingState state = ParsingState.IGNORE;
        char[] charArray = line.toCharArray();
        int index = 0;
        boolean loop = true;
        while (index < charArray.length && loop) {
            char ch = charArray[index];
            switch (state) {
            case IGNORE:
                if (ch == '"') {
                    state = ParsingState.PARSING_REQUEST_METHOD;
                }
                break;
            case PARSING_REQUEST_METHOD:
                if (ch == ' ') {
                    state = ParsingState.PARSING_REQUEST_PATH;
                } else {
                    requestMethod.append(ch);
                }
                break;
            case PARSING_REQUEST_PATH:
                if (ch == ' ') {
                    state = ParsingState.IGNORE_REST_OF_REQUEST_INFO;
                } else {
                    requestPath.append(ch);
                }
                break;
            case IGNORE_REST_OF_REQUEST_INFO:
                if (ch == ' ') {
                    state = ParsingState.PARSING_RESPONSE_CODE;
                }
                break;
            case PARSING_RESPONSE_CODE:
                if (ch == ' ') {
                    loop = false;
                } else {
                    responseCode.append(ch);
                }
                break;
            }
            index++;
        }
        return new LogRecord(new RequestLine(requestMethod.toString(), requestPath.toString()), responseCode.toString());
    }

    private enum ParsingState {
        IGNORE, IGNORE_REST_OF_REQUEST_INFO, PARSING_REQUEST_METHOD, PARSING_REQUEST_PATH, PARSING_RESPONSE_CODE
    }
}
