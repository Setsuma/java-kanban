package manager.jsonTransformation.taskDeserialization;

import com.google.gson.*;
import tasks.Task;
import tasks.taskConditions.Status;


import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;

public class TaskDeserializer implements JsonDeserializer<Task> {
    @Override
    public Task deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        Task task = new Task();
        if (jsonObject.get("id") != null) {
            task.setId(jsonObject.get("id").getAsInt());
        }
        task.setName(jsonObject.get("name").getAsString());
        task.setDescription(jsonObject.get("description").getAsString());
        if (jsonObject.get("status") != null) {
            task.setStatus(Status.valueOf(jsonObject.get("status").getAsString()));
        }
        task.setDuration(Duration.ofMinutes(jsonObject.get("duration").getAsInt()));
        task.setStartTime(LocalDateTime.parse(jsonObject.get("startTime").getAsString()));

        return task;
    }
}