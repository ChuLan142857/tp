package seedu.address.commons.util;

/**
 * Represents a row-level parse error when importing CSV data.
 * Contains the line number and error message for debugging.
 */
public class CsvRowError {
    public final int lineNumber;
    public final String message;

    /**
     * Creates a row-level error with source line number and message.
     *
     * @param lineNumber The line number in the CSV file where the error occurred.
     * @param message The error message describing what went wrong.
     */
    public CsvRowError(int lineNumber, String message) {
        this.lineNumber = lineNumber;
        this.message = message;
    }

    @Override
    public String toString() {
        return "line " + lineNumber + ": " + message;
    }
}
