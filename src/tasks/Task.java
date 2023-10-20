package tasks;

import tasks.taskConditions.Status;
import tasks.taskConditions.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {
    protected String name;
    protected String description;
    protected Integer id;
    protected Status status;
    protected Duration duration;
    protected LocalDateTime startTime;
    protected final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd.MM.yy_HH:mm");

    public Task(String name, String description, LocalDateTime startTime, long durationInMinutes) {
        this.name = name;
        this.description = description;
        this.startTime = startTime;
        this.duration = Duration.ofMinutes(durationInMinutes);
    }

    protected Task(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Task(){};

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public TaskType getType() {
        return TaskType.TASK;
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return name.equals(task.name) && description.equals(task.description) && id.equals(task.id) && status == task.status
                && startTime.equals(task.startTime) && duration.equals(task.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, id, status, duration, startTime);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name +
                ", description=" + description +
                ", status=" + status +
                ", duration=" + duration +
                ", startTime=" + startTime.format(dateFormat) +
                ", endTime=" + getEndTime().format(dateFormat) +
                '}';
    }
}