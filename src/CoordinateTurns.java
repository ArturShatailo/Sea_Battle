import java.util.ArrayList;

public interface CoordinateTurns {

    //Collection of all coordinates possible on the WarField
    ArrayList<int[]> allCoordinates = new ArrayList<>();

    default ArrayList<int[]> additionalCoordinates(ArrayList<int[]> C, ArrayList<int[]> P, int i0, int i1){
        return P;
    }

}
