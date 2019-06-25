package quiz.database;

import java.sql.*;

public class DatabaseHandler {
    private static final String connection_string="jdbc:sqlite:quiz.db";
    private static final String Jdbc_driver="org.sqlite.JDBC";
    private Connection con=null;
    private Statement stmt=null;
    private static final String questionsTable="CREATE TABLE if not exists questions (" +
            "qid INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
            "question VARCHAR NOT NULL," +
            "option1 VARCHAR NOT NULL," +
            "option2 VARCHAR NOT NULL," +
            "option3 VARCHAR NOT NULL," +
            "option4 VARCHAR NOT NULL," +
            "answer VARCHAR NOT NULL," +
            "topic VARCHAR NOT NULL,"+
            "level INTEGER NOT NULL" +
            ");";
    private static final String answersTable="CREATE TABLE if not exists answers (username VARCHAR NOT NULL REFERENCES users (username),"
            + "qno INT NOT NULL,response VARCHAR NOT NULL,PRIMARY KEY (username,qno))";
    private static final String usersTable="create table if not exists users(username varchar,password varchar,PRIMARY KEY(username))";
    private static final String adminTable="create table if not exists admin(username VARCHAR,password VARCHAR,PRIMARY KEY(username))";


    public void connect() {
        try {
            Class.forName(Jdbc_driver);
            con = DriverManager.getConnection(connection_string);
            stmt = con.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
            try {
                if(stmt != null) {
                    stmt.close();
                    stmt = null;
                }
                if(con != null) {
                    con.close();
                    con = null;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    public void createTables() {
        try {
            connect();
            stmt.executeUpdate(adminTable);
            stmt.executeUpdate(usersTable);
            stmt.executeUpdate(questionsTable);
            stmt.executeUpdate(answersTable);
            insertAdmin();
            disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertAdmin() {
        try {
            String sql="insert into admin values('root','password123')";
            if(stmt.executeUpdate(sql)==0)
                System.out.println("root admin already present");
        } catch (SQLException e) {
            //e.printStackTrace();
        }
    }

    public void clearAllTables() {
        String sql = "TRUNCATE answers ";
        String sql1 = "TRUNCATE questions ";
        String sql2 = "TRUNCATE users ";
        try {
            connect();
            stmt.executeUpdate(sql);
            stmt.executeUpdate(sql1);
            stmt.executeUpdate(sql2);
            disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void clearQuestions() {
        try {
            connect();
            stmt.executeUpdate("TRUNCATE questions ");
            disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void clearAnswers() {
        try {
            connect();
            stmt.executeUpdate("TRUNCATE answers ");
            disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet execute(String sql) {
        try {
            ResultSet set = stmt.executeQuery(sql);
            return set;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int executeUpdate(String sql) {
        try {
            return stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
