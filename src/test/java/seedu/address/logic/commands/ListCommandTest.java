package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.enterDefaultEvent;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;
import seedu.address.model.EventBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.event.Event;
import seedu.address.model.event.EventDate;
import seedu.address.model.event.EventMatchesKeywordsPredicate;
import seedu.address.model.event.EventName;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ListCommand.
 */
public class ListCommandTest {

    @Test
    public void execute_globalMode_showsAllEvents() {
        EventBook eventBook = buildEventBook();
        Model model = new ModelManager(new AddressBook(), eventBook, new UserPrefs());
        Model expectedModel = new ModelManager(new AddressBook(), eventBook, new UserPrefs());

        assertCommandSuccess(new ListCommand(), model, ListCommand.MESSAGE_SUCCESS_EVENTS, expectedModel);
    }

    @Test
    public void execute_filteredGlobalMode_showsAllEvents() {
        EventBook eventBook = buildEventBook();
        Model model = new ModelManager(new AddressBook(), eventBook, new UserPrefs());
        Model expectedModel = new ModelManager(new AddressBook(), eventBook, new UserPrefs());

        model.updateFilteredEventList(new EventMatchesKeywordsPredicate(java.util.List.of("Tech")));
        assertCommandSuccess(new ListCommand(), model, ListCommand.MESSAGE_SUCCESS_EVENTS, expectedModel);
    }

    @Test
    public void execute_eventMode_showsAllParticipants() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        enterDefaultEvent(model);
        enterDefaultEvent(expectedModel);

        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        assertCommandSuccess(new ListCommand(), model,
                ListCommand.MESSAGE_SUCCESS_PARTICIPANTS, expectedModel);
    }

    private EventBook buildEventBook() {
        EventBook eventBook = new EventBook();
        eventBook.addEvent(buildEvent("Tech Meetup", "2026-06-15",
                Optional.of("COM1"), Optional.of("Frontend sharing")));
        eventBook.addEvent(buildEvent("Design Jam", "2026-08-10",
                Optional.of("NUS"), Optional.of("UI critique")));
        return eventBook;
    }

    private Event buildEvent(String name, String date, Optional<String> location, Optional<String> description) {
        return new Event(new EventName(name), new EventDate(date), location, description);
    }
}
