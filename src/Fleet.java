import java.util.ArrayList;

public class Fleet{

    private ArrayList<Warship> warships = new ArrayList <>();

    public void setWarships(ArrayList<Warship> warships) {
        this.warships = warships;
    }

    public ArrayList<Warship> getWarships() {
        return warships;
    }

    @Override
    public String toString(){
        return "Warships: "+warships+"\n";
    }

}
