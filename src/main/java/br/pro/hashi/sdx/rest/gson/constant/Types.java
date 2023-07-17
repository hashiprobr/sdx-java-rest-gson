package br.pro.hashi.sdx.rest.gson.constant;

import java.lang.reflect.Type;
import java.util.function.Consumer;

import com.google.gson.stream.JsonWriter;

import br.pro.hashi.sdx.rest.Hint;

public final class Types {
	private static final Type WRITER_CONSUMER = new Hint<Consumer<JsonWriter>>() {}.getType();

	public static boolean instanceOfWriterConsumer(Object body, Type type) {
		return body != null && type.equals(WRITER_CONSUMER);
	}

	private Types() {
	}
}
