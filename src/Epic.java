import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtaskIds;

    Epic(String name, String description) {
        super(name, description);
        subtaskIds = new ArrayList<>();
    }

    public ArrayList<Integer> getSubtaskIds() {
        return subtaskIds;
    }
}
