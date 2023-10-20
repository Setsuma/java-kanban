package manager.jsonTransformation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import manager.jsonTransformation.taskDeserialization.EpicDeserializer;
import manager.jsonTransformation.taskDeserialization.SubtaskDeserializer;
import manager.jsonTransformation.taskDeserialization.TaskDeserializer;
import manager.jsonTransformation.taskSerialization.EpicSerializer;
import manager.jsonTransformation.taskSerialization.SubtaskSerializer;
import manager.jsonTransformation.taskSerialization.TaskSerializer;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

public class TaskGson {
    public static Gson getTaskGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Task.class, new TaskSerializer())
                .registerTypeAdapter(Epic.class, new EpicSerializer())
                .registerTypeAdapter(Subtask.class, new SubtaskSerializer())
                .registerTypeAdapter(Task.class, new TaskDeserializer())
                .registerTypeAdapter(Epic.class, new EpicDeserializer())
                .registerTypeAdapter(Subtask.class, new SubtaskDeserializer())
                .serializeNulls()
                .create();
    }
}
