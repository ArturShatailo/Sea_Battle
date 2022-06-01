import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class Turn implements CoordinateTurns, Symbols, CoordinatesIssue {

    private ArrayList<int[]> prevCoordinates = new ArrayList<>();
    private ArrayList <int[]> turnsCoordinatesLibrary = new ArrayList<>();
    private int[] coordinates;

    public int[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(int[] coordinates) {
        this.coordinates = coordinates;
    }

    public void setPrevCoordinates(ArrayList<int[]> prevCoordinates) {
        this.prevCoordinates = prevCoordinates;
    }

//////////////////////////////////////////////////////////////////////////////


    /*
    This method receives Request object @param request and returns coordinates array int[] of the next computer's turn
    If field of Turn object prevCoordinates has size of 1 element, then possible coordinates that will be returned are
    near above, left, right or under the cell in prevCoordinates (method getCoordinatesAround()).
    If the size is more than 1, so the possible coordinates that will be returned are the cell in a row according to getCoordinatesRow()
    method logic. If field prevCoordinates is Empty, the method returns all coordinates excluding used ones.
    If possibleCoordinates has less than 50 elements, the method findFeetPlaces() will be called to find coincidence in cell that wasn't
    chosen according to user's left warships size.
    */
    public int[] possibleTurnCoordinates(Request request){

        ArrayList<int[]> possibleCoordinates = new ArrayList<>();

        if(prevCoordinates.size()>0) {
            if(prevCoordinates.size() == 1) {
                getCoordinatesAround(prevCoordinates.get(0), possibleCoordinates);
            }else{
                possibleCoordinates = getCoordinatesRow(prevCoordinates, possibleCoordinates);
            }

            possibleCoordinates.removeIf(i -> request.getTurnsList().contains(i));

        }else{

            possibleCoordinates.addAll(allCoordinates
                    .stream()
                    .filter(i -> !request.getTurnsList().contains(i))
                    .toList());

            if(possibleCoordinates.size()<=50) return findFeetPlaces(request, possibleCoordinates);

        }

        return possibleCoordinates.get(Tech.getRandom(0, (possibleCoordinates.size()-1)));
    }

    /*
    Method defines the size of the biggest user's "placed" Sea_Battle_Game with help of findCoincidence() method.
    Then, method gets random "i" indicator that defines the way of first sorting (by X axis ot by Y axis),
    horizontal or vertical, to find coordinates rows (cell coincidence rows that were not attacked)
    from @param possibleCoordinates collection with the help of method getStartCoincidence() that returns
    array of coordinates cell int[]. If results of getStartCoincidence() with selected "i" sort indicator
    will be null, the another "i" value (0 or 1) will be used in new method getStartCoincidence() call.
    The array of coordinates cell int[] will be returned.
     */
    private int[] findFeetPlaces(Request request, ArrayList<int[]> possibleCoordinates) {

        int coincidence = findCoincidence(request);
        int i = Tech.getRandom(0, 1);

        int[] nextTurn = getStartCoincidence(i, possibleCoordinates, coincidence);

        if(nextTurn == null){
            return getStartCoincidence(Math.abs(i-1), possibleCoordinates, coincidence);
        }

        return nextTurn;

    }

    //Method defines the biggest size of available Warship in user's list collection of @param request object
    private int findCoincidence(Request request) {

        return request.getList()
                .stream()
                .max(Comparator.comparingInt(ArrayList::size))
                .stream()
                .toList()
                .get(0)
                .size()-1;

    }

    /*
    Method is looking for horizontal or vertical (depends on @param sort value 1 or 0) coincidence of
    several elements (amount is equal to @param coincidence) in @param possibleCoordinates() collection
    of coordinates pairs implemented as int[] arrays.
    */
    public int[] getStartCoincidence(int sort, ArrayList<int[]> possibleCoordinates, int coincidence){
        possibleCoordinates.sort(Comparator.comparingInt(n -> n[sort]));

        return possibleCoordinates
                .stream().skip(coincidence)
                .filter(n -> (n[Math.abs(sort-1)] - possibleCoordinates.get(possibleCoordinates.indexOf(n)-coincidence)[Math.abs(sort-1)]) == coincidence)
                .filter(n -> (n[sort] - possibleCoordinates.get(possibleCoordinates.indexOf(n)-coincidence)[sort]) < 1)
                .findAny()
                .orElse(null);
    }


    /*
    Method checks if coordinates field is equal to any element in coordinates collection field of Warship object
    from Fleet object of the @param request.
    coordinates field is adding to turnsList of @param request.
    In case of coincidence of coordinates field of Turn and element of coordinates collection of Warship, the coordinates Turn field
    will be added to prevCoordinates field. The Sea_Battle_Game object will be defined as a found one according to the coordinates coincidences.
    removeAndMark() method will be called to mark the Sea_Battle_Game as "harmed" or "destroyed" and set it's status accordingly.

    If there are no such Warship with coordinates that match requested coordinates, the warField cell will be filled with
    miss symbol "*". prevCoordinates field is saving to turnsStack in case of miss after successful attack but not all
    coordinates of Sea_Battle_Game are deleted (Sea_Battle_Game status "harmed").
     */
    public boolean check(Request request, WarField field){

        //if (request.getTurnsList().stream().anyMatch(a -> Arrays.equals(coordinates, a))){ System.out.println("You have already attacked this target"); return true; }
        request.getTurnsList().add(coordinates);
        turnsCoordinatesLibrary.clear();

        for (ArrayList<int[]> ints : request.getList()) {

            if(ints.stream().anyMatch(c -> Arrays.equals(c, coordinates))){

                prevCoordinates.add(coordinates);

                Warship warship = field.getFleet().getWarships()
                        .stream()
                        .filter(b -> b.getCoordinates()
                                .stream()
                                .anyMatch(c -> Arrays.equals(c, coordinates)))
                        .toList()
                        .get(0);

                removeAndMark(warship, request, field);

                return true;

            }else{
                request.setTurnsStack(prevCoordinates);
                field.getWarField()[coordinates[0]][coordinates[1]] = "*";
            }
        }
        return false;
    }


    /*
    Remove targeted Sea_Battle_Game coordinates from list collection and from Sea_Battle_Game 'coordinates' field collection.
    targeted coordinates of the Sea_Battle_Game will be marked with '@' symbol on WarField. If Sea_Battle_Game has no
    elements in coordinates field it will be marked as "destroyed" status and it's coordinates will
    be filled around with 'miss' mark "*" to avoid attack on these cells (method fillCellsAround()).
    If Sea_Battle_Game is "destroyed", field prevCoordinates will be cleared for the next turn independence,
     but if Sea_Battle_Game is "harmed" (attacked, but has at least one element in coordinates collection),
    the prevCoordinates will be saved in turnsStack field of Request object to use them in the next Turn.
    Method prints data about Sea_Battle_Game status and coordinates attacked.

    @param Sea_Battle_Game: Warship object with coordinated equals to those were targeted.
    @param request: Request object of user or computer
    @param field: WarField object of user or computer
     */
    private void removeAndMark(Warship warship, Request request, WarField field) {

        field.getWarField()[coordinates[0]][coordinates[1]] = "@";
        warship.getCoordinates().removeIf(c -> Arrays.equals(c, coordinates));
        request.getList().forEach(n -> n.removeIf(c -> Arrays.equals(c, coordinates)));

        if(warship.ifDie()){
            warship.setStatus("Destroyed");
            fillCellsAround(request, field);
            prevCoordinates.clear();
        }else{
            warship.setStatus("Harmed");
            request.setTurnsStack(prevCoordinates);
        }

        System.out.println(abc[coordinates[0]] +""+ coordinates[1] +": "+warship.getStatus());

    }

    //Fills cells around coordinates of "destroyed" Warship with "*" miss symbol to avoid
    //targeting of these guaranteed empty cells
    private void fillCellsAround(Request request, WarField field) {

        for(int[] n: prevCoordinates) {
            turnsCoordinatesLibrary.addAll(allCoordinates.stream()
                    .filter(c -> (Math.abs(c[0] - n[0]) < 2))
                    .filter(c -> (Math.abs(c[1] - n[1]) < 2))
                    .filter(c -> !turnsCoordinatesLibrary.contains(c))
                    .filter(c -> (c[0] != n[0]) && (c[1] != n[1]))
                    .toList());
        }

        turnsCoordinatesLibrary = getCoordinatesRow(prevCoordinates, turnsCoordinatesLibrary);

        turnsCoordinatesLibrary.forEach(request.getTurnsList()::add);
        turnsCoordinatesLibrary.forEach(n -> field.getWarField()[n[0]][n[1]] = "*");

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

    //Method defines the direction of coordinates row in collection @param coordinates and returns 1 cell before
    //and 1 after the row
    @Override
    public ArrayList<int[]> getCoordinatesRow(ArrayList<int[]> prevCoordinates, ArrayList<int[]> possibleCoordinates) {

        if ((prevCoordinates.get(0)[0] - prevCoordinates.get(prevCoordinates.size() - 1)[0]) == 0) {
            prevCoordinates.sort(Comparator.comparingInt(a -> a[1]));

            possibleCoordinates = (additionalCoordinates(prevCoordinates, possibleCoordinates, 0, 1));
        }

        if ((prevCoordinates.get(0)[1] - prevCoordinates.get(prevCoordinates.size() - 1)[1]) == 0) {
            prevCoordinates.sort(Comparator.comparingInt(a -> a[0]));

            possibleCoordinates = (additionalCoordinates(prevCoordinates, possibleCoordinates, 1, 0));
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
                        .filter(c -> ((first[index0]) == c[index0] && (first[index1] - 1) == c[index1]))
                        .toList());

        possibleCoordinates
                .addAll(allCoordinates
                        .stream()
                        .filter(c -> ((last[index0]) == c[index0] && (last[index1] + 1) == c[index1]))
                        .toList());

        return possibleCoordinates;

    }

}
