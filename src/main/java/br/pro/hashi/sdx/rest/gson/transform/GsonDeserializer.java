package br.pro.hashi.sdx.rest.gson.transform;

import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import br.pro.hashi.sdx.rest.transform.Deserializer;
import br.pro.hashi.sdx.rest.transform.exception.DeserializingException;

public class GsonDeserializer implements Deserializer {
	private final Gson gson;

	public GsonDeserializer(Gson gson) {
		this.gson = gson;
	}

	@Override
	public <T> T fromReader(Reader reader, Class<T> type) {
		T body;
		try {
			body = gson.fromJson(reader, type);
		} catch (JsonIOException exception) {
			throw new UncheckedIOException(new IOException(exception));
		} catch (JsonSyntaxException exception) {
			throw new DeserializingException(exception);
		}
		return body;
	}
}
