import java.util.ArrayList;
import java.util.Comparator;

public class Request implements Symbols, CoordinateTurns, CoordinatesIssue {

    private final int boats;
    private final int frigates;
    private final int battleships;
    private final int aircraftCarriers;


    private final ArrayList<ArrayList<int[]>> list = new ArrayList<>(); //Collection of collections with arrays (coordinates of each created Warship)
    private final ArrayList<int[]> turnsList = new ArrayList<>(); //Collection of arrays (means coordinates) of all turns made
    private final ArrayList <int[]> coordinatesLibrary = new ArrayList<>(); //Collection of temporary arrays (coordinates) used for local methods
    private ArrayList <int[]> turnsStack = new ArrayList<>(); //Collection of arrays (means coordinates) that was saved from previous Turn if it was successful.


    public Request(int aircraftCarriers, int battleships, int frigates, int boats) {
        this.boats = boats;
        this.frigates = frigates;
        this.battleships = battleships;
        this.aircraftCarriers = aircraftCarriers;
    }




    public ArrayList<int[]> getTurnsStack() {
        return turnsStack;
    }

    public void setTurnsStack(ArrayList<int[]> turnsStack) {
        this.turnsStack = turnsStack;
    }

    public ArrayList<int[]> getTurnsList() {
        return turnsList;
    }

    public ArrayList<ArrayList<int[]>> getList() {
        return list;
    }

    public int getBoats() {
        return boats;
    }

    public int getFrigates() {
        return frigates;
    }

    public int getBattleships() {
        return battleships;
    }

    public int getAircraftCarriers() {
        return aircraftCarriers;
    }



    ////////////////////////////////////////////////////////////////

    //Call newWarship() method of received @param warField with parameter 'list', that is collection of all Warship coordinates collections.
    public void requestImplementation(WarField warField){

        warField.newWarship(list);
        //list.forEach(warField::newWarship);
    }

    //Checks if @param coordinates of Warship should be added to 'list' collection according to validation method checkCoordinates().
    //Returns i that is an iterator for loop from which this method has been called.
    public int ifCreate(ArrayList <int[]> coordinates, int i){

        if(!this.checkCoordinates(coordinates)){
            System.out.println("Set warship coordinates again because you should place your ship's parts in a row and " +
                    "not near to another ships");
            return --i;
        }else{
            list.add(coordinates);
            return i;
        }

    }

    /*
    Checks if received 'coordinates' parameter is acceptable for placement on WarField,
    according to the rule that warships should be place with gap of 1 cell between each other from each side.
    Returns false if coordinates is near to ones that have been already taken, or returns the result of additional
    validation in method possibleCoordinates().
    */
    public boolean checkCoordinates(ArrayList <int[]> coordinates) {
        coordinatesLibrary.clear();
        list.forEach(coordinatesLibrary::addAll);

        for(int[] c1:coordinatesLibrary){
            for(int[] c2:coordinates){
                if((Math.abs(c2[0]-c1[0])<2 && Math.abs(c2[1]-c1[1])<2)){
                    return false;
                }
            }
        }
        return possibleCoordinates(coordinates);
    }

    //If @param coordinates has at least 1 element method isRow will be called in return expression,
    //or in case of doesn't have returns false.
    public static boolean possibleCoordinates(ArrayList<int[]> coordinates) {
        if(coordinates.size()>0) {

            return isRow(coordinates);

        }
        return false;
    }

    //This method defines if user placed coordinates in row after according to chosen before.
    //Returns true if the row is completed and false if row is broken.
    private static boolean isRow(ArrayList<int[]> coordinates){

        if ((coordinates.get(0)[0] - coordinates.get(coordinates.size() - 1)[0]) == 0) {
            return sortAndDefineRow(coordinates, 0, 1);
        }else{
            return sortAndDefineRow(coordinates, 1, 0);
        }

    }
    private static boolean sortAndDefineRow(ArrayList<int[]> coordinates, int i, int k) {

        coordinates.sort(Comparator.comparingInt(a -> a[k]));

        if ((coordinates.get(0)[i] - coordinates.get(coordinates.size() - 1)[i]) == 0){

            int rowCounter = coordinates
                    .stream()
                    .skip(1)
                    .filter(n -> n[k]-1 == coordinates.get(coordinates.indexOf(n)-1)[k])
                    .toList().size();

            if(rowCounter != coordinates.size()-1){
                return false;
            }

            return (coordinates.size() + coordinates.get(0)[k] - 1) == coordinates.get(coordinates.size() - 1)[k];
        }else{
            return false;
        }

    }

    /*
    This method receives collection of arrays as a @param coordinates and returns collection of possible
    coordinates for the next placement. If coordinates size is 1, then possible coordinates that will be returned are
    near above, left, right and under the coordinate in @param coordinates element array (method getCoordinatesAround()).
    If the size is more than 1, so the possible coordinates that will be returned are the cells in a row according to getCoordinatesRow()
    method logic. If @param coordinates is Empty, the method returns all coordinates excluding used ones.
     */
    public ArrayList<int[]> possibleCoordinatesBot(ArrayList<int[]> coordinates) {

        ArrayList<int[]> possibleCoordinates = new ArrayList<>();

        if(coordinates.size()>0) {

            if(coordinates.size() == 1) {
                getCoordinatesAround(coordinates.get(0), possibleCoordinates);
            }else{
                possibleCoordinates = getCoordinatesRow(coordinates, possibleCoordinates);
            }

        }else{
            coordinatesLibrary.clear();
            list.forEach(coordinatesLibrary::addAll);

            possibleCoordinates.addAll(allCoordinates
                            .stream()
                            .filter(i -> !coordinatesLibrary.contains(i))
                            .toList());
        }

        return possibleCoordinates;
    }

    //Method defines 4 cells near @param c1 coordinates (above, under, left, right)
    @Override
    public void getCoordinatesAround(int[] c1, ArrayList<int[]> possibleCoordinates) {

            allCoordinates
                    .stream()
                    .filter(c -> (Math.abs(c[0] - c1[0]) < 2))
                    .filter(c -> (Math.abs(c[1] - c1[1]) < 2))
                    .filter(c -> (c[0] != c1[0]) || (c[1] != c1[1]))
                    .forEach(possibleCoordinates::add);
            possibleCoordinates.removeIf(c -> (Math.abs(c[0] - c1[0]) == 1 && Math.abs(c[1] - c1[1]) == 1));

    }

    //Method defines the direction of coordinates row in collection @param coordinates and with help of
    //additionalCoordinates() method returns 1 cell before and 1 after the row.
    @Override
    public ArrayList<int[]> getCoordinatesRow(ArrayList<int[]> coordinates, ArrayList<int[]> possibleCoordinates) {

        if ((coordinates.get(0)[0] - coordinates.get(coordinates.size() - 1)[0]) == 0) {
            coordinates.sort(Comparator.comparingInt(a -> a[1]));

            possibleCoordinates = (additionalCoordinates(coordinates, possibleCoordinates, 0, 1));
        }

        if ((coordinates.get(0)[1] - coordinates.get(coordinates.size() - 1)[1]) == 0) {
            coordinates.sort(Comparator.comparingInt(a -> a[0]));

            possibleCoordinates = (additionalCoordinates(coordinates, possibleCoordinates, 1, 0));
        }

        return possibleCoordinates;

    }

    /*
    method returns 1 cell before and 1 after the row in @param coordinates

    @param index0: coordinates part (one of 2 possible values, X or Y) that is equal for row,
    e.g.: when X is equal that means that row is vertical
    @param index1: coordinates part (one of 2 possible values, X or Y) that is NOT equal for row,
    e.g.: when Y is equal and X is different that means that row is horizontal
     */
    @Override
    public ArrayList<int[]> additionalCoordinates(ArrayList<int[]> coordinates, ArrayList<int[]> possibleCoordinates, int index0, int index1){
        int[] first = coordinates.get(0);
        int[] last = coordinates.get(coordinates.size() - 1);

        possibleCoordinates
                .addAll(allCoordinates
                        .stream()
                        .filter(c -> (first[index0]) == c[index0] && (first[index1] - 1) == c[index1])
                        .toList());

        possibleCoordinates
                .addAll(allCoordinates
                        .stream()
                        .filter(c -> (last[index0]) == c[index0] && (last[index1] + 1) == c[index1])
                        .toList());

        return possibleCoordinates;
    }

}
