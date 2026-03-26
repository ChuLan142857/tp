package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.event.Event;

/**
 * Deletes an event identified using its displayed index from the event book.
 */
public class DeleteEventCommand extends Command {

    public static final String COMMAND_WORD = "deleteevent";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the event identified by the index number used in the displayed event list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_EVENT_SUCCESS =
            "Deleted Event: %1$s. Its participant list was deleted together with the event.";
    public static final String MESSAGE_INVALID_EVENT_DISPLAYED_INDEX = "The event index provided is invalid";
    public static final String MESSAGE_LEAVE_EVENT_VIEW_FIRST = "Leave the event view to delete an event.";

    private final Index targetIndex;

    public DeleteEventCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        if (model.isInEventParticipantsMode()) {
            throw new CommandException(MESSAGE_LEAVE_EVENT_VIEW_FIRST);
        }

        List<Event> lastShownList = model.getFilteredEventList();
        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(MESSAGE_INVALID_EVENT_DISPLAYED_INDEX);
        }

        Event eventToDelete = lastShownList.get(targetIndex.getZeroBased());
        model.deleteEvent(eventToDelete);
        return new CommandResult(String.format(MESSAGE_DELETE_EVENT_SUCCESS, eventToDelete));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof DeleteEventCommand)) {
            return false;
        }

        DeleteEventCommand otherDeleteEventCommand = (DeleteEventCommand) other;
        return targetIndex.equals(otherDeleteEventCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }
}
