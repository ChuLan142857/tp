package seedu.address.logic.parser;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Shared validation helpers for event command parsers.
 */
public final class EventCommandParserUtil {
    public static final String VALID_PREFIXES_MESSAGE = "Valid prefixes: n/, d/, l/, desc/.";

    private static final Set<String> VALID_EVENT_PREFIXES = Set.of("n/", "d/", "l/", "desc/");
    private static final Pattern PREFIX_LIKE_TOKEN = Pattern.compile("^(?<prefix>[A-Za-z]+/).*$");

    private EventCommandParserUtil() {}

    /**
     * Throws a {@code ParseException} if a prefix-like token appears in an invalid place for event commands.
     */
    public static void validateNoUnknownPrefixes(String args, boolean hasIndexPreamble) throws ParseException {
        String[] tokens = args.trim().split("\\s+");
        if (tokens.length == 1 && tokens[0].isEmpty()) {
            return;
        }

        String currentPrefix = null;
        int startIndex = hasIndexPreamble && tokens.length > 0 ? 1 : 0;

        for (int i = startIndex; i < tokens.length; i++) {
            String token = tokens[i];
            Matcher matcher = PREFIX_LIKE_TOKEN.matcher(token);
            if (!matcher.matches()) {
                continue;
            }

            String prefix = matcher.group("prefix");
            if (VALID_EVENT_PREFIXES.contains(prefix)) {
                currentPrefix = prefix;
                continue;
            }

            if (currentPrefix == null || currentPrefix.equals("n/") || currentPrefix.equals("d/")) {
                throw new ParseException(getUnknownPrefixMessage(prefix));
            }
        }
    }

    /**
     * Returns the unknown-prefix error message for event commands.
     */
    public static String getUnknownPrefixMessage(String prefix) {
        return String.format("Unknown prefix '%s'. %s", prefix, VALID_PREFIXES_MESSAGE);
    }
}
