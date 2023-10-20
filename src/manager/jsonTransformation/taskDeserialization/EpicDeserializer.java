package manager.jsonTransformation.taskDeserialization;

import com.google.gson.*;
import tasks.Epic;
import tasks.taskConditions.Status;


import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;

public class EpicDeserializer implements JsonDeserializer<Epic> {
    @Override
    public Epic deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        Epic epic = new Epic();
        if (jsonObject.get("id") != null) {
            epic.setId(jsonObject.get("id").getAsInt());
        }
        epic.setName(jsonObject.get("name").getAsString());
        epic.setDescription(jsonObject.get("description").getAsString());
        if (jsonObject.get("status") != null) {
            epic.setStatus(Status.valueOf(jsonObject.get("status").getAsString()));
        }
        if (jsonObject.get("duration") != null) {
            epic.setDuration(Duration.ofMinutes(jsonObject.get("duration").getAsInt()));
        }
        if (jsonObject.get("startTime") != null) {
            epic.setStartTime(LocalDateTime.parse(jsonObject.get("startTime").getAsString()));
        }
        if (jsonObject.get("endTime") != null) {
            epic.setEndTime(LocalDateTime.parse(jsonObject.get("endTime").getAsString()));
        }
        return epic;
    }
}
