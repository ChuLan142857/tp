package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
 * Contains integration tests for {@code LeaveEventCommand}.
 */
public class LeaveEventCommandTest {

    private static final Event EVENT_ONE = createEvent("Tech Meetup", "2026-06-15",
            Optional.of("NUS"), Optional.of("Networking night"));

    private Model model;

    @BeforeEach
    public void setUp() {
        EventBook eventBook = new EventBook();
        eventBook.addEvent(copyEvent(EVENT_ONE));
        model = new ModelManager(new AddressBook(), eventBook, new UserPrefs());
    }

    @Test
    public void execute_inParticipantsMode_success() throws Exception {
        model.enterEvent(model.getFilteredEventList().get(0));
        LeaveEventCommand leaveEventCommand = new LeaveEventCommand();

        CommandResult result = leaveEventCommand.execute(model);

        assertEquals(LeaveEventCommand.MESSAGE_LEAVE_EVENT_SUCCESS, result.getFeedbackToUser());
        assertFalse(model.isInEventParticipantsMode());
    }

    @Test
    public void execute_notInParticipantsMode_failure() {
        LeaveEventCommand leaveEventCommand = new LeaveEventCommand();

        assertThrows(CommandException.class, Messages.MESSAGE_ENTER_EVENT_FIRST, () ->
                leaveEventCommand.execute(model));
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
