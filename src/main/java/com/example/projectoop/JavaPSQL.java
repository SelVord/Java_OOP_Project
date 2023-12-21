package com.example.projectoop;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JavaPSQL {
    private static User selectedUser;
    private static Exam selectedExam;

    public static User getSelecterUser() {
        System.out.println(selectedUser);
        return selectedUser;
    }

    public static Exam getSelecterExam() {
        System.out.println(selectedExam);
        return selectedExam;
    }

    public static void setSelectedUserAndExam(User user, Exam exam) {
        selectedUser = user;
        selectedExam = exam;
    }

    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/Java_OOP";
    private static final String JDBC_USER = "postgres";
    private static final String JDBC_PASSWORD = "123456";

    public static void registerUser(String username, String password) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String query = "INSERT INTO users (name, psw) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean getUsers(String username, String password) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT name FROM users WHERE name = ? AND psw = ?")) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean checkIfUserExist(String username) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT id FROM users WHERE name = ?")) {
                preparedStatement.setString(1, username);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Exam> getAllExams() {
        List<Exam> examsList = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT exams.id, exams.title, users.name AS user_name, COUNT(questions.id) AS question_count FROM exams JOIN users ON exams.user_id = users.id LEFT JOIN questions ON exams.id = questions.exam_id GROUP BY exams.id, exams.title, user_name order by exams.id")) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int examId = resultSet.getInt("id");
                        String examTitle = resultSet.getString("title");
                        String userName = resultSet.getString("user_name");
                        int questionCount = resultSet.getInt("question_count");

                        examsList.add(new Exam(examId, examTitle, userName, questionCount));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return examsList;
    }

    public static List<Exam> searchById(int id) {
        List<Exam> examsList = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT exams.id, exams.title, users.name AS user_name, COUNT(questions.id) AS question_count FROM exams JOIN users ON exams.user_id = users.id LEFT JOIN questions ON exams.id = questions.exam_id WHERE exams.id = ? GROUP BY exams.id, exams.title, user_name order by exams.id")) {
                preparedStatement.setInt(1, id);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int examId = resultSet.getInt("id");
                        String examTitle = resultSet.getString("title");
                        String userName = resultSet.getString("user_name");
                        int questionCount = resultSet.getInt("question_count");

                        examsList.add(new Exam(examId, examTitle, userName, questionCount));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return examsList;
    }

    public static List<Exam> searchByTitle(String title) {
        List<Exam> examsList = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT exams.id, exams.title, users.name AS user_name, COUNT(questions.id) AS question_count FROM exams JOIN users ON exams.user_id = users.id LEFT JOIN questions ON exams.id = questions.exam_id WHERE exams.title like ? GROUP BY exams.id, exams.title, user_name order by exams.id")) {
                preparedStatement.setString(1, "%" + title + "%");
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int examId = resultSet.getInt("id");
                        String examTitle = resultSet.getString("title");
                        String userName = resultSet.getString("user_name");
                        int questionCount = resultSet.getInt("question_count");

                        examsList.add(new Exam(examId, examTitle, userName, questionCount));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return examsList;
    }

    public static int getUserId(String username) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT id FROM users WHERE name = ?")) {
                preparedStatement.setString(1, username);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("id");
                    } else {
                        return -1;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static List<Exam> getUserExams(String username) {
        List<Exam> examsList = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String query = "SELECT exams.id, exams.title, users.name AS user_name, COUNT(questions.id) AS question_count FROM exams JOIN users ON exams.user_id = users.id LEFT JOIN questions ON exams.id = questions.exam_id WHERE users.name = ? GROUP BY exams.id, exams.title, user_name order by exams.id";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                 preparedStatement.setString(1, username);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int examId = resultSet.getInt("id");
                        String examTitle = resultSet.getString("title");
                        String userName = resultSet.getString("user_name");
                        int questionCount = resultSet.getInt("question_count");

                        examsList.add(new Exam(examId, examTitle, userName, questionCount));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return examsList;
    }

    public static int createExam(String username, String examTitle) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String insertExamQuery = "INSERT INTO exams (user_id, title) VALUES ((SELECT id FROM users WHERE name = ?), ?) RETURNING id";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertExamQuery, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, examTitle);

                preparedStatement.executeUpdate();

                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Не удалось получить сгенерированный ключ.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }


    public static void createQuestion(int examId, String questionText) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String insertQuestionQuery = "INSERT INTO questions (exam_id, question) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuestionQuery)) {
                preparedStatement.setInt(1, examId);
                preparedStatement.setString(2, questionText);

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<User> getUsersToGrade(Exam exam) {
        List<User> usersToGrade = new ArrayList<>();

        String query = "SELECT DISTINCT users.id, users.name " +
                "FROM users " +
                "RIGHT JOIN answers ON users.id = answers.user_id " +
                "LEFT JOIN question_results ON answers.user_id = question_results.user_id " +
                "AND answers.question_id = question_results.question_id " +
                "LEFT JOIN questions ON answers.question_id = questions.id " +
                "LEFT JOIN exams ON questions.exam_id = exams.id " +
                "WHERE exams.id = ? AND question_results.id IS NULL";

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, exam.getId());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int userId = resultSet.getInt("id");
                    String userName = resultSet.getString("name");
                    User user = new User(userId, userName);
                    usersToGrade.add(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usersToGrade;
    }

    public static List<Question> getQuestionsForExam(int examId) {
        List<Question> questions = new ArrayList<>();

        String query = "SELECT * FROM questions WHERE exam_id = ?";

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, selectedExam.getId());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int questionId = resultSet.getInt("id");
                    String questionText = resultSet.getString("question");
                    Question question = new Question(questionId, questionText);
                    questions.add(question);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return questions;
    }

    public static List<String> getAnswersForUserAndExam(int userId, int examId) {
        List<String> answers = new ArrayList<>();

        String query = "SELECT answer " +
                "FROM answers " +
                "WHERE user_id = ? AND question_id IN (SELECT id FROM questions WHERE exam_id = ?)";

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, selectedUser.getId());
            preparedStatement.setInt(2, selectedExam.getId());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String answer = resultSet.getString("answer");
                    answers.add(answer);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return answers;
    }

    public static void addQuestionResult(int userId, int questionId, float grade) {
        String query = "INSERT INTO question_results (user_id, question_id, grade) VALUES (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, questionId);
            preparedStatement.setFloat(3, grade);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addResult(int userId, int examId, float grade) {
        String query = "INSERT INTO results (user_id, exam_id, grade) VALUES (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, examId);
            preparedStatement.setFloat(3, grade);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<QuestionResult> getQuestionResultsForExamAndUser(int userId, int examId) {
        List<QuestionResult> questionResults = new ArrayList<>();

        String query = "SELECT q.question, a.answer, qr.grade " +
                "FROM questions q " +
                "LEFT JOIN answers a ON q.id = a.question_id AND a.user_id = ? " +
                "LEFT JOIN question_results qr ON q.id = qr.question_id AND qr.user_id = ? " +
                "WHERE q.exam_id = ?";

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, userId);
            preparedStatement.setInt(3, examId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String questionText = resultSet.getString("question");
                    String userAnswer = resultSet.getString("answer");
                    Float grade = resultSet.getFloat("grade");

                    QuestionResult questionResult = new QuestionResult(questionText, userAnswer, grade);
                    questionResults.add(questionResult);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return questionResults;
    }

    public static Float getGradeForExam(int userId, int examId) {
        Float overallGrade = null;

        String query = "SELECT grade " +
                "FROM results " +
                "WHERE user_id = ? AND exam_id = ?";

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, examId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    overallGrade = resultSet.getFloat("grade");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return overallGrade;
    }

    public static List<String> getCompletedExams(String currentUser) {
        List<String> completedExams = new ArrayList<>();

        String query = "SELECT DISTINCT exams.title " +
                "FROM exams " +
                "JOIN results ON exams.id = results.exam_id " +
                "WHERE results.user_id = ?";

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            int userId = getUserId(currentUser);

            preparedStatement.setInt(1, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String title = resultSet.getString("title");
                    completedExams.add(title);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return completedExams;
    }



    public static int getIdByTitle(String title) {
        String query = "SELECT id FROM exams WHERE title = ?";

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, title);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    return resultSet.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}



//create table users (
//    id serial primary key,
//    name varchar(50),
//    psw varchar(50)
//);
//
//create table exams (
//    id serial primary key,
//    user_id int references users(id),
//    title varchar(100)
//);
//
//create table questions (
//    id serial primary key,
//    exam_id int references exams(id),
//    question varchar(300)
//);
//
//create table answers (
//    id serial primary key,
//    user_id int references users(id),
//    question_id int references questions(id),
//    answer varchar(1000)
//);
//
//create table results (
//    id serial primary key,
//    user_id int references users(id),
//    exam_id int references exams(id),
//    grade float
//);
//
//create table question_results (
//    id serial primary key,
//    user_id int references users(id),
//    question_id int references questions(id),
//    grade float
//);