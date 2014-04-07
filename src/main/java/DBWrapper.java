import java.sql.*;
import java.util.ArrayList;

public class DBWrapper {
    
    private final Connection connect;  // without auto-commit    
    
    public DBWrapper() throws SQLException {
        try { Class.forName("com.mysql.jdbc.Driver").newInstance(); }
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException except) {
            throw new SQLException();
        }

        String serverName = "localhost";
        String mydatabase = "MathBaseball";
        String url = "jdbc:mysql://" + serverName + "/" + mydatabase; 

        String username = "root";
        String password = "NEWPASS";
        
        connect = DriverManager.getConnection(url, username, password);
        connect.setAutoCommit(false);
    }
    
    public boolean close() {
        try {
            connect.close();
            return true;
        } catch (SQLException sqex) {
            return false;
        }
    }
    
    public void addNewStudent(String username, String password ,String firstName, String lastName, String teacherUserName) {
        PreparedStatement newData1 = null;
        PreparedStatement newData2 = null;
        PreparedStatement teacherIDFinder = null;
        int teacherID = -1;
        
        try {

        	newData1 = connect.prepareStatement(
                " insert into User (username, password, firstName, lastName, isAStudent) " + 
                " values (?, ?, ?, ?, ?); ");
            newData1.setString(1, username);
            newData1.setString(2, password);
            newData1.setString(3, firstName);
            newData1.setString(4, lastName);
            newData1.setInt(5, 1);
            newData1.executeUpdate();
            
            teacherIDFinder = connect.prepareStatement(
            		" select userID from User "
            		+ "where username = ?;");
            teacherIDFinder.setString(1, teacherUserName);
            ResultSet results = teacherIDFinder.executeQuery();
            if (results.next()) {
                teacherID = results.getInt(1);
            }
            
            
            newData2 = connect.prepareStatement(
                    " insert into Student (studentID, teacherID) " + 
                    " values (last_insert_id(), ?); ");
            newData2.setInt(1, teacherID);
            newData2.executeUpdate();
        
            connect.commit();
            
        } catch(SQLException sqex) {
        	try { connect.rollback(); System.err.println(sqex.toString()); }
            catch (SQLException sqex2) { System.err.println("Dammit\n" + sqex2.toString()); }
        } finally {
            if (newData1 != null) {
                try { newData1.close(); }  catch (SQLException sqex) { System.err.println(sqex.toString()); }
            }
            if (newData2 != null) {
                try { newData2.close(); }  catch (SQLException sqex) { System.err.println(sqex.toString()); }
            }
            if (teacherIDFinder != null) {
                try { teacherIDFinder.close(); }  catch (SQLException sqex) { System.err.println(sqex.toString()); }
            }
        }
    }
    
    public int startNewGame(String username) {
        PreparedStatement newData1 = null;
        PreparedStatement newData2 = null;
        PreparedStatement gameIDFinder = null;
        PreparedStatement studentIDFinder = null;
        int gameID = -1;
        int studentID = -1;
        
        try {

        	newData1 = connect.prepareStatement(
                " insert into GameScore (numberOfQuestions, numberOfCorrectAnswers) " + 
                " values (?, ?); ");
            newData1.setInt(1, 0);
            newData1.setInt(2, 0);
            newData1.executeUpdate();
            
            gameIDFinder = connect.prepareStatement(
            		" select last_insert_id(); ");
            ResultSet results = gameIDFinder.executeQuery();
            if (results.next()) {
                gameID = results.getInt(1);
            }
            
            studentIDFinder = connect.prepareStatement(
            		" select userID from User "
            		+ "where username = ? ;");
            studentIDFinder.setString(1, username);
            ResultSet result = studentIDFinder.executeQuery();
            if (result.next()) {
                studentID = result.getInt(1);
            }
            
            newData2 = connect.prepareStatement(
                    " insert into GameResult (gameID, studentID) " + 
                    " values (?, ?); ");
                newData2.setInt(1, gameID);
                newData2.setInt(2, studentID);
                newData2.executeUpdate();
        
            connect.commit();
            
        } catch(SQLException sqex) {
        	try { connect.rollback(); System.err.println(sqex.toString()); }
            catch (SQLException sqex2) { System.err.println("Dammit\n" + sqex2.toString()); }
        } finally {
            if (newData1 != null) {
                try { newData1.close(); }  catch (SQLException sqex) { System.err.println(sqex.toString()); }
            }
            if (newData2 != null) {
                try { newData2.close(); }  catch (SQLException sqex) { System.err.println(sqex.toString()); }
            }
            if (gameIDFinder != null) {
                try { gameIDFinder.close(); }  catch (SQLException sqex) { System.err.println(sqex.toString()); }
            }
            if (studentIDFinder != null) {
                try { studentIDFinder.close(); }  catch (SQLException sqex) { System.err.println(sqex.toString()); }
            }
        }
        return gameID;
    }
    
    
    public void correctAnswer(int gameID) {
        PreparedStatement newData = null;
        
        try {
            newData = connect.prepareStatement(
                " update GameScore set numberOfQuestions = numberOfQuestions + 1, numberOfCorrectAnswers = numberOfCorrectAnswers + 1 " + 
                " where gameID = ? ; ");
            newData.setInt(1, gameID);
            newData.executeUpdate();
            connect.commit();
        
        } catch (SQLException sqex) {
            try { connect.rollback(); System.err.println(sqex.toString()); }
            catch (SQLException sqex2) { System.err.println("Dammit\n" + sqex2.toString()); }
        } finally {
            if (newData != null) {
                try { newData.close(); }  catch (SQLException sqex) { System.err.println(sqex.toString()); }
            }
        }        
    }
    
    
    public void incorrectAnswer(int gameID) {
        PreparedStatement newData = null;
        
        try {
            newData = connect.prepareStatement(
                " update GameScore set numberOfQuestions = numberOfQuestions + 1 " + 
                " where gameID = ? ; ");
            newData.setInt(1, gameID);
            newData.executeUpdate();
            connect.commit();
        
        } catch (SQLException sqex) {
            try { connect.rollback(); System.err.println(sqex.toString()); }
            catch (SQLException sqex2) { System.err.println("Dammit\n" + sqex2.toString()); }
        } finally {
            if (newData != null) {
                try { newData.close(); }  catch (SQLException sqex) { System.err.println(sqex.toString()); }
            }
        }        
    }
    
    
    public boolean login(String username, String password) {
        boolean result = true;
        PreparedStatement finder = null;
        
        try {
            
            finder = connect.prepareStatement(
                " select userID from User " +
                " where username = ? and password = ? ; ");
            finder.setString(1, username);
            finder.setString(2, password);
            
            ResultSet user = finder.executeQuery();
            if (!user.next()) {
            	result = false;
            }
            
        } catch (SQLException sqex) {
            System.err.println(sqex.toString());
        } finally {
            if (finder != null) {
                try { finder.close(); }  catch (SQLException sqex) { System.err.println(sqex.toString()); }
            }
        }
        
        return result;
    }
    
    
    
    
    public void addNewTeacher(String firstName, String lastName, String username, String password) {
        Statement select = null;
        PreparedStatement newData = null;
        
        try {
        	select = connect.createStatement();

        	newData = connect.prepareStatement(
                " insert into User (username, password, firstName, lastName, isAStudent) " + 
                " values (?, ?, ?, ?, ?); ");
            newData.setString(1, username);
            newData.setString(2, password);
            newData.setString(3, firstName);
            newData.setString(4, lastName);
            newData.setInt(5, 0);
            
            newData.executeUpdate();
            
            connect.commit();
        
        } catch(SQLException sqex) {
        	try { connect.rollback(); System.err.println(sqex.toString()); }
            catch (SQLException sqex2) { System.err.println("Dammit\n" + sqex2.toString()); }
        } finally {
        	if (select != null) {
                try { select.close(); }  catch (SQLException sqex) { System.err.println(sqex.toString()); }
            }
            if (newData != null) {
                try { newData.close(); }  catch (SQLException sqex) { System.err.println(sqex.toString()); }
            }
        }
    }
    
    
    public int getScoreForStudent(String username) {
    	int score = 0;
    	int studentID = -1;
    	ArrayList<Integer> gameIDs = new ArrayList<Integer>();
        PreparedStatement studentFinder = null;
        PreparedStatement gameFinder = null;
        PreparedStatement scoreFinder = null;
        
        try {
            
        	//Get student ID
            studentFinder = connect.prepareStatement(
                " select userID from User " +
                " where username = ?; ");
            studentFinder.setString(1, username);
            ResultSet id = studentFinder.executeQuery();
            if (id.next()) {
                studentID = id.getInt(1);
            }
            
            //Get the students games
            gameFinder = connect.prepareStatement(
            		" select gameID from GameResult " +
            		" where studentID = ? ; ");
            gameFinder.setInt(1, studentID);
            ResultSet games = gameFinder.executeQuery();
            while (games.next()) {
            	gameIDs.add(games.getInt(1));
            }
            
            //Get the scores 
            for(int gameID : gameIDs) {
            	scoreFinder = null;
            	scoreFinder = connect.prepareStatement(
            			" select numberOfCorrectAnswers from GameScore " +
            			" where gameID = ? ; ");
            	scoreFinder.setInt(1, gameID);
            	ResultSet gameScore = scoreFinder.executeQuery();
            	if (gameScore.next()) {
            		score = score + gameScore.getInt(1);
            	}
            }
            
        } catch (SQLException sqex) {
            System.err.println(sqex.toString());
        } finally {
            if (studentFinder != null) {
                try { studentFinder.close(); }  catch (SQLException sqex) { System.err.println(sqex.toString()); }
            }
            if (gameFinder != null) {
                try { gameFinder.close(); }  catch (SQLException sqex) { System.err.println(sqex.toString()); }
            }
            if (scoreFinder != null) {
                try { scoreFinder.close(); }  catch (SQLException sqex) { System.err.println(sqex.toString()); }
            }
        }
        
        return score;
    	
    }
    
    public int getNumberOfQuestionsForStudent(String username) {
    	int numberOfQuestions = 0;
    	int studentID = -1;
    	ArrayList<Integer> gameIDs = new ArrayList<Integer>();
        PreparedStatement studentFinder = null;
        PreparedStatement gameFinder = null;
        PreparedStatement numberOfQuestionsFinder = null;
        
        try {
            
        	//Get student ID
            studentFinder = connect.prepareStatement(
                " select userID from User " +
                " where username = ?; ");
            studentFinder.setString(1, username);
            ResultSet id = studentFinder.executeQuery();
            if (id.next()) {
                studentID = id.getInt(1);
            }
            
            //Get the students games
            gameFinder = connect.prepareStatement(
            		" select gameID from GameResult " +
            		" where studentID = ? ; ");
            gameFinder.setInt(1, studentID);
            ResultSet games = gameFinder.executeQuery();
            while (games.next()) {
            	gameIDs.add(games.getInt(1));
            }
            
            //Get the number of questions
            for(int gameID : gameIDs) {
            	numberOfQuestionsFinder = null;
            	numberOfQuestionsFinder = connect.prepareStatement(
            			" select numberOfQuestions from GameScore " +
            			" where gameID = ? ; ");
            	numberOfQuestionsFinder.setInt(1, gameID);
            	ResultSet gameScore = numberOfQuestionsFinder.executeQuery();
            	if (gameScore.next()) {
            		numberOfQuestions = numberOfQuestions + gameScore.getInt(1);
            	}
            }
            
        } catch (SQLException sqex) {
            System.err.println(sqex.toString());
        } finally {
            if (studentFinder != null) {
                try { studentFinder.close(); }  catch (SQLException sqex) { System.err.println(sqex.toString()); }
            }
            if (gameFinder != null) {
                try { gameFinder.close(); }  catch (SQLException sqex) { System.err.println(sqex.toString()); }
            }
            if (numberOfQuestionsFinder != null) {
                try { numberOfQuestionsFinder.close(); }  catch (SQLException sqex) { System.err.println(sqex.toString()); }
            }
        }
        
        return numberOfQuestions;
    	
    }
    
    public ArrayList<StudentInfo> getStudentInfoForTeacher(String teacherUsername) {
    	int teacherID = -1;
    	ArrayList<StudentInfo> studentInfo = new ArrayList<StudentInfo>();
    	ArrayList<Integer> studentIDs = new ArrayList<Integer>();
        PreparedStatement teacherFinder = null;
        PreparedStatement studentFinder = null;
        PreparedStatement studentInfoFinder = null;
        
        try {
            
        	//Get teacher ID
            teacherFinder = connect.prepareStatement(
                " select userID from User " +
                " where username = ?; ");
            teacherFinder.setString(1, teacherUsername);
            ResultSet id = teacherFinder.executeQuery();
            if (id.next()) {
                teacherID = id.getInt(1);
            }
            
            //Get the teacher's students
            studentFinder = connect.prepareStatement(
            		" select studentID from Student " +
            		" where teacherID = ? ; ");
            studentFinder.setInt(1, teacherID);
            ResultSet students = studentFinder.executeQuery();
            while (students.next()) {
            	studentIDs.add(students.getInt(1));
            }
            
            //Get the students' info
            for(int studentID : studentIDs) {
            	studentInfoFinder = null;
            	studentInfoFinder = connect.prepareStatement(
            			" select username, password, firstName, lastName from User " +
            			" where userID = ? ; ");
            	studentInfoFinder.setInt(1, studentID);
            	ResultSet userInfo = studentInfoFinder.executeQuery();
            	if (userInfo.next()) {
            		studentInfo.add(new StudentInfo(userInfo.getString(3), userInfo.getString(4), userInfo.getString(1), userInfo.getString(2)));
            	}
            }
            
        } catch (SQLException sqex) {
            System.err.println(sqex.toString());
        } finally {
            if (teacherFinder != null) {
                try { teacherFinder.close(); }  catch (SQLException sqex) { System.err.println(sqex.toString()); }
            }
            if (studentFinder != null) {
                try { studentFinder.close(); }  catch (SQLException sqex) { System.err.println(sqex.toString()); }
            }
            if (studentInfoFinder != null) {
                try { studentInfoFinder.close(); }  catch (SQLException sqex) { System.err.println(sqex.toString()); }
            }
        }
        
        return studentInfo;
    }
    
    public void changeUsername(String oldUsername, String newUsername) {
        PreparedStatement newData = null;
        
        try {
            newData = connect.prepareStatement(
                " update User set username = ? " + 
                " where username = ? ; ");
            newData.setString(1, newUsername);
            newData.setString(2, oldUsername);
            newData.executeUpdate();
            connect.commit();
        
        } catch (SQLException sqex) {
            try { connect.rollback(); System.err.println(sqex.toString()); }
            catch (SQLException sqex2) { System.err.println("Dammit\n" + sqex2.toString()); }
        } finally {
            if (newData != null) {
                try { newData.close(); }  catch (SQLException sqex) { System.err.println(sqex.toString()); }
            }
        }        
    }
    
    public void changePassword(String username, String newPassword) {
    	PreparedStatement newData = null;
        
        try {
            newData = connect.prepareStatement(
                " update User set password = ? " + 
                " where username = ? ; ");
            newData.setString(1, newPassword);
            newData.setString(2, username);
            newData.executeUpdate();
            connect.commit();
        
        } catch (SQLException sqex) {
            try { connect.rollback(); System.err.println(sqex.toString()); }
            catch (SQLException sqex2) { System.err.println("Dammit\n" + sqex2.toString()); }
        } finally {
            if (newData != null) {
                try { newData.close(); }  catch (SQLException sqex) { System.err.println(sqex.toString()); }
            }
        }     
    }
    
    public void changeFirstName(String username, String newFirstName) {
    	PreparedStatement newData = null;
        
        try {
            newData = connect.prepareStatement(
                " update User set firstName = ? " + 
                " where username = ? ; ");
            newData.setString(1, newFirstName);
            newData.setString(2, username);
            newData.executeUpdate();
            connect.commit();
        
        } catch (SQLException sqex) {
            try { connect.rollback(); System.err.println(sqex.toString()); }
            catch (SQLException sqex2) { System.err.println("Dammit\n" + sqex2.toString()); }
        } finally {
            if (newData != null) {
                try { newData.close(); }  catch (SQLException sqex) { System.err.println(sqex.toString()); }
            }
        }     
    }
    
    public void changeLastName(String username, String newLastName) {
    	PreparedStatement newData = null;
        
        try {
            newData = connect.prepareStatement(
                " update User set lastName = ? " + 
                " where username = ? ; ");
            newData.setString(1, newLastName);
            newData.setString(2, username);
            newData.executeUpdate();
            connect.commit();
        
        } catch (SQLException sqex) {
            try { connect.rollback(); System.err.println(sqex.toString()); }
            catch (SQLException sqex2) { System.err.println("Dammit\n" + sqex2.toString()); }
        } finally {
            if (newData != null) {
                try { newData.close(); }  catch (SQLException sqex) { System.err.println(sqex.toString()); }
            }
        }     
    }
    
    
    public static void main(String[] args) throws SQLException {
    	DBWrapper db = new DBWrapper();
    	ArrayList<StudentInfo> students;
    	db.addNewTeacher("Greg", "Gagne", "ggagne", "softawre");
    	db.addNewStudent("cfowles", "s", "Chris", "Fowles", "ggagne");
    	db.addNewStudent("jdolan", "a", "Jonathan", "Dolan", "ggagne");
    	int j = db.startNewGame("jdolan");
	    int c = db.startNewGame("cfowles");
	    db.correctAnswer(j);
	    db.correctAnswer(j);
	    db.correctAnswer(j);
	    db.correctAnswer(j);
	    db.incorrectAnswer(j);
	    
	    db.incorrectAnswer(c);
	    db.incorrectAnswer(c);
	    db.correctAnswer(c);
	    db.correctAnswer(c);
	    db.correctAnswer(c);
	    
	    students = db.getStudentInfoForTeacher("ggagne");
	    
	    for (StudentInfo s : students){
	    	System.out.println(s.getFirstName()+ " " + s.getLastName());
	    }
	    
	    
	    db.close();
    }

    
    
}
