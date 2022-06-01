import java.util.ArrayList;
import java.util.Arrays;

public class Main  implements Symbols{

    //Static winner field
    static String winner;

    public static void main(String[] args){


        //New WarField, new Request creation for both computer and user.
        //Method createField() is called for both created WarFields

        WarField allieField = new WarField();
        Request allieRequest = new Request(4,3,2,1);
        allieField.createField();

        WarField botField = new WarField();
        Request botRequest = new Request(4, 3, 2, 1);
        botField.createField();

        //Method getAllCoordinates
        Tech.getAllCoordinates(10, 10);

        //Method printUI call of user's WarField.
        allieField.printUI();

        /*
        Creating of all Warships according to the Request fields and entered by User data (coordinates).
        Then, printing WarField to show the placement.
         */
////////////////////////////////////////////////Aircraft Carrier
        System.out.println("Please create 1 AIRCRAFT CARRIER.");
        addAircraftCarrier(allieRequest);

        allieRequest.requestImplementation(allieField);
        allieField.fillCoordinates();
        allieField.printUI();

////////////////////////////////////////////////Battleship
        System.out.println("Please create 2 BATTLESHIPS.");
        addBattleships(allieRequest);

        allieRequest.requestImplementation(allieField);
        allieField.fillCoordinates();
        allieField.printUI();

//////////////////////////////////////////////////Frigates
        System.out.println("Please create 3 FRIGATES.");
        addFrigates(allieRequest);

        allieRequest.requestImplementation(allieField);
        allieField.fillCoordinates();
        allieField.printUI();

//////////////////////////////////////////////////////////Boats
        System.out.println("Please create 4 BOATS.");
        addBoats(allieRequest);

        allieRequest.requestImplementation(allieField);
        allieField.fillCoordinates();
        allieField.printUI();


        //Computer's Warships creating method call
        botFactory(botRequest, botField);

        //When winner gets value "user" or "computer", the WIN message will be shown in the console.
        winner = startFight(botRequest, botField, allieRequest, allieField, Tech.getRandom(0, 1) == 0);
        System.out.println("The winner is: " + winner);

    }

    /*
    The next creating Warships methods receive coordinates from console input and converts them into char[]
    for further check and implementation.

    @param request: Request object created for user.
     */
    //Creating ships with 1 coordinate in collection in amount that is equal to the received Request field 'boats'.
    private static void addBoats(Request request) {

        for(int i=0; i< request.getBoats(); i++){
            ArrayList<int[]> coordinates = new ArrayList<>();
            System.out.println("Input coordinates of your BOAT according to the table.");

            coordinates.add(coordinatesToFigures(Tech.GetInputStringFunction().toCharArray()));

            i = request.ifCreate(coordinates, i);
        }

    }

    //Creating ships with 2 coordinate in collection in amount that is equal to the received Request field 'frigates'.
    private static void addFrigates(Request request) {

        for(int i=0; i< request.getFrigates(); i++) {
            ArrayList<int[]> coordinates = new ArrayList<>();
            System.out.println("Input 2 coordinates of your frigate according to the table.");
            System.out.print("section 1: ");
            coordinates.add(coordinatesToFigures(Tech.GetInputStringFunction().toCharArray()));
            System.out.print("section 2: ");
            coordinates.add(coordinatesToFigures(Tech.GetInputStringFunction().toCharArray()));

            i = request.ifCreate(coordinates, i);
        }

    }

    //Creating ships with 3 coordinate in collection in amount that is equal to the received Request field 'battleships'.
    private static void addBattleships(Request request) {

        for(int i=0; i< request.getBattleships(); i++) {
            ArrayList<int[]> coordinates = new ArrayList<>();
            System.out.println("Input 3 coordinates of your battleship according to the table.");

            System.out.print("section 1: ");
            coordinates.add(coordinatesToFigures(Tech.GetInputStringFunction().toCharArray()));
            System.out.print("section 2: ");
            coordinates.add(coordinatesToFigures(Tech.GetInputStringFunction().toCharArray()));
            System.out.print("section 3: ");
            coordinates.add(coordinatesToFigures(Tech.GetInputStringFunction().toCharArray()));

            i = request.ifCreate(coordinates, i);
        }

    }

    //Creating ships with 4 coordinate in collection in amount that is equal to the received Request field 'aircraftCarriers'.
    private static void addAircraftCarrier(Request request) {

        for(int i=0; i<request.getAircraftCarriers(); i++) {
            ArrayList<int[]> coordinates = new ArrayList<>();
            System.out.println("Input 4 coordinates of your aircraft carrier according to the table.");

            System.out.print("section 1: ");
            coordinates.add(coordinatesToFigures(Tech.GetInputStringFunction().toCharArray()));
            System.out.print("section 2: ");
            coordinates.add(coordinatesToFigures(Tech.GetInputStringFunction().toCharArray()));
            System.out.print("section 3: ");
            coordinates.add(coordinatesToFigures(Tech.GetInputStringFunction().toCharArray()));
            System.out.print("section 4: ");
            coordinates.add(coordinatesToFigures(Tech.GetInputStringFunction().toCharArray()));

            i = request.ifCreate(coordinates, i);
        }
    }


    /*
    The next method creates Warships automatically according to rules defined in several methods to adhere the game rules and place coordinates
    correctly. The amount of Warships is taken from Request object's fields 'boats', 'frigates', 'battleships', 'aircraftCarriers'.

    Rules of creating are implemented in possibleCoordinatesBot() and checkCoordinates() methods of the Request object

    @param botField: WarField object created for computer
    @param botRequest: Request object created for computer
     */
    private static void botFactory(Request botRequest, WarField botField){

        for(int i=0; i<botRequest.getAircraftCarriers(); i++) {

            ArrayList<int[]> coordinates = new ArrayList<>();
            ArrayList<int[]> allowed;

            for(int k=0; k<4; k++){
                allowed = botRequest.possibleCoordinatesBot(coordinates);
                coordinates.add(allowed.get(Tech.getRandom(0, (allowed.size()-1))));
            }

            if(!botRequest.checkCoordinates(coordinates)){
                i--;
            }else{
                botRequest.getList().add(coordinates);
            }
        }

        for(int i=0; i<botRequest.getBattleships(); i++) {

            ArrayList<int[]> coordinates = new ArrayList<>();
            ArrayList<int[]> allowed;

            for(int k=0; k<3; k++){
                allowed = botRequest.possibleCoordinatesBot(coordinates);
                coordinates.add(allowed.get(Tech.getRandom(0, (allowed.size()-1))));
            }

            if(!botRequest.checkCoordinates(coordinates)){
                i--;
            }else{
                botRequest.getList().add(coordinates);
            }
        }

        for(int i=0; i<botRequest.getFrigates(); i++) {

            ArrayList<int[]> coordinates = new ArrayList<>();
            ArrayList<int[]> allowed;

            for(int k=0; k<2; k++){
                allowed = botRequest.possibleCoordinatesBot(coordinates);
                coordinates.add(allowed.get(Tech.getRandom(0, (allowed.size()-1))));
            }

            if(!botRequest.checkCoordinates(coordinates)){
                i--;
            }else{
                botRequest.getList().add(coordinates);
            }
        }

        for(int i=0; i<botRequest.getBoats(); i++) {
            ArrayList<int[]> coordinates = new ArrayList<>();
            ArrayList<int[]> allowed = new ArrayList<>(botRequest.possibleCoordinatesBot(coordinates));

            coordinates.add(allowed.get(Tech.getRandom(0, (allowed.size()-1))));

            if(!botRequest.checkCoordinates(coordinates)){
                i--;
            }else{
                botRequest.getList().add(coordinates);
            }
        }

        botRequest.requestImplementation(botField);

        //botField.fillCoordinates();

        botField.printUI();
    }

    //The method that converts entered coordinates from @param char[] to [digit][digit] and returns int[] array.
    public static int[] coordinatesToFigures(char[] c){

        if(Character.isDigit(c[0])){
            return coordinatesArray(c, 0, 1);
        }else{
            return coordinatesArray(c, 1, 0);
        }

    }

    private static int[] coordinatesArray(char[] c, int i, int k) {

        int[] array = new int[2];
        array[1] = Integer.parseInt(String.valueOf(c[i]));
        array[0] = Arrays.binarySearch(abc, String.valueOf(c[k]).toLowerCase());

        return array;

    }


    /*
    Start fight method checks if static field 'winner' is defined. If no, then check whose turn is now and calls an appropriate method
    of user's of computer's turn logic. If 'winner' is defined, the method returns 'winner' value and program will display the winner message.

    @param botRequest: Request object of computer
    @param request: Request object of user
    @param botField: WarField object of computer
    @param warField: WarField object of user
    @param queue: the turn queue true/false definer
     */
    public static String startFight(Request botRequest, WarField botField, Request request, WarField warField, boolean queue){

        if(winner == null){
            if(queue){
                userTurn(botRequest, botField, request, warField);
            }else{
                botTurn(botRequest, botField, request, warField);
            }
        }else{
            return winner;
        }
        return winner;
    }


    /*
    The method requests for coordinates entered in console, creates new Turn object and 'active' indicator.
    While 'active' is true, the Turn will be the same (it implements the mechanism when player beats the target and has
    the right for next turn). The entered coordinates is checking for repeating, and then check() method of Turn checks if
    the coordinates are got the target (enemy's Sea_Battle_Game). according to ships coordinates coincidence.

    If there is no Warship with at least 1 coordinates, the winner is defining and the 'winner' static field gets value 'user'

    @param botRequest: Request object of computer
    @param request: Request object of user
    @param botField: WarField object of computer
    @param warField: WarField object of user
     */
    public static void userTurn(Request botRequest, WarField botField, Request request, WarField warField){

        Turn turn = new Turn();
        boolean active = true;

        while(active){
            System.out.println("Input your turn coordinates: ");
            turn.setPrevCoordinates(botRequest.getTurnsStack());

            int[] turnCoordinates = coordinatesToFigures(Tech.GetInputStringFunction().toCharArray());

            if(botRequest.getTurnsList().stream().filter(n -> Arrays.equals(n, turnCoordinates)).toList().isEmpty()){
                turn.setCoordinates(turnCoordinates);
                active = turn.check(botRequest, botField);
                System.out.println("\n\nEnemy's war field");
                botField.printUI();
            }else{
                System.out.println("You have already attacked this target. Input another coordinates: ");
            }

            //turn.setCoordinates(coordinatesToFigures(Tech.GetInputStringFunction().toCharArray())); active = turn.check(botRequest, botField); System.out.println("\n\nEnemy's war field"); botField.printUI();

            if(botField.getFleet().getWarships().stream().allMatch(a -> a.getStatus().equals("Destroyed"))){
                winner = "user";
                break;
            }

        }
        startFight(botRequest, botField, request, warField, false);
    }


    /*
    The method creates new Turn object and 'active' indicator, requests for coordinates from possibleCoordinates() method of Turn object.
    While 'active' is true, the Turn will be the same (it implements the mechanism when player beats the target and has
    the right for next turn). The random chosen coordinates is checking for repeating, and then check() method of Turn checks if
    the coordinates are got the target (enemy's Sea_Battle_Game). according to ships coordinates coincidence.

    If there is no Warship with at least 1 coordinates, the winner is defining and the 'winner' static field gets value 'computer'

    @param botRequest: Request object of computer
    @param request: Request object of user
    @param botField: WarField object of computer
    @param warField: WarField object of user
     */
    public static void botTurn(Request botRequest, WarField botField, Request request, WarField warField){
        Turn turn = new Turn();
        boolean active = true;

        while(active){
            turn.setPrevCoordinates(request.getTurnsStack());
            turn.setCoordinates(turn.possibleTurnCoordinates(request));
            active = turn.check(request, warField);
            System.out.println("Computer's turn: " + abc[turn.getCoordinates()[0]] +""+ turn.getCoordinates()[1]);

            System.out.println("\n\nYour war field");
            warField.printUI();

            if(warField.getFleet().getWarships().stream().allMatch(a -> a.getStatus().equals("Destroyed"))){
                winner = "computer";
                break;
            }

        }
        startFight(botRequest, botField, request, warField, true);
    }
}
