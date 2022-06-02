import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class WarField implements Symbols{

    private final String [][] warFieldUI;
    private final String [][] warField;
    private final Fleet fleet = new Fleet();
    private final int horizontalSize;
    private final int verticalSize;

    public WarField(int horizontalSize, int verticalSize) {
        this.warFieldUI = new String[horizontalSize+1][verticalSize+1];
        this.warField = new String[horizontalSize][verticalSize];
        this.horizontalSize = horizontalSize;
        this.verticalSize = verticalSize;
    }

    public Fleet getFleet() {
        return fleet;
    }

    public String[][] getWarField() {
        return warField;
    }

///////////////////////////////////////////////////

    //New Warship creation with 'status' field "placed" and coordinates taken from list collection. Collection of Warship objects
    //set as a 'warships' field in 'fleet' field (Fleet object)
    public void newWarship(ArrayList<ArrayList<int[]>> list){

        fleet.setWarships(list
                .stream()
                .map(a -> new Warship(a, "placed"))
                .collect(Collectors.toCollection(ArrayList::new)));

    }

    //Method call onField() method for each Warship object in fleet field.
    public void fillCoordinates(){
        fleet.getWarships().forEach(this::onField);
    }

    //Method checks if coordinates field of received @param Sea_Battle_Game is not empty and if not, calls convertCoordinates() method for each
    //element in coordinates collections.
    public void onField(Warship warship) {
        if(!warship.getCoordinates().isEmpty()){
            warship.getCoordinates().forEach(this::convertCoordinates);
        }
    }

    //Receives @param array (coordinates pair) and set '#' symbol on each equals to @param array coordinates
    //in warField (String 2D array) field.
    public void convertCoordinates(int[] array) {

        warField[array[0]][array[1]] = "#";

    }

    //Fill all warField field (String 2D array) coordinates (cells) with empty Strings " ".
    public void createField(){
        for (String[] strings : warField) {
            Arrays.fill(strings, " ");
        }
    }

    //Printing cell structure with filled symbols in warField String array field.
    public void printUI(){
        System.out.print("\n");
        for(int i=0; i<warFieldUI.length; i++){
            for(int k=0; k<warFieldUI[i].length; k++){

                if(i==0 && k==0){
                    warFieldUI[i][k] = "  - ";
                }else if(i==0){
                    if(k>10){
                        warFieldUI[i][k] = " "+(k-1);
                    }else {
                        warFieldUI[i][k] = " " + (k - 1) + " ";
                    }
                }else if(k==0){
                    warFieldUI[i][k] = "  "+abc[i-1]+" ";
                }else{
                    warFieldUI[i][k] = " "+warField[i-1][k-1]+" ";
                }
                System.out.print(warFieldUI[i][k]+" | ");

            }
            System.out.print("\n");
            for(int l = 0; l<=verticalSize; l++){
                System.out.print("-----+");
            }
            System.out.print("\n");
        }
    }

}
