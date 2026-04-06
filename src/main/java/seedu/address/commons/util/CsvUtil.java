package seedu.address.commons.util;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.logic.parser.ParserUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Attendance;
import seedu.address.model.person.Email;
import seedu.address.model.person.GitHub;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.RsvpStatus;
import seedu.address.model.person.Team;
import seedu.address.model.tag.Tag;

/**
 * Utility methods for CSV import and export of {@link Person} records.
 */
public final class CsvUtil {
    public static final String EXTENSION = ".csv";

    public static final String HEADER_NAME = "name";
    public static final String HEADER_PHONE = "phone";
    public static final String HEADER_EMAIL = "email";
    public static final String HEADER_ADDRESS = "address";
    public static final String HEADER_TEAM = "team";
    public static final String HEADER_GITHUB = "github";
    public static final String HEADER_RSVP_STATUS = "rsvpstatus";
    public static final String HEADER_TAGS = "tags";
    public static final String HEADER_CHECKIN_STATUS = "checkinstatus";
    public static final String HEADER_STATUS = "status";

    private static final DateTimeFormatter EXPORT_TIMESTAMP_FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");

    private CsvUtil() {}

    /**
     * Reads participants from a CSV file and returns parse results.
     */
    public static CsvImportResult readPersonsFromCsv(Path csvPath) throws IOException, CsvDataException {
        requireNonNull(csvPath);
        List<String> lines = Files.readAllLines(csvPath, StandardCharsets.UTF_8);
        if (lines.isEmpty()) {
            throw new CsvDataException("CSV file is empty.");
        }

        List<String> headerValues = CsvParser.parseCsvRow(lines.get(0), 1);
        Map<String, Integer> headerToIndex = buildHeaderToIndex(headerValues);
        validateRequiredHeaders(headerToIndex);

        List<Person> validPersons = new ArrayList<>();
        List<CsvRowError> rowErrors = new ArrayList<>();

        for (int i = 1; i < lines.size(); i++) {
            int lineNumber = i + 1;
            String row = lines.get(i);
            if (row.trim().isEmpty()) {
                continue;
            }

            try {
                List<String> rowValues = CsvParser.parseCsvRow(row, lineNumber);
                Person person = toPerson(rowValues, headerToIndex);
                validPersons.add(person);
            } catch (CsvDataException | ParseException e) {
                rowErrors.add(new CsvRowError(lineNumber, e.getMessage()));
            }
        }

        return new CsvImportResult(validPersons, rowErrors);
    }

    /**
     * Writes participants to a CSV file.
     */
    public static void writePersonsToCsv(List<Person> persons, Path outputPath) throws IOException {
        requireNonNull(persons);
        requireNonNull(outputPath);

        FileUtil.createParentDirsOfFile(outputPath);

        StringBuilder csv = new StringBuilder();
        csv.append(String.join(",",
                HEADER_NAME,
                HEADER_PHONE,
                HEADER_EMAIL,
                HEADER_ADDRESS,
                HEADER_TEAM,
                HEADER_GITHUB,
                HEADER_RSVP_STATUS,
                HEADER_TAGS,
                HEADER_CHECKIN_STATUS))
                .append(System.lineSeparator());

        for (Person person : persons) {
            csv.append(CsvParser.escapeCsv(person.getName().fullName)).append(',')
                    .append(CsvParser.escapeCsv(person.getPhone().value)).append(',')
                    .append(CsvParser.escapeCsv(person.getEmail().value)).append(',')
                    .append(CsvParser.escapeCsv(person.getAddress().value)).append(',')
                    .append(CsvParser.escapeCsv(person.getTeam().map(Team::toString).orElse(""))).append(',')
                    .append(CsvParser.escapeCsv(person.getGitHub().map(GitHub::toString).orElse(""))).append(',')
                    .append(CsvParser.escapeCsv(person.getRsvpStatus().toString())).append(',')
                    .append(CsvParser.escapeCsv(tagsToCsvField(person.getTags()))).append(',')
                    .append(CsvParser.escapeCsv(person.getCheckInStatus().getStatus() ? "yes" : "no"))
                    .append(System.lineSeparator());
        }

        FileUtil.writeToFile(outputPath, csv.toString());
    }

    /**
     * Returns a non-colliding CSV file path by appending a timestamp if needed.
     */
    public static Path resolveTimestampedPathIfExists(Path preferredPath) {
        requireNonNull(preferredPath);

        if (!Files.exists(preferredPath)) {
            return preferredPath;
        }

        String filename = preferredPath.getFileName().toString();
        int extensionIndex = filename.lastIndexOf('.');
        String stem = extensionIndex > 0 ? filename.substring(0, extensionIndex) : filename;
        String extension = extensionIndex > 0 ? filename.substring(extensionIndex) : EXTENSION;
        String timestamp = LocalDateTime.now().format(EXPORT_TIMESTAMP_FORMATTER);
        String timestampedName = stem + "-" + timestamp + extension;

        Path parent = preferredPath.getParent();
        return parent == null ? Path.of(timestampedName) : parent.resolve(timestampedName);
    }

    private static Map<String, Integer> buildHeaderToIndex(List<String> headerValues) {
        Map<String, Integer> headerToIndex = new HashMap<>();
        for (int i = 0; i < headerValues.size(); i++) {
            String normalizedHeader = headerValues.get(i).trim().toLowerCase();
            if (!normalizedHeader.isEmpty() && !headerToIndex.containsKey(normalizedHeader)) {
                headerToIndex.put(normalizedHeader, i);
            }
        }
        return headerToIndex;
    }

    private static void validateRequiredHeaders(Map<String, Integer> headerToIndex) throws CsvDataException {
        if (!headerToIndex.containsKey(HEADER_NAME)
                || !headerToIndex.containsKey(HEADER_PHONE)
                || !headerToIndex.containsKey(HEADER_EMAIL)
                || !headerToIndex.containsKey(HEADER_ADDRESS)) {
            throw new CsvDataException("CSV header must include: name, phone, email, address");
        }
    }

    private static Person toPerson(List<String> rowValues, Map<String, Integer> headerToIndex)
            throws ParseException, CsvDataException {
        String nameRaw = getValue(rowValues, headerToIndex, HEADER_NAME);
        String phoneRaw = getValue(rowValues, headerToIndex, HEADER_PHONE);
        String emailRaw = getValue(rowValues, headerToIndex, HEADER_EMAIL);
        String addressRaw = getValue(rowValues, headerToIndex, HEADER_ADDRESS);

        Name name = ParserUtil.parseName(nameRaw);
        Phone phone = ParserUtil.parsePhone(phoneRaw);
        Email email = ParserUtil.parseEmail(emailRaw);
        Address address = ParserUtil.parseAddress(addressRaw);

        Optional<Team> team = Optional.empty();
        String teamRaw = getValue(rowValues, headerToIndex, HEADER_TEAM);
        if (!teamRaw.isBlank()) {
            team = Optional.of(ParserUtil.parseTeam(teamRaw));
        }

        GitHub github = null;
        String githubRaw = getValue(rowValues, headerToIndex, HEADER_GITHUB);
        if (!githubRaw.isBlank()) {
            github = ParserUtil.parseGitHub(githubRaw);
        }

        String rsvpRaw = getValue(rowValues, headerToIndex, HEADER_RSVP_STATUS);
        RsvpStatus rsvpStatus = rsvpRaw.isBlank()
                ? new RsvpStatus("pending")
                : ParserUtil.parseRsvpStatus(rsvpRaw);

        String tagsRaw = getValue(rowValues, headerToIndex, HEADER_TAGS);
        Set<Tag> tags = ParserUtil.parseDelimitedTags(tagsRaw, ";");

        String checkInRaw = getOptionalValue(rowValues, headerToIndex, HEADER_CHECKIN_STATUS, HEADER_STATUS);
        Attendance attendance = parseAttendance(checkInRaw);

        return new Person(name, phone, email, address, team, tags, attendance, github, rsvpStatus);
    }

    private static String getValue(List<String> rowValues, Map<String, Integer> headerToIndex, String header)
            throws CsvDataException {
        Integer index = headerToIndex.get(header);
        if (index == null || index >= rowValues.size()) {
            return "";
        }

        String value = rowValues.get(index);
        if ((header.equals(HEADER_NAME)
                || header.equals(HEADER_PHONE)
                || header.equals(HEADER_EMAIL)
                || header.equals(HEADER_ADDRESS)) && value.trim().isEmpty()) {
            throw new CsvDataException("Missing required value for '" + header + "'");
        }
        return value.trim();
    }

    private static String getOptionalValue(List<String> rowValues, Map<String, Integer> headerToIndex,
                                           String... headers) {
        for (String header : headers) {
            Integer index = headerToIndex.get(header);
            if (index != null && index < rowValues.size()) {
                return rowValues.get(index).trim();
            }
        }
        return "";
    }

    private static Attendance parseAttendance(String value) throws ParseException {
        if (value == null || value.isBlank()) {
            return new Attendance();
        }
        return ParserUtil.parseAttendance(value);
    }

    private static String tagsToCsvField(Set<Tag> tags) {
        return tags.stream()
                .map(tag -> tag.tagName)
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.joining(";"));
    }
}
