# Study Organizer (Java)

Study Organizer is a simple Java console application built to help students organize study groups and plan study sessions easily.

This project was developed as a group project for practicing Object-Oriented Programming (OOP) concepts in Java, such as inheritance, abstraction, encapsulation, and polymorphism.

---

## What This Project Does

With this program, a student can:

- Register and log in
- Choose a subject from a subject list
- View available groups for that subject
- Join or leave a study group
- View group members and the group leader
- Schedule study sessions (only the leader can do this)

The first student who joins a group automatically becomes the group leader.

---

## Study Session Types

The leader can schedule two types of sessions:

### Physical Session
- Includes a location (example: Library, Classroom)

### Online Session
- Includes an online link (example: Google Meet / Zoom)

All scheduled sessions are saved into a file called:

sessions.txt

---

## OOP Concepts Used

This project includes key OOP concepts:

- Abstraction → User is an abstract class
- Inheritance → Student extends User
- Polymorphism → StudySession has different types (PhysicalSession, OnlineSession)
- Encapsulation → private fields with getters

---

## How to Run the Program

1. Clone the repository:
   `bash
   git clone https://github.com/your-swee-t24/Study-Group-Organizer.git
