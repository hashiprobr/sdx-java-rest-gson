package br.pro.hashi.sdx.rest.gson.transform;

import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;

import br.pro.hashi.sdx.rest.transform.Deserializer;
import br.pro.hashi.sdx.rest.transform.exception.DeserializingException;

public class GsonDeserializer implements Deserializer {
	private final Gson gson;

	public GsonDeserializer(Gson gson) {
		this.gson = gson;
	}

	@Override
	public <T> T read(Reader reader, Type type) {
		if (type.equals(JsonReader.class)) {
			@SuppressWarnings("unchecked")
			T body = (T) gson.newJsonReader(reader);
			return body;
		}
		try {
			try {
				if (type.equals(JsonElement.class)) {
					@SuppressWarnings("unchecked")
					T body = (T) JsonParser.parseReader(reader);
					return body;
				}
				return gson.fromJson(reader, type);
			} catch (JsonSyntaxException exception) {
				throw new DeserializingException(exception);
			} catch (JsonIOException exception) {
				throw new IOException(exception);
			} finally {
				reader.close();
			}
		} catch (IOException exception) {
			throw new UncheckedIOException(exception);
		}
	}
}
