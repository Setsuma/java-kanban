public class Subtask extends Task {
    private int epicId;

    Subtask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
    }

    public Integer getEpicId() {
        return epicId;
    }

    public void setEpicId(Integer epicId) {
        this.epicId = epicId;
    }
}
