package seedu.address.model;

import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.ThemeMode;

public class UserPrefsTest {

    @Test
    public void setGuiSettings_nullGuiSettings_throwsNullPointerException() {
        UserPrefs userPref = new UserPrefs();
        assertThrows(NullPointerException.class, () -> userPref.setGuiSettings(null));
    }

    @Test
    public void setAddressBookFilePath_nullPath_throwsNullPointerException() {
        UserPrefs userPrefs = new UserPrefs();
        assertThrows(NullPointerException.class, () -> userPrefs.setAddressBookFilePath(null));
    }

    @Test
    public void setThemeMode_nullThemeMode_throwsNullPointerException() {
        UserPrefs userPrefs = new UserPrefs();
        assertThrows(NullPointerException.class, () -> userPrefs.setThemeMode(null));
    }

    @Test
    public void defaultThemeMode_isDark() {
        UserPrefs userPrefs = new UserPrefs();
        Assertions.assertEquals(ThemeMode.DARK, userPrefs.getThemeMode());
    }

    @Test
    public void defaultOnboardingStep_isFirstStep() {
        UserPrefs userPrefs = new UserPrefs();
        Assertions.assertEquals(1, userPrefs.getOnboardingTutorialStep());
    }

    @Test
    public void setOnboardingTutorialStep_invalidStep_clampsToFirstStep() {
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setOnboardingTutorialStep(0);
        Assertions.assertEquals(1, userPrefs.getOnboardingTutorialStep());
    }

}
