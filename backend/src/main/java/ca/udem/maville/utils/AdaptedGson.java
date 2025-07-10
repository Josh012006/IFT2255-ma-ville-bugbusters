package ca.udem.maville.utils;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import io.javalin.json.JsonMapper;
import com.google.gson.Gson;

public class AdaptedGson {



    public static class GsonMapper implements JsonMapper {
        private final Gson gson = new Gson();

        @Override
        public <T> T fromJsonString(String json, Type targetType) {
            return gson.fromJson(json, targetType);
        }

        @Override
        public String toJsonString(Object obj, Type type) {
            return gson.toJson(obj);
        }
    }


    private static class IsoDateAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {

        @Override
        public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
            // Convertir Date en Instant puis en String ISO 8601
            Instant instant = src.toInstant();
            String isoString = DateTimeFormatter.ISO_INSTANT.format(instant);
            return new JsonPrimitive(isoString);
        }

        @Override
        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            // Convertir String ISO 8601 en Date via Instant
            String dateStr = json.getAsString();
            Instant instant = Instant.parse(dateStr);
            return Date.from(instant);
        }
    }

    public static Gson getGsonInstance() {
        return new GsonBuilder()
                .registerTypeAdapter(Date.class, new IsoDateAdapter())
                .create();
    }
}
