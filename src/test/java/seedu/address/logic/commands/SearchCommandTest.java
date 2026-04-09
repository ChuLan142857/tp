package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_EVENTS_LISTED_OVERVIEW;
import static seedu.address.logic.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.enterDefaultEvent;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.ELLE;
import static seedu.address.testutil.TypicalPersons.FIONA;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collections;
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
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code SearchCommand}.
 */
public class SearchCommandTest {
    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private final Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        SearchCommand searchFirstCommand = new SearchCommand(Collections.singletonList("first"));
        SearchCommand searchSecondCommand = new SearchCommand(Collections.singletonList("second"));

        // same object -> returns true
        assertTrue(searchFirstCommand.equals(searchFirstCommand));

        // same values -> returns true
        SearchCommand searchFirstCommandCopy = new SearchCommand(Collections.singletonList("first"));
        assertTrue(searchFirstCommand.equals(searchFirstCommandCopy));

        // different types -> returns false
        assertFalse(searchFirstCommand.equals(1));

        // null -> returns false
        assertFalse(searchFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(searchFirstCommand.equals(searchSecondCommand));
    }

    @Test
    public void execute_globalMode_filtersEvents() {
        EventBook eventBook = new EventBook();
        Event techMeetup = buildEvent("Tech Meetup", "2026-06-10",
                Optional.of("COM1"), Optional.of("Frontend sharing session"));
        Event aiWorkshop = buildEvent("AI Workshop", "2026-07-20",
                Optional.of("SMU Labs"), Optional.of("Machine learning basics"));
        Event designJam = buildEvent("Design Jam", "2026-08-15",
                Optional.of("NUS"), Optional.of("UI critique"));
        eventBook.addEvent(techMeetup);
        eventBook.addEvent(aiWorkshop);
        eventBook.addEvent(designJam);

        Model localModel = new ModelManager(new AddressBook(), eventBook, new UserPrefs());
        Model expectedLocalModel = new ModelManager(new AddressBook(), eventBook, new UserPrefs());
        SearchCommand command = new SearchCommand(Arrays.asList("machine", "com1"));

        expectedLocalModel.updateFilteredEventList(
                new EventMatchesKeywordsPredicate(Arrays.asList("machine", "com1")));
        assertCommandSuccess(command, localModel,
                String.format(MESSAGE_EVENTS_LISTED_OVERVIEW, 2), expectedLocalModel);
        assertEquals(Arrays.asList(techMeetup, aiWorkshop), localModel.getFilteredEventList());
    }

    @Test
    public void execute_eventMode_filtersPersons() {
        enterDefaultEvent(model);
        enterDefaultEvent(expectedModel);

        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 3);
        NameContainsKeywordsPredicate predicate = preparePredicate("Kurz Elle Kunz");
        SearchCommand command = new SearchCommand(Arrays.asList("Kurz", "Elle", "Kunz"));
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(CARL, ELLE, FIONA), model.getFilteredPersonList());
    }

    @Test
    public void execute_emailAndGitHubKeywords_multiplePersonsFound() {
        Person alex = new PersonBuilder().withName("Alex Yeoh")
                .withEmail("alexyeoh@example.com")
                .withGitHub("alexyeoh")
                .build();
        Person david = new PersonBuilder().withName("David Li")
                .withEmail("lidavid@example.com")
                .withGitHub("lidavid")
                .build();
        Person charlotte = new PersonBuilder().withName("Charlotte Oliveiro")
                .withEmail("charlotte@example.com")
                .withGitHub(null)
                .build();
        AddressBook addressBook = new AddressBook();
        addressBook.addPerson(alex);
        addressBook.addPerson(david);
        addressBook.addPerson(charlotte);

        Model localModel = new ModelManager(addressBook, new UserPrefs());
        Model expectedLocalModel = new ModelManager(addressBook, new UserPrefs());
        enterDefaultEvent(localModel);
        enterDefaultEvent(expectedLocalModel);
        NameContainsKeywordsPredicate predicate = preparePredicate("ALEXYEOH@EXAMPLE.COM LIDAVID");
        SearchCommand command = new SearchCommand(Arrays.asList("ALEXYEOH@EXAMPLE.COM", "LIDAVID"));

        expectedLocalModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, localModel,
                String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 2), expectedLocalModel);
        assertEquals(Arrays.asList(alex, david), localModel.getFilteredPersonList());
    }

    @Test
    public void toStringMethod() {
        SearchCommand searchCommand = new SearchCommand(Arrays.asList("keyword"));
        String expected = SearchCommand.class.getCanonicalName() + "{keywords=[keyword]}";
        assertEquals(expected, searchCommand.toString());
    }

    private Event buildEvent(String name, String date, Optional<String> location, Optional<String> description) {
        return new Event(new EventName(name), new EventDate(date), location, description);
    }

    /**
     * Parses {@code userInput} into a {@code NameContainsKeywordsPredicate}.
     */
    private NameContainsKeywordsPredicate preparePredicate(String userInput) {
        return new NameContainsKeywordsPredicate(Arrays.asList(userInput.split("\\s+")));
    }
}
