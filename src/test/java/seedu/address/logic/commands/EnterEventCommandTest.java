package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.EventBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.event.Event;
import seedu.address.model.event.EventDate;
import seedu.address.model.event.EventName;

/**
 * Contains integration tests and unit tests for {@code EnterEventCommand}.
 */
public class EnterEventCommandTest {

    private static final Event EVENT_ONE = createEvent("Tech Meetup", "2026-06-15",
            Optional.of("NUS"), Optional.of("Networking night"));
    private static final Event EVENT_TWO = createEvent("Hackathon", "2026-08-20",
            Optional.of("COM1"), Optional.empty());

    private Model model;

    @BeforeEach
    public void setUp() {
        EventBook eventBook = new EventBook();
        eventBook.addEvent(copyEvent(EVENT_ONE));
        eventBook.addEvent(copyEvent(EVENT_TWO));
        model = new ModelManager(new AddressBook(), eventBook, new UserPrefs());
    }

    @Test
    public void execute_validIndex_success() throws Exception {
        Event eventToEnter = model.getFilteredEventList().get(Index.fromOneBased(1).getZeroBased());
        EnterEventCommand enterEventCommand = new EnterEventCommand(Index.fromOneBased(1));

        CommandResult result = enterEventCommand.execute(model);

        assertEquals(String.format(EnterEventCommand.MESSAGE_ENTER_EVENT_SUCCESS, eventToEnter.getName().fullName),
                result.getFeedbackToUser());
        assertTrue(model.isInEventParticipantsMode());
        assertEquals(eventToEnter.getParticipants().getPersonList(), model.getFilteredPersonList());
    }

    @Test
    public void execute_invalidIndex_failure() {
        EnterEventCommand enterEventCommand = new EnterEventCommand(Index.fromOneBased(3));

        assertThrows(CommandException.class, "The event index provided is invalid", () ->
                enterEventCommand.execute(model));
        assertFalse(model.isInEventParticipantsMode());
    }

    @Test
    public void execute_alreadyInParticipantsMode_failure() {
        model.enterEvent(model.getFilteredEventList().get(0));
        EnterEventCommand enterEventCommand = new EnterEventCommand(Index.fromOneBased(2));

        assertThrows(CommandException.class, Messages.MESSAGE_ALREADY_IN_EVENT, () ->
                enterEventCommand.execute(model));
    }

    @Test
    public void equals() {
        EnterEventCommand enterFirstCommand = new EnterEventCommand(Index.fromOneBased(1));
        EnterEventCommand enterSecondCommand = new EnterEventCommand(Index.fromOneBased(2));

        assertTrue(enterFirstCommand.equals(enterFirstCommand));
        assertTrue(enterFirstCommand.equals(new EnterEventCommand(Index.fromOneBased(1))));
        assertFalse(enterFirstCommand.equals(enterSecondCommand));
        assertFalse(enterFirstCommand.equals(null));
        assertFalse(enterFirstCommand.equals(1));
    }

    private static Event createEvent(String name, String date,
            Optional<String> location, Optional<String> description) {
        return new Event(new EventName(name), new EventDate(date), location, description);
    }

    private static Event copyEvent(Event source) {
        Event copy = createEvent(source.getName().fullName, source.getDate().toString(),
                source.getLocation(), source.getDescription());
        copy.setParticipants(source.getParticipants());
        return copy;
    }
}
