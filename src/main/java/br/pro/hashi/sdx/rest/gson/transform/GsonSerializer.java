package br.pro.hashi.sdx.rest.gson.transform;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.function.Consumer;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import br.pro.hashi.sdx.rest.gson.constant.Types;
import br.pro.hashi.sdx.rest.transform.Serializer;

public class GsonSerializer implements Serializer {
	private final Gson gson;

	public GsonSerializer(Gson gson) {
		this.gson = gson;
	}

	@Override
	public void write(Object body, Type type, Writer writer) {
		try {
			if (body instanceof JsonReader) {
				JsonReader jsonReader = (JsonReader) body;
				JsonWriter jsonWriter = gson.newJsonWriter(writer);
				JsonToken token;
				do {
					token = jsonReader.peek();
					switch (token) {
					case NULL:
						jsonReader.nextNull();
						jsonWriter.nullValue();
						break;
					case BOOLEAN:
						jsonWriter.value(jsonReader.nextBoolean());
						break;
					case NUMBER:
						String stringValue = jsonReader.nextString();
						Number number;
						if (stringValue.indexOf('.') == -1) {
							number = new BigInteger(stringValue);
						} else {
							number = new BigDecimal(stringValue);
						}
						jsonWriter.value(number);
						break;
					case STRING:
						jsonWriter.value(jsonReader.nextString());
						break;
					case NAME:
						jsonWriter.name(jsonReader.nextName());
						break;
					case BEGIN_ARRAY:
						jsonReader.beginArray();
						jsonWriter.beginArray();
						break;
					case END_ARRAY:
						jsonReader.endArray();
						jsonWriter.endArray();
						break;
					case BEGIN_OBJECT:
						jsonReader.beginObject();
						jsonWriter.beginObject();
						break;
					case END_OBJECT:
						jsonReader.endObject();
						jsonWriter.endObject();
						break;
					default:
					}
				} while (token != JsonToken.END_DOCUMENT);
				jsonReader.close();
				jsonWriter.flush();
				return;
			}
			if (Types.instanceOfWriterConsumer(body, type)) {
				@SuppressWarnings("unchecked")
				Consumer<JsonWriter> consumer = (Consumer<JsonWriter>) body;
				consumer.accept(gson.newJsonWriter(writer));
				return;
			}
			try {
				if (body instanceof JsonElement) {
					gson.toJson((JsonElement) body, writer);
					return;
				}
				gson.toJson(body, type, writer);
			} catch (JsonIOException exception) {
				throw new IOException(exception);
			}
		} catch (IOException exception) {
			throw new UncheckedIOException(exception);
		}
	}
}
