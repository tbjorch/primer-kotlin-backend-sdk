package http.deserializers

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.time.Instant

class InstantTypeAdapter : JsonDeserializer<Instant> {
    override fun deserialize(json: JsonElement, type: Type, context: JsonDeserializationContext): Instant {
        var instantStringRepresentation = json.asJsonPrimitive.asString
        if (!instantStringRepresentation.last().equals('Z', true)) {
            instantStringRepresentation += "Z"
        }
        return Instant.parse(instantStringRepresentation)
    }
}
