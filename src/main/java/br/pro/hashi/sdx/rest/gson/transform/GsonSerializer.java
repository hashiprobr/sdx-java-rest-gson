package br.pro.hashi.sdx.rest.gson.transform;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.function.Consumer;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.stream.JsonWriter;

import br.pro.hashi.sdx.rest.transform.Hint;
import br.pro.hashi.sdx.rest.transform.Serializer;

public class GsonSerializer implements Serializer {
	private final Gson gson;
	private final Type consumerType;

	public GsonSerializer(Gson gson) {
		this.gson = gson;
		this.consumerType = new Hint<Consumer<JsonWriter>>() {}.getType();
	}

	@Override
	public void write(Object body, Type type, Writer writer) {
		if (type.equals(consumerType)) {
			@SuppressWarnings("unchecked")
			Consumer<JsonWriter> consumer = (Consumer<JsonWriter>) body;
			JsonWriter jsonWriter;
			try {
				jsonWriter = gson.newJsonWriter(writer);
			} catch (IOException exception) {
				throw new UncheckedIOException(exception);
			}
			consumer.accept(jsonWriter);
		} else {
			try {
				gson.toJson(body, type, writer);
			} catch (JsonIOException exception) {
				throw new UncheckedIOException(new IOException(exception));
			}
		}
	}
}
