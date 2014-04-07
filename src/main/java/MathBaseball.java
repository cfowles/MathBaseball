import java.util.Random;

public class MathBaseball{
    static Random generator = new Random();
    static PlayBall gui;
    static int answer;
    static int player;
    static DBWrapper dataBase;

    public MathBaseball(int playerId){
        /*try {
            dataBase = new DBWrapper();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        player = playerId;*/
    }

    public static void makeGui(char type, DBWrapper db){
        System.out.println(type);
        gui = new PlayBall(type,db);
        gui.beginGame();
    }

    public static void answerReceived(int input, char type){
        if (input == answer){
            gui.displayCorrect();
        }
        else{
            if (type == 'a'){
                gui.displayWrongAdd(answer);
            }
            else if (type == 's'){
                gui.displayWrongSub(answer);
            }
            else{
                gui.displayWrongPlaces(answer);
            }
        }
    }

    public static void generateQuestion(int max, int type){
        int a = generator.nextInt(max);
        int b = generator.nextInt(max);

        if (type == 1){ // addition
            gui.displayAdditionQuestion(a, b);

            answer = a + b;


        }
        else if (type == 2){ // subtraction
            if (a < b){
                int temp;
                temp = a;
                a = b;
                b = temp;
            }
            gui.displaySubtractionQuestion(a, b);

            answer = a - b;

        }
        else{
            MathBaseball.generatePlaceQuestion();
        }
    }

    public static void generatePlaceQuestion(){
        int given = generator.nextInt(900);
        given = given + 100;
        int place = generator.nextInt(2);
        place++;
        String intStr = Integer.toString(given);
        //make sure each place is unique
        //needed?
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
        //print questions and check answer

        if (place == 1){
            gui.displayPlacesQuestion(given, place);
            answer =  intStr.charAt(0);

        }
        else if (place == 2){
            gui.displayPlacesQuestion(given, place);
            answer= intStr.charAt(1);

        }
        else{
            gui.displayPlacesQuestion(given, place);
            answer = intStr.charAt(2);

        }




    }

}