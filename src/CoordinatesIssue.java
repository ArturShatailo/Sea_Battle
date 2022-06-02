import java.util.ArrayList;

public interface CoordinatesIssue {

    void getCoordinatesAround(int[] c, ArrayList<int[]> P);

    ArrayList<int[]> getCoordinatesRow(ArrayList<int[]> C, ArrayList<int[]> P);

    ArrayList<int[]> additionalCoordinates(ArrayList<int[]> C, ArrayList<int[]> P, int i0, int i1);

}
