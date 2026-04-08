package seedu.address.logic.onboarding;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.logic.Logic;
import seedu.address.logic.LogicManager;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.AddEventCommand;
import seedu.address.logic.commands.AssignTeamCommand;
import seedu.address.logic.commands.EnterEventCommand;
import seedu.address.logic.commands.SearchCommand;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.event.Event;
import seedu.address.model.event.EventDate;
import seedu.address.model.event.EventName;
import seedu.address.model.person.Person;
import seedu.address.storage.JsonAddressBookStorage;
import seedu.address.storage.JsonEventBookStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.StorageManager;
import seedu.address.testutil.PersonBuilder;

public class OnboardingCoordinatorTest {

    @TempDir
    public Path tempFolder;

    private Logic logic;
    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager();
        StorageManager storage = new StorageManager(
                new JsonAddressBookStorage(tempFolder.resolve("addressBook.json")),
                new JsonEventBookStorage(tempFolder.resolve("eventBook.json")),
                new JsonUserPrefsStorage(tempFolder.resolve("userPrefs.json")));
        logic = new LogicManager(model, storage);
    }

    @Test
    public void extractCommandWord_validInput_returnsFirstWord() {
        assertEquals(Optional.of("addevent"), OnboardingCoordinator.extractCommandWord("addevent n/Hi"));
        assertEquals(Optional.of("enter"), OnboardingCoordinator.extractCommandWord("enter event 1"));
    }

    @Test
    public void onCommandExecuted_wrongCommandOnSuccess_showsReminderWithoutAdvancing() {
        OnboardingCoordinator c = new OnboardingCoordinator();
        Optional<String> extra = c.onCommandExecuted("list", true, logic);
        assertTrue(extra.isPresent());
        assertTrue(extra.get().contains("tutorial still expects"));
        assertFalse(c.isFlowFinishedInSession());
        extra = c.onCommandExecuted(AddEventCommand.COMMAND_WORD + " n/Test d/2026-01-01", true, logic);
        assertTrue(extra.isPresent());
        assertTrue(extra.get().contains("Next"));
        assertFalse(c.isFlowFinishedInSession());
    }

    @Test
    public void onCommandExecuted_failure_showsStepReminder() {
        OnboardingCoordinator c = new OnboardingCoordinator();
        Optional<String> extra = c.onCommandExecuted("bad", false, logic);
        assertTrue(extra.isPresent());
        assertTrue(extra.get().contains("Onboarding"));
    }

    @Test
    public void afterAddEvent_messageMentionsCreatedEventName() {
        Event event = new Event(
                new EventName("TechMeetup"),
                new EventDate("2026-06-15"),
                Optional.empty(),
                Optional.empty());
        model.addEvent(event);

        OnboardingCoordinator c = new OnboardingCoordinator();
        Optional<String> msg = c.onCommandExecuted(
                AddEventCommand.COMMAND_WORD + " n/X d/2026-01-01", true, logic);
        assertTrue(msg.isPresent());
        assertTrue(msg.get().contains("TechMeetup"));
        assertTrue(msg.get().contains("enter event"));
    }

    @Test
    public void fullSequence_completesFlow() {
        Event event = new Event(
                new EventName("Bootcamp"),
                new EventDate("2026-03-01"),
                Optional.empty(),
                Optional.empty());
        model.addEvent(event);
        model.enterEvent(event);

        Person person = new PersonBuilder().withName("Alex Yeoh").build();
        model.addPerson(person);

        OnboardingCoordinator c = new OnboardingCoordinator();
        c.onCommandExecuted(AddEventCommand.COMMAND_WORD + " n/X d/2026-01-01", true, logic);
        c.onCommandExecuted(EnterEventCommand.COMMAND_WORD + " event 1", true, logic);
        c.onCommandExecuted(AddCommand.COMMAND_WORD + " n/P p/1 e/a@a.com a/addr", true, logic);

        Person withTeam = new PersonBuilder(person).withTeam("Alpha").build();
        model.setPerson(person, withTeam);

        c.onCommandExecuted(AssignTeamCommand.COMMAND_WORD + " 1 team/Alpha", true, logic);
        c.onCommandExecuted(SearchCommand.COMMAND_WORD + " Alex", true, logic);
        assertTrue(c.isFlowFinishedInSession());
    }

    @Test
    public void restoreProgress_invalidStoredStep_clampsToFirstStep() {
        OnboardingCoordinator c = new OnboardingCoordinator();
        c.setCurrentStep(0);

        String reminder = c.getCurrentStepReminder(logic);
        assertTrue(reminder.contains("Step 1/5"));
        assertTrue(reminder.contains(AddEventCommand.COMMAND_WORD));
    }

    @Test
    public void restoreProgress_resumesParticipantSteps_withoutAutoEnteringEvent() {
        Event event = new Event(
                new EventName("Hack Day"),
                new EventDate("2026-04-01"),
                Optional.empty(),
                Optional.empty());
        event.addParticipant(new PersonBuilder().withName("Mia Tan").withTeam("Alpha").build());
        model.addEvent(event);

        OnboardingCoordinator c = new OnboardingCoordinator();
        c.setCurrentStep(5);
        c.restoreProgress(logic);

        assertFalse(logic.isInEventParticipantsMode());

        String reminder = c.getCurrentStepReminder(logic);
        assertTrue(reminder.contains("Step 5/5"));
        assertTrue(reminder.contains("Resume by reopening"));
        assertTrue(reminder.contains("Hack Day"));
    }

    @Test
    public void onCommandExecuted_enterEventDuringResumedParticipantStep_keepsCurrentStep() {
        Event event = new Event(
                new EventName("Hack Day"),
                new EventDate("2026-04-01"),
                Optional.empty(),
                Optional.empty());
        event.addParticipant(new PersonBuilder().withName("Mia Tan").withTeam("Alpha").build());
        model.addEvent(event);

        OnboardingCoordinator c = new OnboardingCoordinator();
        c.setCurrentStep(5);
        c.restoreProgress(logic);

        model.enterEvent(event);
        Optional<String> message = c.onCommandExecuted(EnterEventCommand.COMMAND_WORD + " event 1", true, logic);

        assertTrue(message.isPresent());
        assertTrue(message.get().contains("Step 5/5"));
        assertFalse(c.isFlowFinishedInSession());
    }
}
