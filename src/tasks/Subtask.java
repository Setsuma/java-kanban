package tasks;

import java.time.LocalDateTime;
import java.util.Objects;

public class Subtask extends Task {
    private final int epicId;

    public Subtask(String name, String description, int epicId, LocalDateTime startTime, long durationInMinutes) {
        super(name, description, startTime, durationInMinutes);
        this.epicId = epicId;
    }

    public Integer getEpicId() {
        return epicId;
    }

    @Override
    public TaskType getType() {
        return TaskType.SUBTASK;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return epicId == subtask.epicId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "id=" + id +
                ", name='" + name +
                ", description='" + description +
                ", status=" + status +
                ", epicId=" + epicId +
                ", duration=" + duration +
                ", startTime=" + startTime.format(dateFormat) +
                ", endTime=" + getEndTime().format(dateFormat) +
                '}';
    }
}
