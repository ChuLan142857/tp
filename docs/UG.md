---
layout: default.md
title: "User Guide"
pageNav: 3
---

# TeamEventPro User Guide

## 1. About TeamEventPro

TeamEventPro is a desktop application designed to help users manage events and participants efficiently. It is intended for users who prefer typing commands over navigating through menus, allowing them to perform tasks quickly and consistently.

The application supports two main workflows. First, users can manage events by creating, editing, deleting, searching for, and entering events. Second, once inside an event, users can manage participants by adding or editing their details, assigning teams, checking attendance, viewing statistics, and importing or exporting participant data.

TeamEventPro provides the speed of a Command Line Interface (CLI) while still offering the visual clarity of a Graphical User Interface (GUI). This makes it suitable for users who want a structured and efficient way to handle event and participant management in a single application.

---

## 2. Quick Start

1. Ensure you have Java `17` or above installed on your computer.

   - Mac users should ensure they are using the precise JDK version required for the project.

2. Download the latest `.jar` file from the project’s release page.

3. Copy the `.jar` file into the folder you want to use as the home folder for TeamEventPro.

4. Open a terminal, navigate to the folder containing the `.jar` file, and run:

   `java -jar addressbook.jar`

5. Wait a few seconds for the application window to open.

6. Type commands into the command box and press Enter to execute them.

7. An onboarding tutorial will begin when you open the application for the first time. Please go through that to familiarize
   yourself with the application.
---

## 3. Understanding App Modes

TeamEventPro has two main modes of use.

### 3.1 Outside an event

In this mode, you are viewing and managing the list of events.

You can use this mode to:
- create events
- edit event details
- delete events
- search for events
- enter a specific event

### 3.2 Inside an event

In this mode, you are managing participants within a selected event.

You can use this mode to:
- add, edit, and delete participants
- assign participants to teams
- check in participants
- filter and view participant details
- view event statistics
- import and export participant data
- leave the current event and return to the event list

---

## 4. Notes About Command Format

- Words in `UPPER_CASE` are parameters to be supplied by the user.
- Items in square brackets are optional.
- Items followed by `...` can be used multiple times.
- Parameters can usually be entered in any order unless stated otherwise.
- Indexes refer to the numbers shown in the displayed list.
- Dates should follow the format `YYYY-MM-DD`.

---

## 5. Commands Available in Both Modes

The following commands can be used regardless of whether you are inside or outside an event:

- `help`
- `list`
- `search`
- `switchmode`

Full details for these commands are in [Common Commands](UserGuideCommonCommands.md).

---

## 6. Next Sections

- [Common Commands](UserGuideCommonCommands.md)
- [Event Commands](UserGuideEvents.md)
- [Participant Commands](UserGuideParticipants.md)
