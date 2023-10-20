package manager.jsonTransformation.taskSerialization;

import com.google.gson.*;
import tasks.Task;

import java.lang.reflect.Type;

public class TaskSerializer implements JsonSerializer<Task> {

    @Override
    public JsonElement serialize(Task src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        if (src.getId() != null) {
            result.addProperty("id", src.getId());
        }
        result.addProperty("name", src.getName());
        result.addProperty("description", src.getDescription());
        if (src.getStatus() != null) {
            result.addProperty("status", src.getStatus().toString());
        }
        result.addProperty("duration", src.getDuration().toMinutes());
        result.addProperty("startTime", src.getStartTime().toString());

        return result;
    }
}
