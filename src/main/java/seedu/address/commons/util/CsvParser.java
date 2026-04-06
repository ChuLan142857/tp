package seedu.address.commons.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for low-level CSV parsing and formatting operations.
 * Handles CSV row parsing with proper quote handling and value escaping.
 */
public final class CsvParser {

    private CsvParser() {}

    /**
     * Parses a single CSV row into a list of field values.
     * Handles quoted fields, escaped quotes, and commas within quoted values.
     *
     * @param row The CSV row string to parse.
     * @param lineNumber The line number in the file (for error reporting).
     * @return List of field values extracted from the row.
     * @throws CsvDataException If the row has unclosed quoted values.
     */
    public static List<String> parseCsvRow(String row, int lineNumber) throws CsvDataException {
        List<String> values = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < row.length(); i++) {
            char character = row.charAt(i);
            if (character == '"') {
                if (inQuotes && i + 1 < row.length() && row.charAt(i + 1) == '"') {
                    // Escaped quote: "" becomes "
                    current.append('"');
                    i++;
                } else {
                    // Toggle quote state
                    inQuotes = !inQuotes;
                }
            } else if (character == ',' && !inQuotes) {
                // Field delimiter (only when not inside quotes)
                values.add(current.toString());
                current.setLength(0);
            } else {
                current.append(character);
            }
        }

        if (inQuotes) {
            throw new CsvDataException("Unclosed quoted value at line " + lineNumber);
        }

        values.add(current.toString());
        return values;
    }

    /**
     * Escapes a value for CSV output.
     * Wraps the value in quotes if it contains commas, quotes, or newlines.
     * Escapes internal quotes by doubling them.
     *
     * @param value The value to escape (can be null).
     * @return The escaped CSV value, quoted if necessary.
     */
    public static String escapeCsv(String value) {
        String raw = value == null ? "" : value;
        boolean shouldQuote = raw.contains(",") || raw.contains("\"") || raw.contains("\n") || raw.contains("\r");
        String escaped = raw.replace("\"", "\"\"");
        return shouldQuote ? '"' + escaped + '"' : escaped;
    }
}
