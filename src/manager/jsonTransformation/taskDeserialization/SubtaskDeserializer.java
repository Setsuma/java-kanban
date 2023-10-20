package manager.jsonTransformation.taskDeserialization;

import com.google.gson.*;
import tasks.Subtask;
import tasks.taskConditions.Status;


import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;

public class SubtaskDeserializer implements JsonDeserializer<Subtask> {
    @Override
    public Subtask deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        Subtask subtask = new Subtask();
        if (jsonObject.get("id") != null) {
            subtask.setId(jsonObject.get("id").getAsInt());
        }
        subtask.setName(jsonObject.get("name").getAsString());
        subtask.setDescription(jsonObject.get("description").getAsString());
        if (jsonObject.get("status") != null) {
            subtask.setStatus(Status.valueOf(jsonObject.get("status").getAsString()));
        }
        subtask.setEpicId(jsonObject.get("epicId").getAsInt());
        subtask.setDuration(Duration.ofMinutes(jsonObject.get("duration").getAsInt()));
        subtask.setStartTime(LocalDateTime.parse(jsonObject.get("startTime").getAsString()));

        return subtask;
    }
}
