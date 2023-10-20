package manager.jsonTransformation.taskSerialization;

import com.google.gson.*;
import tasks.Subtask;

import java.lang.reflect.Type;

public class SubtaskSerializer implements JsonSerializer<Subtask> {

    @Override
    public JsonElement serialize(Subtask src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();

        if (src.getId() != null) {
            result.addProperty("id", src.getId());
        }
        result.addProperty("name", src.getName());
        result.addProperty("description", src.getDescription());
        if (src.getStatus() != null) {
            result.addProperty("status", src.getStatus().toString());
        }
        result.addProperty("epicId", src.getEpicId());
        result.addProperty("duration", src.getDuration().toMinutes());
        result.addProperty("startTime", src.getStartTime().toString());

        return result;
    }
}