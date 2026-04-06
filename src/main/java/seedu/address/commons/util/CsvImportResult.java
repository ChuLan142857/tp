package seedu.address.commons.util;

import java.util.List;

import seedu.address.model.person.Person;

/**
 * Represents the structured result of importing persons from a CSV file.
 * Contains both successfully parsed persons and any row-level errors encountered.
 */
public class CsvImportResult {
    private final List<Person> persons;
    private final List<CsvRowError> rowErrors;

    /**
     * Creates an immutable import result containing valid persons and row errors.
     *
     * @param persons List of successfully parsed Person objects.
     * @param rowErrors List of row-level errors encountered during parsing.
     */
    public CsvImportResult(List<Person> persons, List<CsvRowError> rowErrors) {
        this.persons = List.copyOf(persons);
        this.rowErrors = List.copyOf(rowErrors);
    }

    /**
     * Returns the list of valid participants parsed from the CSV file.
     *
     * @return Immutable list of Person objects.
     */
    public List<Person> getPersons() {
        return persons;
    }

    /**
     * Returns the list of row-level parsing errors captured during import.
     *
     * @return Immutable list of CsvRowError objects.
     */
    public List<CsvRowError> getRowErrors() {
        return rowErrors;
    }
}
