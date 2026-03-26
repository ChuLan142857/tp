package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
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
 * Contains integration tests and unit tests for {@code DeleteEventCommand}.
 */
public class DeleteEventCommandTest {

    private static final Event EVENT_ONE = createEvent("Tech Meetup", "2026-06-15",
            Optional.of("NUS"), Optional.of("Networking night"));
    private static final Event EVENT_TWO = createEvent("Hackathon", "2026-08-20",
            Optional.of("COM1"), Optional.empty());

    private Model model;

    @BeforeEach
    public void setUp() {
        EventBook eventBook = new EventBook();

        Event eventOne = copyEvent(EVENT_ONE);
        eventOne.addParticipant(ALICE);

        eventBook.addEvent(eventOne);
        eventBook.addEvent(copyEvent(EVENT_TWO));

        model = new ModelManager(new AddressBook(), eventBook, new UserPrefs());
    }

    @Test
    public void execute_validIndex_success() throws Exception {
        Event eventToDelete = model.getFilteredEventList().get(Index.fromOneBased(1).getZeroBased());
        DeleteEventCommand deleteEventCommand = new DeleteEventCommand(Index.fromOneBased(1));

        CommandResult result = deleteEventCommand.execute(model);

        assertEquals(String.format(DeleteEventCommand.MESSAGE_DELETE_EVENT_SUCCESS, eventToDelete),
                result.getFeedbackToUser());

        List<Event> remainingEvents = model.getFilteredEventList();
        assertEquals(1, remainingEvents.size());
        assertTrue(remainingEvents.stream().noneMatch(event -> event.isSameEvent(eventToDelete)));
        assertTrue(remainingEvents.stream().anyMatch(event -> event.isSameEvent(EVENT_TWO)));
    }

    @Test
    public void execute_invalidIndex_failure() {
        DeleteEventCommand deleteEventCommand = new DeleteEventCommand(Index.fromOneBased(3));

        assertThrows(CommandException.class,
                DeleteEventCommand.MESSAGE_INVALID_EVENT_DISPLAYED_INDEX, () -> deleteEventCommand.execute(model));
    }

    @Test
    public void execute_inParticipantsMode_failure() {
        model.enterEvent(model.getFilteredEventList().get(0));

        DeleteEventCommand deleteEventCommand = new DeleteEventCommand(Index.fromOneBased(1));

        assertThrows(CommandException.class,
                DeleteEventCommand.MESSAGE_LEAVE_EVENT_VIEW_FIRST, () -> deleteEventCommand.execute(model));
    }

    @Test
    public void equals() {
        DeleteEventCommand deleteFirstCommand = new DeleteEventCommand(Index.fromOneBased(1));
        DeleteEventCommand deleteSecondCommand = new DeleteEventCommand(Index.fromOneBased(2));

        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));
        assertTrue(deleteFirstCommand.equals(new DeleteEventCommand(Index.fromOneBased(1))));
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
        assertFalse(deleteFirstCommand.equals(null));
        assertFalse(deleteFirstCommand.equals(1));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        DeleteEventCommand deleteEventCommand = new DeleteEventCommand(targetIndex);
        String expected = DeleteEventCommand.class.getCanonicalName() + "{targetIndex=" + targetIndex + "}";
        assertEquals(expected, deleteEventCommand.toString());
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
