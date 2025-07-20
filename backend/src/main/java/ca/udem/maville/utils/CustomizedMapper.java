package ca.udem.maville.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.bson.types.ObjectId;

import java.io.IOException;

public class CustomizedMapper {

    // Deserializer : JSON → ObjectId
    private static class ObjectIdDeserializer extends JsonDeserializer<ObjectId> {
        @Override
        public ObjectId deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return new ObjectId(p.getValueAsString());
        }
    }

    // Serializer : ObjectId → JSON
    private static class ObjectIdSerializer extends JsonSerializer<ObjectId> {
        @Override
        public void serialize(ObjectId value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(value.toHexString());
        }
    }

    // Module Jackson personnalisé pour MongoDB
    public static class MongoModule extends SimpleModule {
        public MongoModule() {
            addDeserializer(ObjectId.class, new ObjectIdDeserializer());
            addSerializer(ObjectId.class, new ObjectIdSerializer());
        }
    }

}
