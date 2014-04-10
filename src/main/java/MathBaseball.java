//Main backend class for the Math Baseball program.
//written by: Jonathan Dolan
//Spring 2014

import java.awt.Color;
//import java.sql.SQLException;
import java.util.Random;

public class MathBaseball{
    static Random generator = new Random();
    static PlayBall gui;
    static int answer;
    static int player;
    static DBWrapper dataBase;
    static Team t;
    static int level;
    private static final int MAX = 100;
    static boolean randomGame = false;

    public static void playGame(char type, String uName, PlayBall playBall){
        t = new Team(dataBase.getTeamTemplate(uName));
        dataBase = BaseballController.dataBase;
        if(type == 'r')
            randomGame = true;
        questionType = type;
        gui = playBall;
        playBall.startGui();
        beginGame();
        generateQuestion(MAX, type);    //Need to add ability for random game mode
    }

    //Changes team template in the Team class
    public static void changeTemplate(String userName, int template){
    	t = new Team(template);
    	dataBase.changeTeamTemplate(userName, template);
    }
    
    //Gets an array of strings that represents location of current players
    public static String[] getDiamond(){
    	return Team.getCorrectDiamond();
    }


    //sets team name
    public static void setName(String name){
    	Team.setTeamName(name);
    }
    
    //gets team name
    public static String getName(){
    	return Team.teamName;
    }
    
    //gets color, set by template
   public static Color getColor(){
    	return t.getColor();
    }
   
   //gets total score
   public static int getScore(){
	   return Team.totalScore;
   }
   
   public static String getAtBat(){
	   return Team.atBat;
   }

    //checks answer and displays, then saves stats to database
    public static void answerReceived(int input, char type){
        if (input == answer){
            displayCorrect();
            dataBase.correctAnswer(player);
            t.advanceBases(level);
        }
        else{
            if (type == 'a'){
                displayWrongAdd(answer);
                dataBase.incorrectAnswer(player);
            }
            else if (type == 's'){
                displayWrongSub(answer);
                dataBase.incorrectAnswer(player);
            }
            else{
                displayWrongPlaces(answer);
                dataBase.incorrectAnswer(player);
            }
        }
    }

    public static void answerReceived(){
        generateQuestion(MAX, questionType);
    }


    //generates a question
    public static void generateQuestion(int max, int type){
        String s;
        int a = generator.nextInt(max);
        int b = generator.nextInt(max);
        

        if (type == 1){ // addition
            s = ("What is the SUM of " + a + " and " + b +"? " + a + " + " + b + " = ");
            gui.displayToScreen(s);
            gui.setButtonText("SWING");
            answer = a + b;


        }
        else if (type == 2){ // subtraction
            if (a < b){
                int temp;
                temp = a;
                a = b;
                b = temp;
            }
            s = ("What is the DIFFERENCE of " + a + " and " + b +"? " + a + " - " + b + " = ");
            gui.displayToScreen(s);
            gui.setButtonText("SWING");
            answer = a - b;

        }
        else{
            MathBaseball.generatePlaceQuestion();
        }
        //find level, to see how man bases are run
        if (answer <= 10){
        	level = 1;
        }
        else if (answer <= 100){
        	level = 2;
        }
        else
        	level = 3;
    }

    //generates place question
    public static void generatePlaceQuestion(){
        String s;
    	level = 1;
        int given = generator.nextInt(900);
        given = given + 100;
        int place = generator.nextInt(2);
        place++;
        String intStr = Integer.toString(given);
        //make sure each place is unique
        while (true){
            if (intStr.charAt(0) == intStr.charAt(1)){
                intStr.replace(intStr.charAt(0), (Integer.toString(generator.nextInt(9)).charAt(0)));
            }
            else if(intStr.charAt(0) == intStr.charAt(2)){
                intStr.replace(intStr.charAt(0), (Integer.toString(generator.nextInt(9)).charAt(0)));
            }
            else if(intStr.charAt(1) == intStr.charAt(2)){
                intStr.replace(intStr.charAt(1), (Integer.toString(generator.nextInt(9)).charAt(0)));
            }
            else{
                break;
            }
        }

        given = Integer.parseInt(intStr);

        if (place == 1){
            gui.displayPlacesQuestion(given, place);
            s = ("What is in the " + placeFinder(place) + " place of " + given +"?");
            gui.displayToScreen(s);
            answer =  intStr.charAt(0);

        }
        else if (place == 2){
            gui.displayPlacesQuestion(given, place);
            s = ("What is in the " + placeFinder(place) + " place of " + given +"?");
            gui.displayToScreen(s);
            answer= intStr.charAt(1);

        }
        else{
            gui.displayPlacesQuestion(given, place);
            s = ("What is in the " + placeFinder(place) + " place of " + given +"?");
            gui.displayToScreen(s);
            answer = intStr.charAt(2);

        }

    }

    //Added --> Written by: Chris Fowles//
    static int firstNum;
    static int secondNum;
    static char questionType;
    static String tempQuestion;
    static String atBatPlayer;
    static int score;

    private static String buildQuestion(int one, int two){
        gui.setButtonText("SWING");
        String s;
        if(questionType == 'a'){
            s = ("What is the SUM of " + one + " and " + two +"? " + one + " + " + two + " = ");
        }
        else if(questionType == 's'){
            s = ("What is the DIFFERENCE of " + one + " and " + two +"? " + one + " - " + two + " = ");
        }
        else{
            s = ("What is in the " + placeFinder(two) + " place of " + one +"?");
        }
        tempQuestion = s;
        return s;
    }

    private static String placeFinder(int x){
        String s;
        if(x == 1){
            s = "One's";
        }
        else if(x == 2){
            s = "Tenth's";
        }
        else{
            s = "Hundredth's";
        }
        return s;
    }

    public static void displayAdditionQuestion(int one, int two){
        firstNum = one;
        secondNum = two;
        questionType = 'a';
        String d = buildQuestion(one, two);
        gui.displayToScreen("");
        gui.displayToScreen(d);
    }

    public static void displaySubtractionQuestion(int one, int two){
        firstNum = one;
        secondNum = two;
        questionType = 's';
        gui.displayToScreen("");
        gui.displayToScreen(buildQuestion(one, two));
    }

    public static void displayPlacesQuestion(int one, int two){
        firstNum = one;
        secondNum = two;
        questionType = 'p';
        gui.displayToScreen("");
        gui.displayToScreen(buildQuestion(one, two));
    }

    public static String getPlayersOnBase(){
        String [] bases = getDiamond();
        return "First Base: " + bases[0] + ", Second: " + bases[1] + ", Third: " + bases[2] +".";
    }

    public static void displayCorrect(){
        //dBase.correctAnswer(gameID);
        //score = dBase.getScoreForStudent(MainMenu.studentUserName);
        gui.displayToScreen("");
        gui.displayToScreen("GOOD JOB! That was the correct answer.\n"
                + getAtBat() + " got a hit!\n" +
                getPlayersOnBase() +
                "\nPress Next Question, or hit ENTER, to continue.");
        gui.setButtonText("Next Question");
        gui.setScore("");
        gui.setScore("Score: " + score);
    }

    public static void displayWrongAdd(int c){
        //dBase.incorrectAnswer(gameID);
        gui.displayToScreen("");
        gui.displayToScreen("Nice try, but that was INCORRECT.\n" + getAtBat() + " WHIFFED\n" + "The right answer was " + c
        +"\n" + getPlayersOnBase());
        gui.setButtonText("Next Question");
    }

    public static void displayWrongSub(int c){
        //dBase.incorrectAnswer(gameID);
        gui.displayToScreen("");
        gui.displayToScreen("Nice try, but that was INCORRECT. \n" + getAtBat() + " hit a FOUL BALL\n" + "The right answer was " + c
                +"\n" + getPlayersOnBase());
        gui.setButtonText("Next Question");
    }

    public static void displayWrongPlaces(int c){
        //dBase.incorrectAnswer(gameID);
        gui.displayToScreen("");
        gui.displayToScreen("Nice try, but that was INCORRECT. \nThe right answer was " + c
                +"\n" + getPlayersOnBase());
        gui.setButtonText("Next Question");
    }

    public static void beginGame(){
        gui.setButtonText("START");
        gui.setScore("SCORE: ");
        gui.displayToScreen("Welcome to Math Baseball! \n" +
                "For each problem click SWING or press ENTER to submit your answer\n"
                + "To begin press the START button below\n"
                + "GOOD LUCK!");
    }



}