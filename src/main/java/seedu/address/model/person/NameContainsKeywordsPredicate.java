package seedu.address.model.person;

import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;
import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person}'s name, email, or GitHub username matches any of the keywords given.
 */
public class NameContainsKeywordsPredicate implements Predicate<Person> {
    private final List<String> keywords;

    public NameContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Person person) {
        return keywords.stream()
                .filter(keyword -> !keyword.trim().isEmpty())
                .anyMatch(keyword -> matchesName(person, keyword)
                        || matchesEmail(person, keyword)
                        || matchesGitHub(person, keyword));
    }

    private boolean matchesName(Person person, String keyword) {
        return StringUtil.containsWordIgnoreCase(person.getName().fullName, keyword);
    }

    private boolean matchesEmail(Person person, String keyword) {
        return containsIgnoreCase(person.getEmail().value, keyword);
    }

    private boolean matchesGitHub(Person person, String keyword) {
        return person.getGitHub()
                .map(github -> containsIgnoreCase(github.value, keyword))
                .orElse(false);
    }

    private boolean containsIgnoreCase(String value, String keyword) {
        String normalizedKeyword = keyword.trim().toLowerCase(Locale.ROOT);
        return value.toLowerCase(Locale.ROOT).contains(normalizedKeyword);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof NameContainsKeywordsPredicate)) {
            return false;
        }

        NameContainsKeywordsPredicate otherNameContainsKeywordsPredicate = (NameContainsKeywordsPredicate) other;
        return keywords.equals(otherNameContainsKeywordsPredicate.keywords);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("keywords", keywords).toString();
    }
}
