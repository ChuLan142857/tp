package seedu.address.model.event;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.Test;

public class EventMatchesKeywordsPredicateTest {

    @Test
    public void equals() {
        EventMatchesKeywordsPredicate firstPredicate =
                new EventMatchesKeywordsPredicate(Collections.singletonList("first"));
        EventMatchesKeywordsPredicate secondPredicate =
                new EventMatchesKeywordsPredicate(Collections.singletonList("second"));

        assertTrue(firstPredicate.equals(firstPredicate));
        assertTrue(firstPredicate.equals(new EventMatchesKeywordsPredicate(Collections.singletonList("first"))));
        assertFalse(firstPredicate.equals(1));
        assertFalse(firstPredicate.equals(null));
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_matchesVisibleFields_returnsTrue() {
        Event event = new Event(
                new EventName("Tech Meetup"),
                new EventDate("2026-06-15"),
                Optional.of("Marina Bay Sands"),
                Optional.of("Monthly networking session"));

        assertTrue(new EventMatchesKeywordsPredicate(Collections.singletonList("tech")).test(event));
        assertTrue(new EventMatchesKeywordsPredicate(Collections.singletonList("2026-06-15")).test(event));
        assertTrue(new EventMatchesKeywordsPredicate(Collections.singletonList("marina")).test(event));
        assertTrue(new EventMatchesKeywordsPredicate(Arrays.asList("networking", "hackathon")).test(event));
    }

    @Test
    public void test_noMatches_returnsFalse() {
        Event event = new Event(
                new EventName("Tech Meetup"),
                new EventDate("2026-06-15"),
                Optional.of("Marina Bay Sands"),
                Optional.of("Monthly networking session"));

        assertFalse(new EventMatchesKeywordsPredicate(Collections.emptyList()).test(event));
        assertFalse(new EventMatchesKeywordsPredicate(Collections.singletonList("workshop")).test(event));
    }
}
