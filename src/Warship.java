import java.util.ArrayList;
import java.util.Arrays;

public class Warship{

    private ArrayList <int[]> coordinates = new ArrayList<>();
    private String status;

    public Warship() {

    }

    public Warship(ArrayList<int[]> coordinates, String status) {
        this.coordinates = coordinates;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<int[]> getCoordinates() {
        return coordinates;
    }

    @Override
    public String toString(){
        return "coordinates: "+ Arrays.toString(coordinates.toArray()) +", status: "+status;
    }

    //This method checks if coordinates of this Warship is empty. If yes, the Warship gets status 'destroyed', if no, status 'harmed'
    //Return boolean, when true is Warship status 'destroyed'.
    public boolean ifDie(){
        if(this.coordinates.isEmpty()){
            this.status = "destroyed";
            return true;
        }
        this.status = "harmed";
        return false;
    }


}
