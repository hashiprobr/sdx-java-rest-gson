package br.pro.hashi.sdx.rest.gson.transform;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.UncheckedIOException;
import java.lang.reflect.Type;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import br.pro.hashi.sdx.rest.transform.Deserializer;
import br.pro.hashi.sdx.rest.transform.Hint;
import br.pro.hashi.sdx.rest.transform.exception.DeserializingException;

class GsonDeserializerTest {
	private Gson gson;
	private Deserializer d;

	@BeforeEach
	void setUp() {
		gson = mock(Gson.class);
		d = new GsonDeserializer(gson);
	}

	@Test
	void reads() {
		Reader reader = newReader();
		Object body = mockGsonReturn(reader);
		assertSame(body, d.read(reader, Object.class));
	}

	@Test
	void readsWithHint() {
		Reader reader = newReader();
		Object body = mockGsonReturn(reader);
		assertSame(body, d.read(reader, new Hint<Object>() {}.getType()));
	}

	@Test
	void doesNotReadIfCloseThrows() throws IOException {
		Reader reader = spy(newReader());
		doThrow(IOException.class).when(reader).close();
		mockGsonReturn(reader);
		Exception exception = assertThrows(UncheckedIOException.class, () -> {
			d.read(reader, Object.class);
		});
		assertInstanceOf(IOException.class, exception.getCause());
	}

	private Object mockGsonReturn(Reader reader) {
		Object body = new Object();
		when(gson.fromJson(eq(reader), (Type) eq(Object.class))).thenReturn(body);
		return body;
	}

	@Test
	void doesNotReadIfGsonThrowsJsonIOException() {
		Reader reader = newReader();
		Throwable cause = mock(JsonIOException.class);
		mockGsonThrow(reader, cause);
		Exception exception = assertThrows(UncheckedIOException.class, () -> {
			d.read(reader, Object.class);
		});
		assertSame(cause, exception.getCause().getCause());
	}

	@Test
	void doesNotReadIfGsonThrowsJsonSyntaxException() {
		Reader reader = newReader();
		Throwable cause = mock(JsonSyntaxException.class);
		mockGsonThrow(reader, cause);
		Exception exception = assertThrows(DeserializingException.class, () -> {
			d.read(reader, Object.class);
		});
		assertSame(cause, exception.getCause());
	}

	private Throwable mockGsonThrow(Reader reader, Throwable cause) {
		when(gson.fromJson(eq(reader), (Type) eq(Object.class))).thenThrow(cause);
		return cause;
	}

	private Reader newReader() {
		return new StringReader("content");
	}
}
