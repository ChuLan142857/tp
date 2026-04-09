---
layout: default.md
title: "User Guide - Participant Commands"
pageNav: 3
---

# Participant Commands

This page describes commands that are used while you are inside an event and managing that event's participants.

---

## 1. Participant Management

### 1.1 Add command

Used to add an applicant to the currently entered event.

#### Format
`add n/[NAME] p/[PHONE] e/[EMAIL] a/[ADDRESS] [tm/TEAM] [g/GITHUB_USERNAME] [r/RSVP_STATUS] [t/TAG]...`

#### Example Usage
`add n/John Doe p/98765432 e/johnd@example.com a/311, Clementi Ave 2, #02-25 tm/Development g/johndoe r/yes t/friends`

#### Successful Execution
`New applicant added: ...`

#### Notes
- Can only be used inside an event.
- Name, phone, email, and address are required.
- `RSVP_STATUS` must be `yes`, `no`, or `pending`.
- Team names must be alphanumeric and at most 15 characters.

### 1.2 Edit command

Used to edit the details of an existing applicant in the current event.

#### Format
`edit [INDEX] [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [g/GITHUB_USERNAME] [r/RSVP_STATUS] [tm/TEAM] [t/TAG]...`

#### Example Usage
`edit 1 p/91234567 e/johndoe@example.com`

#### Successful Execution
`Edited Applicant: ...`

#### Notes
- Can only be used inside an event.
- Index must be a positive integer.
- At least one field to edit must be provided.

### 1.3 Delete command

Used to delete an applicant from the current event.

#### Format
`delete [INDEX]`

#### Example Usage
`delete 1`

#### Successful Execution
`Deleted Applicant: ...`

#### Notes
- Can only be used inside an event.
- Index must be a positive integer.

### 1.4 Clear command

Used to clear all applicants from the current event.

#### Format
`clear`

#### Example Usage
`clear`

#### Successful Execution
`Address book has been cleared!`

#### Notes
- Can only be used inside an event.
- This removes all applicants from the current event.

---

## 2. Team and Attendance Management

### 2.1 Assign Team command

Used to assign a participant to a team.

#### Format
`assign [INDEX] team/[TEAM NAME]`

#### Example Usage
`assign 2 team/Alpha`

#### Successful Execution
`Assigned ... to Team Alpha.`

#### Notes
- Can only be used inside an event.
- Index must be a positive integer.
- Team names should be concise and valid according to app rules.

### 2.2 Check-In command

Used to mark an applicant as checked in.

#### Format
`checkin [INDEX]`

#### Example Usage
```
checkin 3
```
![Command](images/checkin/command.png)

#### Successful Execution

![Result](images/checkin/result.png)

#### Notes
- Can only be used inside an event.
- Index must be a positive integer.

---

## 3. Search, Filtering, and Viewing

### 3.1 Filter command

Used to filter the participant list using one criterion at a time.

#### Format
`filter r/[RSVP_STATUS]`
`filter t/[TAG]`
`filter team/[TEAM NAME]`
`filter checkin/[yes|no]`

#### Example Usage
`filter team/Alpha`

#### Successful Execution
Matching applicants are shown in the participant list.

#### Notes
- Can only be used inside an event.
- Supported prefixes are `r/`, `t/`, `team/`, and `checkin/`.

### 3.2 View command

Used to show the details of a selected applicant.

#### Format
`view [INDEX]`

#### Example Usage
`view 1`

#### Successful Execution
`Viewed Applicant [1]`

#### Notes
- Can only be used inside an event.
- Index must be a positive integer.

### 3.3 Statistics command

Used to display the current event's participant statistics summary.

#### Format
`statistics`

#### Example Usage
`statistics`

#### Successful Execution
`Showing the event's statistics summary.`

#### Notes
- Can only be used inside an event.

---

## 4. Import and Export

### 4.1 Import command

Used to import applicants from a CSV file into the current event.

#### Format
`import [FILE_PATH]`
`import list`

#### Example Usage
`import data/participants.csv`

#### Successful Execution
Applicants from the CSV file are imported into the current event. Invalid rows and duplicates are skipped and reported.

#### Notes
- Can only be used inside an event.
- Only `.csv` files are supported.
- `import list` shows discoverable CSV files.
- Required CSV headers are `name`, `phone`, `email`, and `address`.

### 4.2 Export command

Used to export applicants from the current event to a CSV file.

#### Format
`export [FILE_PATH]`

#### Example Usage
`export data/exports/team-event.csv`

#### Successful Execution
`Exported ... participant(s) to ...`

#### Notes
- Can only be used inside an event.
- Only `.csv` files are supported.
- If no file path is provided, the default export path is used.
- If the target file already exists, the app exports to a timestamped file instead.

---

## 5. Event Navigation

### 5.1 Leave Event command

Used to leave the current event and return to the event list.

#### Format
`leave event`

#### Example Usage
```
leave event
```
![Command](images/leave-event/command.png)

#### Successful Execution

![Result](images/leave-event/result.png)

#### Notes
- Can only be used inside an event.
- Ensure that there is no space after `event`.

---

## 6. Navigation

- [Back to Introduction, Modes, and Common Commands](UG.md)
- [Back to Common Commands](UserGuideCommonCommands.md)
- [Back to Event Commands](UserGuideEvents.md)
