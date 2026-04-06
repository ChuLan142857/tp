package seedu.address.commons.util;

/**
 * Exception thrown when CSV data validation fails.
 * Used for CSV-specific data validation failures during import/export operations.
 */
public class CsvDataException extends Exception {
    /**
     * Creates a CSV data validation exception with a user-facing message.
     *
     * @param message The error message describing the validation failure.
     */
    public CsvDataException(String message) {
        super(message);
    }
}
