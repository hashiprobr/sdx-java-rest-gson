package br.pro.hashi.sdx.rest.gson.transform;

import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.function.Consumer;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;

import br.pro.hashi.sdx.rest.transform.Hint;
import br.pro.hashi.sdx.rest.transform.Serializer;
import br.pro.hashi.sdx.rest.transform.extension.Plumber;

public class GsonSerializer implements Serializer {
	private final Plumber plumber;
	private final Gson gson;

	public GsonSerializer(Gson gson) {
		this.plumber = new Plumber();
		this.gson = gson;
	}

	@Override
	public <T> void write(T body, Class<T> type, Writer writer) {
		write(body, (Type) type, writer);
	}

	@Override
	public <T> void write(T body, Hint<T> hint, Writer writer) {
		write(body, hint.getType(), writer);
	}

	private <T> void write(T body, Type type, Writer writer) {
		try {
			gson.toJson(body, type, writer);
		} catch (JsonIOException exception) {
			throw new UncheckedIOException(new IOException(exception));
		}
	}

	@Override
	public <T> Reader toReader(T body, Class<T> type) {
		return toReader(body, (Type) type);
	}

	@Override
	public <T> Reader toReader(T body, Hint<T> hint) {
		return toReader(body, hint.getType());
	}

	private <T> Reader toReader(T body, Type type) {
		Reader reader;
		Consumer<Writer> consumer = (writer) -> {
			gson.toJson(body, type, writer);
		};
		try {
			reader = plumber.connect(consumer);
		} catch (IOException exception) {
			throw new UncheckedIOException(exception);
		}
		return reader;
	}
}
