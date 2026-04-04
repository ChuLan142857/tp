package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.ImportCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates an {@link ImportCommand} object.
 */
public class ImportCommandParser implements Parser<ImportCommand> {

    private static final String LIST_KEYWORD = "list";
    private static final String LIST_FLAG = "--list";

    @Override
    public ImportCommand parse(String args) throws ParseException {
        if (args == null) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ImportCommand.MESSAGE_USAGE));
        }

        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            return new ImportCommand(true);
        }

        if (trimmedArgs.equalsIgnoreCase(LIST_KEYWORD) || trimmedArgs.equalsIgnoreCase(LIST_FLAG)) {
            return new ImportCommand(true);
        }

        return new ImportCommand(trimmedArgs);
    }
}
