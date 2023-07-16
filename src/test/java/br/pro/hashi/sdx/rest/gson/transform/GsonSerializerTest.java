package br.pro.hashi.sdx.rest.gson.transform;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.function.Consumer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.stream.JsonWriter;

import br.pro.hashi.sdx.rest.Hint;
import br.pro.hashi.sdx.rest.transform.Serializer;

class GsonSerializerTest {
	private Gson gson;
	private Serializer s;

	@BeforeEach
	void setUp() {
		gson = mock(Gson.class);
		s = new GsonSerializer(gson);
	}

	@Test
	void writes() {
		Object body = new Object();
		doAnswer((invocation) -> {
			Writer writer = invocation.getArgument(2);
			writer.write("body");
			return null;
		}).when(gson).toJson(eq(body), eq(Object.class), any(Writer.class));
		StringWriter writer = new StringWriter();
		s.write(body, writer);
		assertContentEquals("body", writer);
	}

	@Test
	void writesNull() {
		StringWriter writer = new StringWriter();
		s.write(null, writer);
		assertContentEquals("", writer);
	}

	private void assertContentEquals(String expected, StringWriter writer) {
		assertEquals(expected, writer.toString());
	}

	@Test
	void doesNotWriteIfGsonThrowsJsonIOException() {
		Object body = new Object();
		Writer writer = new StringWriter();
		Throwable cause = mock(JsonIOException.class);
		doThrow(cause).when(gson).toJson(eq(body), eq(Object.class), any(Writer.class));
		Exception exception = assertThrows(UncheckedIOException.class, () -> {
			s.write(body, writer);
		});
		assertSame(cause, exception.getCause().getCause());
	}

	@Test
	void doesNotWriteIfGsonThrowsIOException() throws IOException {
		Consumer<JsonWriter> body = (jsonWriter) -> {};
		Type type = new Hint<Consumer<JsonWriter>>() {}.getType();
		Writer writer = new StringWriter();
		Throwable cause = new IOException();
		when(gson.newJsonWriter(writer)).thenThrow(cause);
		Exception exception = assertThrows(UncheckedIOException.class, () -> {
			s.write(body, type, writer);
		});
		assertSame(cause, exception.getCause());
	}
}
