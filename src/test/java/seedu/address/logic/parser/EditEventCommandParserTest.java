package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LOCATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditEventCommand;
import seedu.address.logic.commands.EditEventCommand.EditEventDescriptor;
import seedu.address.model.event.EventDate;
import seedu.address.model.event.EventName;

public class EditEventCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditEventCommand.MESSAGE_USAGE);

    private final EditEventCommandParser parser = new EditEventCommandParser();

    @Test
    public void parse_missingParts_failure() {
        assertParseFailure(parser, PREFIX_NAME + "Tech Meetup", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "1", EditEventCommand.MESSAGE_NOT_EDITED);
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        assertParseFailure(parser, "-1 " + PREFIX_NAME + "Tech Meetup", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "0 " + PREFIX_NAME + "Tech Meetup", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser, "1 " + PREFIX_NAME + "!! Invalid @@ ", EventName.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1 " + PREFIX_DATE + "not-a-date", EventDate.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1 " + PREFIX_DATE + "2026-02-30", EventDate.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        EditEventDescriptor descriptor = new EditEventDescriptor();
        descriptor.setName(new EventName("Builder Session"));
        descriptor.setDate(new EventDate("2026-09-01"));
        descriptor.setLocation(Optional.of("NUS Innovation 4.0"));
        descriptor.setDescription(Optional.of("Hands-on prototype workshop"));

        EditEventCommand expectedCommand = new EditEventCommand(Index.fromOneBased(1), descriptor);

        assertParseSuccess(parser,
                " 1 "
                        + PREFIX_NAME + "Builder Session "
                        + PREFIX_DATE + "2026-09-01 "
                        + PREFIX_LOCATION + "NUS Innovation 4.0 "
                        + PREFIX_DESCRIPTION + "Hands-on prototype workshop",
                expectedCommand);
    }

    @Test
    public void parse_someFieldsSpecified_success() {
        EditEventDescriptor descriptor = new EditEventDescriptor();
        descriptor.setDate(new EventDate("2026-09-01"));

        EditEventCommand expectedCommand = new EditEventCommand(Index.fromOneBased(2), descriptor);

        assertParseSuccess(parser,
                " 2 " + PREFIX_DATE + "2026-09-01",
                expectedCommand);
    }

    @Test
    public void parse_clearOptionalFields_success() {
        EditEventDescriptor descriptor = new EditEventDescriptor();
        descriptor.setLocation(Optional.empty());
        descriptor.setDescription(Optional.empty());

        EditEventCommand expectedCommand = new EditEventCommand(Index.fromOneBased(1), descriptor);

        assertParseSuccess(parser,
                " 1 " + PREFIX_LOCATION + " " + PREFIX_DESCRIPTION,
                expectedCommand);
    }

    @Test
    public void parse_multipleRepeatedFields_failure() {
        String userInput = "1 "
                + PREFIX_NAME + "Tech Meetup "
                + PREFIX_NAME + "Hack Night "
                + PREFIX_DATE + "2026-06-15 "
                + PREFIX_DATE + "2026-07-01";

        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME, PREFIX_DATE));
    }
}
