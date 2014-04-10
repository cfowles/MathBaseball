//Team includes team name, color, name template, and logic to see who's on base, as well as scoring.
//written by: Jonathan Dolan
//Spring 2014

import java.awt.Color;
import java.util.ArrayList;

public class Team {

    //add team name
    static String teamName;

    static int totalScore = 0;

    //baseball diamond, holds player names on bases
    static String atBat = "";
    static String firstBase = "";
    static String secondBase = "";
    static String thirdBase = "";

    //color templates
    static Color[] colors = {Color.red, Color.blue, Color.yellow, Color.green, Color.pink};
    static Color myColor;
    //name templates
    static String[][] names = {{"Babe Ruth", "Walter Johnson", "Christy Mathewson", "Stan Musial", "Honus Wagner", "Johnny Bench", "Satchel Paige", "Frank Robinson", "Pete Rose", },
            {"Willie Mays", "Hank Aaron", "Ted Williams", "Joe DiMaggio", "Cy Young", "Mickey Mantle", "Roberto Clemente", "Lefty Grove", "Sandy Koufax", },
            {"Ty Cobb", "Lou Gehrig", "Rogers Hornsby", "Grover Alexander", "Jimmie Foxx", "Josh Gibson", "Warren Spahn", "Eddie Collins", "Tris Speaker", }};
    static ArrayList<String> myNames = new ArrayList<String>();

    public Team(int templateId){
        myColor = colors[templateId];
        for (int i = 0; i < names[0].length; i++){
            myNames.add(names[templateId][i]);
        }
        atBat = getNextBatter();

    }

    public static void setTeamName(String name){
        teamName = name;
    }

    public Color getColor(){
        return myColor;
    }

    public static String[] getCorrectDiamond(){
        String[] temp = {firstBase, secondBase, thirdBase};
        return temp;
    }

    public static String getRunnersOn(){
        StringBuilder stringBuilder = new StringBuilder();
        String onBase;
        if(thirdBase == "")
            stringBuilder.append(0);
        else
            stringBuilder.append(1);
        if(secondBase == "")
            stringBuilder.append(0);
        else
            stringBuilder.append(1);
        if(firstBase == "")
            stringBuilder.append(0);
        else
            stringBuilder.append(1);
        return stringBuilder.toString();
    }

    private static String getNextBatter(){
        String temp = myNames.remove(0);
        myNames.add(temp);
        return temp;
    }

    public static void printDiamond(){
        String[] temp = Team.getCorrectDiamond();
        System.out.println("At bat: " + temp[0]);
        System.out.println("First base: " + temp[1]);
        System.out.println("Second base: " + temp[2]);
        System.out.println("Third base: " + temp[3]);
    }



    //returns increase in score, if present
    public int advanceBases(int level){
        int answer = 0;
        switch (level){
            case 1 : answer = Team.advanceOne();
                break;
            case 2 : answer = Team.advanceTwo();
                break;
            case 3 : answer = Team.advanceThree();
                break;
        }
        totalScore += answer;
        return answer;
    }



    public static int advanceOne(){
        int score = 0;
        if (!thirdBase.equals("")){
            score = 1;
        }
        thirdBase = secondBase;
        secondBase = firstBase;
        firstBase = atBat;
        atBat = getNextBatter();
        return score;
    }

    private static int advanceTwo(){
        int score = 0;
        if (!thirdBase.equals("") && !secondBase.equals("")){
            score = 2;
        }
        else if (!thirdBase.equals("")){
            score = 1;
        }
        thirdBase = secondBase;
        secondBase = atBat;
        firstBase = "";
        atBat = getNextBatter();
        return score;
    }

    private static int advanceThree(){
        int score = 0;
        if (!thirdBase.equals("") && !secondBase.equals("") && !firstBase.equals("")){
            score = 3;
        }
        else if (!thirdBase.equals("") && !secondBase.equals("")){
            score = 2;
        }
        else if (!thirdBase.equals("")){
            score = 1;
        }

        thirdBase = atBat;
        secondBase = "";
        firstBase = "";
        atBat = getNextBatter();
        return score;

    }

    public static void main(String[] args){
        Team t = new Team(0);
        int score = 0;
        System.out.println("initial first: " + firstBase);
        System.out.println("initial second: " + secondBase);
        System.out.println("initial third: " + thirdBase);
        System.out.println("initial atBat: " + atBat);
        System.out.println();
        score += t.advanceBases(1);
        System.out.println("after one");
        System.out.println(score);
        System.out.println(firstBase);
        System.out.println(secondBase);
        System.out.println(thirdBase);
        System.out.println(atBat);
        System.out.println();
        score += t.advanceBases(1);
        System.out.println("after another");
        System.out.println(score);
        System.out.println(firstBase);
        System.out.println(secondBase);
        System.out.println(thirdBase);
        System.out.println(atBat);
        System.out.println();
        score += t.advanceBases(1);
        System.out.println("after another");
        System.out.println(score);
        System.out.println(firstBase);
        System.out.println(secondBase);
        System.out.println(thirdBase);
        System.out.println(atBat);
        System.out.println();
        score += t.advanceBases(1);
        System.out.println("after another");
        System.out.println(score);
        System.out.println(firstBase);
        System.out.println(secondBase);
        System.out.println(thirdBase);
        System.out.println(atBat);
        System.out.println();
        score += 	t.advanceBases(2);
        System.out.println("after another");
        System.out.println(score);
        System.out.println(firstBase);
        System.out.println(secondBase);
        System.out.println(thirdBase);
        System.out.println(atBat);
        System.out.println();
        score += 	t.advanceBases(1);
        System.out.println("after another");
        System.out.println(score);
        System.out.println(firstBase);
        System.out.println(secondBase);
        System.out.println(thirdBase);
        System.out.println(atBat);
        System.out.println();
    }
}