import java.util.ArrayList;

public interface CoordinatesIssue {

    void getCoordinatesAround(int[] c, ArrayList<int[]> P);

    ArrayList<int[]> getCoordinatesRow(ArrayList<int[]> C, ArrayList<int[]> P);

}
