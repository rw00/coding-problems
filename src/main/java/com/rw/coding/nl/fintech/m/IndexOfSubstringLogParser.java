package com.rw.coding.nl.fintech.m;

public class IndexOfSubstringLogParser extends AbstractLogParser {

    @Override
    protected LogRecord parseLogRecord(String line) {
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

    private int charArrayIndexOf(char[] charArray, int startIndex, char ch) {
        for (int i = startIndex; i < charArray.length; i++) {
            if (charArray[i] == ch) {
                return i;
            }
        }
        throw new IllegalStateException();
    }

    private int charArrayLastIndexOf(char[] charArray, int startIndex, char ch) {
        for (int i = startIndex; i >= 0; i--) {
            if (charArray[i] == ch) {
                return i;
            }
        }
        throw new IllegalStateException();
    }

    private String charArraySubString(char[] charArray, int startIndex, int endIndex) {
        // using a Builder incurs the same cost
        return new String(charArray, startIndex, endIndex - startIndex);
    }
}
