import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StudyGroup1 {

    // ========= Base User Class =========
    abstract static class User {
        protected String id;
        protected String name;

        public User(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public abstract String getRole();

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    // ========= Student =========
    static class Student extends User {
        private int batchYear;

        public Student(String id, String name, int batchYear) {
            super(id, name);
            this.batchYear = batchYear;
        }

        @Override
        public String getRole() {
            return "Student";
        }

        public int getBatchYear() {
            return batchYear;
        }
    }

    // ========= Subject =========
    static class Subject {
        private String subjectId;
        private String subjectName;

        public Subject(String subjectId, String subjectName) {
            this.subjectId = subjectId;
            this.subjectName = subjectName;
        }

        public String getSubjectId() {
            return subjectId;
        }

        public String getSubjectName() {
            return subjectName;
        }
    }

    // ========= Study Session =========
    static class StudySession {
        protected String date;
        protected String time;
        protected String location;

        public StudySession(String date, String time, String location) {
            this.date = date;
            this.time = time;
            this.location = location;
        }

        public String getType() {
            return "General";
        }

        public void display() {
            System.out.println(getType() + " | " + date + " | " + time + " | " + location);
        }
    }

    static class PhysicalSession extends StudySession {
        public PhysicalSession(String date, String time, String location) {
            super(date, time, location);
        }

        @Override
        public String getType() {
            return "Physical";
        }
    }

    static class OnlineSession extends StudySession {
        public OnlineSession(String date, String time, String link) {
            super(date, time, link);
        }

        @Override
        public String getType() {
            return "Online";
        }
    }

    // ========= Study Group =========
    static class StudyGroup {
        private String groupId;
        private String groupName;
        private Subject subject;
        private List<Student> members = new ArrayList<>();
        private List<StudySession> sessions = new ArrayList<>();
        private Student leader;

        public StudyGroup(String groupId, Subject subject, String groupName) {
            this.groupId = groupId;
            this.subject = subject;
            this.groupName = groupName;
        }

        public String getGroupId() {
            return groupId;
        }

        public String getGroupName() {
            return groupName;
        }

        public Subject getSubject() {
            return subject;
        }

        public boolean addMember(Student student) {

            if (members.contains(student)) {
                return false;
            }
            members.add(student);

            if (leader == null) {
                leader = student;
            }

            return true;
        }

        public boolean removeMember(Student student) {
            if (!members.contains(student)) {
                return false;
            }

            members.remove(student);

            if (student.equals(leader)) {
                leader = members.isEmpty() ? null : members.get(0);
            }

            return true;
        }

        public boolean isLeader(Student student) {
            return student != null && student.equals(leader);
        }

        public boolean isMember(Student student) {
            return members.contains(student);
        }

        public void addSession(StudySession session) {
            sessions.add(session);
        }

        public void showMembers() {
            if (members.isEmpty()) {
                System.out.println("No members yet.");
                return;
            }

            System.out.println("\n===== Members of " + groupId + " =====");
            for (Student s : members) {
                System.out.println("- " + s.getName());
            }

            System.out.println("Leader: " + (leader != null ? leader.getName() : "None"));
        }

        public void showSessions() {
            if (sessions.isEmpty()) {
                System.out.println("No sessions yet.");
                return;
            }

            System.out.println("\n===== Sessions of " + groupId + " =====");
            for (StudySession s : sessions) {
                s.display();
            }
        }

    }

    private void loadFromFile() {
        try (Scanner fileScanner = new Scanner(new java.io.File("sessions.txt"))) {

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] data = line.split(",");

                if (data.length != 5)
                    continue;

                String groupId = data[0];
                String type = data[1];
                String date = data[2];
                String time = data[3];
                String location = data[4];

                StudyGroup group = findGroup(groupId);

                if (group != null) {
                    StudySession session;

                    if (type.equals("Physical")) {
                        session = new PhysicalSession(date, time, location);
                    } else {
                        session = new OnlineSession(date, time, location);
                    }

                    group.addSession(session);
                }
            }

        } catch (IOException e) {
            System.out.println("No previous session file found (this is normal on first run).");
        }
    }

    private void loadStudents() {
        try (Scanner sc = new Scanner(new java.io.File("students.txt"))) {
            while (sc.hasNextLine()) {
                String[] data = sc.nextLine().split(",");

                String id = data[0];
                String name = data[1];
                int batch = Integer.parseInt(data[2]);

                students.add(new Student(id, name, batch));
            }
        } catch (Exception e) {
            System.out.println("No student file found.");
        }
    }

    private void loadMembers() {
        try (Scanner sc = new Scanner(new java.io.File("members.txt"))) {
            while (sc.hasNextLine()) {
                String[] data = sc.nextLine().split(",");

                String groupId = data[0];
                String studentId = data[1];

                StudyGroup group = findGroup(groupId);
                Student student = findStudent(studentId);

                if (group != null && student != null) {
                    group.addMember(student);
                }
            }
        } catch (Exception e) {
            System.out.println("No members file found.");
        }
    }

    // ========= System Data =========
    private List<Student> students = new ArrayList<>();
    private List<Subject> subjects = new ArrayList<>();
    private List<StudyGroup> groups = new ArrayList<>();

    private Student currentStudent;
    private Scanner scanner = new Scanner(System.in);

    // ========= Start =========
    public void start() {
        setupSubjects();
        setupGroups();
        loadStudents();
        loadMembers(); //
        loadFromFile();
        loginMenu();
        showSubjects();
    }

    // ========= Setup =========
    private void setupSubjects() {
        subjects.add(new Subject("S1", "Object Oriented Programming (OOP)"));
        subjects.add(new Subject("S2", "Data Structure and Algorithm (DSA)"));
        subjects.add(new Subject("S3", "Operating System (OS)"));
        subjects.add(new Subject("S4", "Computer Organization and Architecture (COA)"));
        subjects.add(new Subject("S5", "Fundamentals of Networking"));
        subjects.add(new Subject("S6", "Statistics and Probability"));
    }

    private void loginMenu() {
        while (true) {
            System.out.println("\n===== LOGIN MENU =====");
            System.out.println("1. Register New Student");
            System.out.println("2. Login Existing Student");
            System.out.println("0. Exit");
            System.out.print("Choose option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    registerStudent();
                    return;

                case "2":
                    boolean success = loginStudent();
                    if (success)
                        return;
                    break;

                case "0":
                    System.out.println("Goodbye!");
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    private boolean loginStudent() {
        System.out.println("\n===== Student Login =====");

        System.out.print("Enter your ID: ");
        String id = scanner.nextLine().trim();

        Student student = findStudent(id);

        if (student == null) {
            System.out.println("Student not found. Please register first.");
            return false;
        }

        currentStudent = student;
        System.out.println("Welcome back, " + currentStudent.getName() + "!");
        return true;
    }

    private void setupGroups() {
        for (Subject subject : subjects) {
            groups.add(new StudyGroup(subject.getSubjectId() + "-A", subject, subject.getSubjectName() + " Group A"));
            groups.add(new StudyGroup(subject.getSubjectId() + "-B", subject, subject.getSubjectName() + " Group B"));
        }
    }

    // ========= Registration =========
    private void registerStudent() {
        String id;
        String name;

        System.out.println("====== Welcome to Study Organizer Group! ======");
        System.out.println("Please enter your credentials to register.\n");

        // Name input
        while (true) {
            System.out.print("Enter your Name: ");
            name = scanner.nextLine().trim();

            if (!isValidName(name)) {
                System.out.println("Name must contain only letters and spaces.");
                continue;
            }

            break;
        }

        // ID input
        while (true) {
            System.out.print("Enter your ID: ");
            id = scanner.nextLine().trim();

            if (id.isEmpty()) {
                System.out.println("ID cannot be empty.");
                continue;
            }

            if (!isValidId(id)) {
                System.out.println("Use only letters and numbers (min 3 characters).");
                continue;
            }

            if (findStudent(id) != null) {
                System.out.println("ID already exists.");
                continue;
            }

            break;
        }

        currentStudent = new Student(id, name, 2);
        students.add(currentStudent);
        saveStudent(currentStudent); //

        System.out.println("\n===== Registered Successfully! =====");
        System.out.println("Choose the subject you want to study.");
    }

    private Student findStudent(String id) {
        for (Student s : students) {
            if (s.getId().equals(id)) {
                return s;
            }
        }
        return null;
    }

    private boolean isValidId(String id) {
        return id.matches("[a-zA-Z0-9]{3,}");
    }

    private boolean isValidName(String name) {
        return name.matches("[a-zA-Z\\s]{2,}");
    }

    // ========= Subject Menu =========
    private void showSubjects() {
        while (true) {
            System.out.println("\nAvailable Subjects:");
            for (Subject s : subjects) {
                System.out.println(s.getSubjectId() + " - " + s.getSubjectName());
            }

            System.out.print("\nChoose subjectId (or type 0 to exit): ");
            String input = scanner.nextLine().trim();

            if (input.equals("0")) {
                System.out.println("Goodbye!");
                return;
            }

            Subject subject = findSubject(input);
            if (subject != null) {
                showGroups(subject);
            } else {
                System.out.println("Invalid subjectId. Try again.");
            }
        }
    }

    private Subject findSubject(String id) {
        for (Subject s : subjects) {
            if (s.getSubjectId().equalsIgnoreCase(id)) {
                return s;
            }
        }
        return null;
    }

    // ========= Group Menu =========
    private void showGroups(Subject subject) {
        System.out.println("\n===== Groups for " + subject.getSubjectName() + " =====");

        for (StudyGroup g : groups) {
            if (g.getSubject().equals(subject)) {
                System.out.println(g.getGroupId() + " - " + g.getGroupName());
            }
        }

        while (true) {
            System.out.print("\nChoose groupId (or type B to go back): ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("B")) {
                return;
            }

            StudyGroup group = findGroup(input);
            if (group != null && group.getSubject().equals(subject)) {
                groupMenu(group);
                return;
            } else {
                System.out.println("Invalid groupId. Try again.");
            }
        }
    }

    private StudyGroup findGroup(String id) {
        for (StudyGroup g : groups) {
            if (g.getGroupId().equalsIgnoreCase(id)) {
                return g;
            }
        }
        return null;
    }

    // ========= Group Operations =========
    private void groupMenu(StudyGroup group) {
        while (true) {
            System.out.println("\n===== Group Menu (" + group.getGroupId() + ") =====");
            System.out.println("1. Join Group");
            System.out.println("2. Leave Group");
            System.out.println("3. View Members");
            System.out.println("4. View Sessions");
            System.out.println("5. Schedule Session (Leader Only)");
            System.out.println("6. Back to Subject List");
            System.out.println("0. Back to Registration");

            System.out.print("Choose option: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1 -> {
                        boolean joined = group.addMember(currentStudent);
                        if (joined) {
                            saveMembership(group.getGroupId(), currentStudent);
                        }

                        if (joined) {
                            System.out.println("You joined successfully!");

                            if (group.isLeader(currentStudent)) {
                                System.out.println("You are the LEADER of this group.");
                            } else {
                                System.out.println("You are a MEMBER of this group.");
                            }
                        } else {
                            System.out.println("You are already in this group.");
                        }
                    }
                    case 2 -> {
                        boolean left = group.removeMember(currentStudent);
                        if (left) {
                            System.out.println("You left the group successfully.");
                        } else {
                            System.out.println("You haven't joined this group yet.");
                        }
                    }

                    case 3 -> group.showMembers();

                    case 4 -> group.showSessions();

                    case 5 -> {
                        if (!group.isMember(currentStudent)) {
                            System.out.println("You must join the group first!");
                            break;
                        }

                        if (group.isLeader(currentStudent)) {
                            schedule(group);
                        } else {
                            System.out.println("Only the leader can schedule sessions.");
                        }
                    }

                    case 6 -> {
                        return;
                    }

                    case 0 -> {
                        loginMenu();
                        return;
                    }

                    default -> System.out.println("Invalid choice.");
                }

            } catch (Exception e) {
                System.out.println("Invalid input. Enter a number.");
            }
        }
    }

    // ========= Schedule =========
    private void schedule(StudyGroup group) {
        System.out.println("\n===== Schedule Session =====");

        String date;
        while (true) {
            System.out.print("Date (YYYY-MM-DD): ");
            date = scanner.nextLine();

            if (isValidDate(date)) {
                break;
            } else {
                System.out.println("Invalid date format.");
            }
        }

        String time;
        while (true) {

            System.out.print("Time (HH:MM AM/PM): ");
            time = scanner.nextLine();

            if (isValidTime(time)) {
                break;
            } else {
                System.out.println("Invalid time format.");
            }
        }

        System.out.println("1. Physical Session");
        System.out.println("2. Online Session");
        System.out.print("Choose session type: ");
        int type = Integer.parseInt(scanner.nextLine());

        StudySession session;

        if (type == 1) {
            System.out.print("Location: ");
            String location = scanner.nextLine();
            session = new PhysicalSession(date, time, location);
        } else {
            System.out.print("Online Link: ");
            String link = scanner.nextLine();
            session = new OnlineSession(date, time, link);
        }

        group.addSession(session);
        saveToFile(group.getGroupId(), session);

        System.out.println("Session scheduled successfully!");
    }

    // ========= File Save =========
    private void saveToFile(String groupId, StudySession session) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("sessions.txt", true))) {

            String record = groupId + "," +
                    session.getType() + "," +
                    session.date + "," +
                    session.time + "," +
                    session.location;

            bw.write(record);
            bw.newLine();

        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }

    private void saveStudent(Student s) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("students.txt", true))) {
            bw.write(s.getId() + "," + s.getName() + "," + s.getBatchYear());
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error saving student." + e.getMessage());
        }
    }

    private void saveMembership(String groupId, Student s) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("members.txt", true))) {
            bw.write(groupId + "," + s.getId());
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error saving member." + e.getMessage());
        }
    }

    private boolean isValidDate(String date) {
        return date.matches("\\d{4}-\\d{2}-\\d{2}");
    }

    private boolean isValidTime(String time) {
        return time.matches("(0[1-9]|1[0-2]):[0-5][0-9] (AM|PM)");
    }

    // ========= Main =========
    public static void main(String[] args) {
        new StudyGroup1().start();
    }
}
