---
layout: default.md
title: "User Guide"
pageNav: 3
---

# TeamEventPro User Guide

## 1. About TeamEventPro

TeamEventPro is a desktop event-operations app designed for organizers who need to manage events and participants quickly and accurately.

It is built for keyboard-first workflows. Most actions can be completed with short commands, which is helpful during live registration, check-in queues, team assignment, and last-minute updates.

TeamEventPro combines the speed of a Command Line Interface (CLI) with the clarity of a Graphical User Interface (GUI). This allows you to work quickly while still seeing lists and updates clearly on screen.

![TeamEventPro User Guide Home Page](images/MainPage/MainPage.png)

---

## 2. Quick Start

1. Ensure Java `17` or above is installed.
   - Mac users should use the project's required JDK version.
2. Download the latest `.jar` from the project release page.
3. Place the `.jar` in your preferred TeamEventPro home folder.
4. Open a terminal in that folder and run `java -jar addressbook.jar`.
5. Wait a few seconds for the app to launch.

   You should see the application main page below:

   ![TeamEventPro Application Main Page](images/MainPage/MainPage_real.png)

6. Type commands in the command box and press Enter to run them.
7. If onboarding appears on first launch, complete it once to get familiar with navigation and command flow.

### 2.1 Suggested First Commands

- `help`
- `addevent n/Tech Meetup d/2026-06-15 l/NUS COM1 desc/Evening session`
- `list`
- `enter event 1`
- `add n/John Doe p/98765432 e/john@example.com a/311 Clementi Ave 2`
- `checkin 1`
- `leave`

---

## 3. Understanding App Modes

TeamEventPro has two working modes.

### 3.1 Outside an event

You are viewing and managing the event list.

You can use this mode to:
- create events
- edit event details
- delete events
- search for events
- enter a specific event

### 3.2 Inside an event

You are managing participants for the selected event.

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

- Words in `UPPER_CASE` are values you provide.
- Items in square brackets are optional.
- Items followed by `...` can be repeated.
- Parameter order usually does not matter unless otherwise stated.
- `INDEX` refers to the number shown in the currently displayed list.
- Dates use `YYYY-MM-DD` format.

---

## 5. Commands Available in Both Modes

These commands are available both inside and outside an event:

- `help`
- `list`
- `search`
- `switchmode`

Full details for these commands are in [Common Commands](UserGuideCommonCommands.md).

---

## 6. Next Sections

- [Common Commands](UserGuideCommonCommands.md): Commands shared across both modes.
- [Event Commands](UserGuideEvents.md): Commands for creating and managing events.
- [Participant Commands](UserGuideParticipants.md): Commands for managing participants inside an event.
