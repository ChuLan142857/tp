package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LOCATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.event.Event;
import seedu.address.model.event.EventDate;
import seedu.address.model.event.EventName;

/**
 * Edits the details of an existing event in the event book.
 */
public class EditEventCommand extends Command {

    public static final String COMMAND_WORD = "editevent";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the event identified "
            + "by the index number used in the displayed event list. Existing values will be overwritten "
            + "by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_DATE + "DATE] "
            + "[" + PREFIX_LOCATION + "LOCATION] "
            + "[" + PREFIX_DESCRIPTION + "DESCRIPTION]\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_NAME + "Hack Night "
            + PREFIX_DATE + "2026-08-20 "
            + PREFIX_LOCATION + "NUS COM1 "
            + PREFIX_DESCRIPTION + "Bring your laptop";

    public static final String MESSAGE_EDIT_EVENT_SUCCESS = "Edited Event: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_EVENT = "This event already exists in the event book";
    public static final String MESSAGE_INVALID_EVENT_DISPLAYED_INDEX = "The event index provided is invalid";
    public static final String MESSAGE_LEAVE_EVENT_VIEW_FIRST = "Leave the event view to edit an event.";

    private final Index index;
    private final EditEventDescriptor editEventDescriptor;

    /**
     * @param index of the event in the filtered event list to edit
     * @param editEventDescriptor details to edit the event with
     */
    public EditEventCommand(Index index, EditEventDescriptor editEventDescriptor) {
        requireNonNull(index);
        requireNonNull(editEventDescriptor);

        this.index = index;
        this.editEventDescriptor = new EditEventDescriptor(editEventDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.isInEventParticipantsMode()) {
            throw new CommandException(MESSAGE_LEAVE_EVENT_VIEW_FIRST);
        }

        List<Event> lastShownList = model.getFilteredEventList();
        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(MESSAGE_INVALID_EVENT_DISPLAYED_INDEX);
        }

        Event eventToEdit = lastShownList.get(index.getZeroBased());
        Event editedEvent = createEditedEvent(eventToEdit, editEventDescriptor);

        if (!eventToEdit.isSameEvent(editedEvent) && model.hasEvent(editedEvent)) {
            throw new CommandException(MESSAGE_DUPLICATE_EVENT);
        }

        model.setEvent(eventToEdit, editedEvent);
        return new CommandResult(String.format(MESSAGE_EDIT_EVENT_SUCCESS, editedEvent));
    }

    /**
     * Creates and returns an {@code Event} with the details of {@code eventToEdit}
     * edited with {@code editEventDescriptor}.
     */
    private static Event createEditedEvent(Event eventToEdit, EditEventDescriptor editEventDescriptor) {
        assert eventToEdit != null;

        EventName updatedName = editEventDescriptor.getName().orElse(eventToEdit.getName());
        EventDate updatedDate = editEventDescriptor.getDate().orElse(eventToEdit.getDate());
        Optional<String> updatedLocation = editEventDescriptor.getLocation().orElse(eventToEdit.getLocation());
        Optional<String> updatedDescription = editEventDescriptor.getDescription().orElse(eventToEdit.getDescription());

        Event editedEvent = new Event(updatedName, updatedDate, updatedLocation, updatedDescription);
        editedEvent.setParticipants(eventToEdit.getParticipants());
        return editedEvent;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof EditEventCommand)) {
            return false;
        }

        EditEventCommand otherEditEventCommand = (EditEventCommand) other;
        return index.equals(otherEditEventCommand.index)
                && editEventDescriptor.equals(otherEditEventCommand.editEventDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("editEventDescriptor", editEventDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the event with. Each non-empty field value will replace the
     * corresponding field value of the event.
     */
    public static class EditEventDescriptor {
        private EventName name;
        private EventDate date;
        private Optional<String> location;
        private Optional<String> description;

        public EditEventDescriptor() {}

        /**
         * Copy constructor.
         */
        public EditEventDescriptor(EditEventDescriptor toCopy) {
            setName(toCopy.name);
            setDate(toCopy.date);
            setLocation(toCopy.location);
            setDescription(toCopy.description);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, date, location, description);
        }

        public void setName(EventName name) {
            this.name = name;
        }

        public Optional<EventName> getName() {
            return Optional.ofNullable(name);
        }

        public void setDate(EventDate date) {
            this.date = date;
        }

        public Optional<EventDate> getDate() {
            return Optional.ofNullable(date);
        }

        public void setLocation(Optional<String> location) {
            this.location = location;
        }

        public Optional<Optional<String>> getLocation() {
            return Optional.ofNullable(location);
        }

        public void setDescription(Optional<String> description) {
            this.description = description;
        }

        public Optional<Optional<String>> getDescription() {
            return Optional.ofNullable(description);
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }
            if (!(other instanceof EditEventDescriptor)) {
                return false;
            }
            EditEventDescriptor otherDescriptor = (EditEventDescriptor) other;
            return Objects.equals(name, otherDescriptor.name)
                    && Objects.equals(date, otherDescriptor.date)
                    && Objects.equals(location, otherDescriptor.location)
                    && Objects.equals(description, otherDescriptor.description);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, date, location, description);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("name", name)
                    .add("date", date)
                    .add("location", location)
                    .add("description", description)
                    .toString();
        }
    }
}
