package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditEventCommand.EditEventDescriptor;
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
 * Contains integration tests and unit tests for {@code EditEventCommand}.
 */
public class EditEventCommandTest {

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
    public void execute_allFieldsSpecified_success() throws Exception {
        EditEventDescriptor descriptor = new EditEventDescriptor();
        descriptor.setName(new EventName("Builder Session"));
        descriptor.setDate(new EventDate("2026-09-01"));
        descriptor.setLocation(Optional.of("NUS Innovation 4.0"));
        descriptor.setDescription(Optional.of("Hands-on prototype workshop"));

        EditEventCommand editEventCommand = new EditEventCommand(Index.fromOneBased(1), descriptor);
        CommandResult result = editEventCommand.execute(model);

        Event editedEvent = model.getFilteredEventList().get(0);

        assertEquals(String.format(EditEventCommand.MESSAGE_EDIT_EVENT_SUCCESS, editedEvent),
                result.getFeedbackToUser());
        assertEquals(new EventName("Builder Session"), editedEvent.getName());
        assertEquals(new EventDate("2026-09-01"), editedEvent.getDate());
        assertEquals(Optional.of("NUS Innovation 4.0"), editedEvent.getLocation());
        assertEquals(Optional.of("Hands-on prototype workshop"), editedEvent.getDescription());
        assertEquals(1, editedEvent.getParticipants().getPersonList().size());
        assertTrue(editedEvent.getParticipants().getPersonList().contains(ALICE));
    }

    @Test
    public void execute_clearOptionalFields_success() throws Exception {
        EditEventDescriptor descriptor = new EditEventDescriptor();
        descriptor.setLocation(Optional.empty());
        descriptor.setDescription(Optional.empty());

        EditEventCommand editEventCommand = new EditEventCommand(Index.fromOneBased(1), descriptor);
        editEventCommand.execute(model);

        Event editedEvent = model.getFilteredEventList().get(0);
        assertEquals(Optional.empty(), editedEvent.getLocation());
        assertEquals(Optional.empty(), editedEvent.getDescription());
        assertTrue(editedEvent.getParticipants().getPersonList().contains(ALICE));
    }

    @Test
    public void execute_duplicateEvent_failure() {
        EditEventDescriptor descriptor = new EditEventDescriptor();
        descriptor.setName(new EventName(EVENT_TWO.getName().fullName));

        EditEventCommand editEventCommand = new EditEventCommand(Index.fromOneBased(1), descriptor);

        assertThrows(CommandException.class,
                EditEventCommand.MESSAGE_DUPLICATE_EVENT, () -> editEventCommand.execute(model));
    }

    @Test
    public void execute_invalidEventIndex_failure() {
        EditEventDescriptor descriptor = new EditEventDescriptor();
        descriptor.setName(new EventName("Updated Event"));

        EditEventCommand editEventCommand = new EditEventCommand(Index.fromOneBased(3), descriptor);

        assertThrows(CommandException.class,
                EditEventCommand.MESSAGE_INVALID_EVENT_DISPLAYED_INDEX, () -> editEventCommand.execute(model));
    }

    @Test
    public void execute_inParticipantsMode_failure() {
        model.enterEvent(model.getFilteredEventList().get(0));

        EditEventDescriptor descriptor = new EditEventDescriptor();
        descriptor.setName(new EventName("Updated Event"));

        EditEventCommand editEventCommand = new EditEventCommand(Index.fromOneBased(1), descriptor);

        assertThrows(CommandException.class,
                EditEventCommand.MESSAGE_LEAVE_EVENT_VIEW_FIRST, () -> editEventCommand.execute(model));
    }

    @Test
    public void equals() {
        EditEventDescriptor firstDescriptor = new EditEventDescriptor();
        firstDescriptor.setName(new EventName("Event One Updated"));
        EditEventDescriptor secondDescriptor = new EditEventDescriptor();
        secondDescriptor.setDate(new EventDate("2026-12-31"));

        EditEventCommand firstCommand = new EditEventCommand(Index.fromOneBased(1), firstDescriptor);
        EditEventCommand secondCommand = new EditEventCommand(Index.fromOneBased(2), secondDescriptor);

        assertTrue(firstCommand.equals(firstCommand));
        assertTrue(firstCommand.equals(new EditEventCommand(Index.fromOneBased(1), firstDescriptor)));
        assertFalse(firstCommand.equals(secondCommand));
        assertFalse(firstCommand.equals(null));
        assertFalse(firstCommand.equals(1));
    }

    @Test
    public void toStringMethod() {
        EditEventDescriptor descriptor = new EditEventDescriptor();
        descriptor.setName(new EventName("Event One Updated"));
        EditEventCommand editEventCommand = new EditEventCommand(Index.fromOneBased(1), descriptor);

        String expected = EditEventCommand.class.getCanonicalName()
                + "{index=" + Index.fromOneBased(1) + ", editEventDescriptor=" + descriptor + "}";
        assertEquals(expected, editEventCommand.toString());
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
