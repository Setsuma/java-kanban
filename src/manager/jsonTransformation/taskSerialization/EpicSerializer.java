package manager.jsonTransformation.taskSerialization;

import com.google.gson.*;
import tasks.Epic;

import java.lang.reflect.Type;

public class EpicSerializer implements JsonSerializer<Epic> {

    @Override
    public JsonElement serialize(Epic src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();

        if (src.getId() != null) {
            result.addProperty("id", src.getId());
        }
        result.addProperty("name", src.getName());
        result.addProperty("description", src.getDescription());
        if (src.getStatus() != null) {
            result.addProperty("status", src.getStatus().toString());
        }
        if (!src.getSubtasksIds().isEmpty()) {
            JsonArray subtaskIds = new JsonArray();
            for (int id : src.getSubtasksIds()) {
                subtaskIds.add(id);
            }
            result.add("subtaskIds", subtaskIds);
        }
        result.addProperty("duration", src.getDuration().toMinutes());
        result.addProperty("startTime", src.getStartTime().toString());
        result.addProperty("endTime", src.getEndTime().toString());

        return result;
    }
}