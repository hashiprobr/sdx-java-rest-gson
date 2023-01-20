package br.pro.hashi.sdx.rest.gson.transform;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;

import br.pro.hashi.sdx.rest.transform.Serializer;

public class GsonSerializer implements Serializer {
	private final Gson gson;

	public GsonSerializer(Gson gson) {
		this.gson = gson;
	}

	@Override
	public void write(Object body, Type type, Writer writer) {
		try {
			gson.toJson(body, type, writer);
		} catch (JsonIOException exception) {
			throw new UncheckedIOException(new IOException(exception));
		} finally {
			try {
				writer.close();
			} catch (IOException exception) {
				throw new UncheckedIOException(exception);
			}
		}
	}
}
