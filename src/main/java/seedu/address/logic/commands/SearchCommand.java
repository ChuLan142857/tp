package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.event.EventMatchesKeywordsPredicate;
import seedu.address.model.person.NameContainsKeywordsPredicate;


/**
 * Searches and lists matching events or persons depending on the current app mode.
 */
public class SearchCommand extends Command {

    public static final String COMMAND_WORD = "search";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Finds matching events before entering an event, or matching participants after entering one.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " meetup workshop";

    private final List<String> keywords;

    public SearchCommand(List<String> keywords) {
        this.keywords = List.copyOf(keywords);
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        if (model.isInEventParticipantsMode()) {
            model.updateFilteredPersonList(new NameContainsKeywordsPredicate(keywords));
            return new CommandResult(
                    String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getFilteredPersonList().size()));
        }

        model.updateFilteredEventList(new EventMatchesKeywordsPredicate(keywords));
        return new CommandResult(
                String.format(Messages.MESSAGE_EVENTS_LISTED_OVERVIEW, model.getFilteredEventList().size()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof SearchCommand)) {
            return false;
        }

        SearchCommand otherSearchCommand = (SearchCommand) other;
        return keywords.equals(otherSearchCommand.keywords);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("keywords", keywords)
                .toString();
    }
}
