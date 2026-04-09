package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.ThemeMode;

/**
 * Represents User's preferences.
 */
public class UserPrefs implements ReadOnlyUserPrefs {

    private GuiSettings guiSettings = new GuiSettings();
    private Path addressBookFilePath = Paths.get("data" , "addressbook.json");
    private boolean onboardingCompleted;
    private int onboardingTutorialStep = 1;
    private ThemeMode themeMode = ThemeMode.DARK;

    /**
     * Creates a {@code UserPrefs} with default values.
     */
    public UserPrefs() {}

    /**
     * Creates a {@code UserPrefs} with the prefs in {@code userPrefs}.
     */
    public UserPrefs(ReadOnlyUserPrefs userPrefs) {
        this();
        resetData(userPrefs);
    }

    /**
     * Resets the existing data of this {@code UserPrefs} with {@code newUserPrefs}.
     */
    public void resetData(ReadOnlyUserPrefs newUserPrefs) {
        requireNonNull(newUserPrefs);
        setGuiSettings(newUserPrefs.getGuiSettings());
        setAddressBookFilePath(newUserPrefs.getAddressBookFilePath());
        setOnboardingCompleted(newUserPrefs.isOnboardingCompleted());
        setOnboardingTutorialStep(newUserPrefs.getOnboardingTutorialStep());
        setThemeMode(newUserPrefs.getThemeMode());
    }

    public GuiSettings getGuiSettings() {
        return guiSettings;
    }

    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        this.guiSettings = guiSettings;
    }

    public Path getAddressBookFilePath() {
        return addressBookFilePath;
    }

    public void setAddressBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        this.addressBookFilePath = addressBookFilePath;
    }

    @Override
    public boolean isOnboardingCompleted() {
        return onboardingCompleted;
    }

    public void setOnboardingCompleted(boolean onboardingCompleted) {
        this.onboardingCompleted = onboardingCompleted;
    }

    public int getOnboardingTutorialStep() {
        return onboardingTutorialStep;
    }

    public void setOnboardingTutorialStep(int onboardingTutorialStep) {
        this.onboardingTutorialStep = Math.max(1, onboardingTutorialStep);
    }

    public ThemeMode getThemeMode() {
        return themeMode;
    }

    public void setThemeMode(ThemeMode themeMode) {
        requireNonNull(themeMode);
        this.themeMode = themeMode;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UserPrefs)) {
            return false;
        }

        UserPrefs otherUserPrefs = (UserPrefs) other;
        return guiSettings.equals(otherUserPrefs.guiSettings)
                && addressBookFilePath.equals(otherUserPrefs.addressBookFilePath)
                && onboardingCompleted == otherUserPrefs.onboardingCompleted
                && onboardingTutorialStep == otherUserPrefs.onboardingTutorialStep
                && themeMode == otherUserPrefs.themeMode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(guiSettings, addressBookFilePath, onboardingCompleted, onboardingTutorialStep, themeMode);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Gui Settings : " + guiSettings);
        sb.append("\nLocal data file location : " + addressBookFilePath);
        sb.append("\nOnboarding completed : " + onboardingCompleted);
        sb.append("\nOnboarding tutorial step : " + onboardingTutorialStep);
        sb.append("\nTheme mode : " + themeMode);
        return sb.toString();
    }

}
