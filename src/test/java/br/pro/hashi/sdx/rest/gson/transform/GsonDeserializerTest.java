package br.pro.hashi.sdx.rest.gson.transform;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
	void returnsWhatGsonReturns() {
		Reader reader = newReader();
		Object body = mockReturn(reader);
		assertSame(body, d.fromReader(reader, Object.class));
	}

	@Test
	void returnsWhatGsonReturnsWithHint() {
		Reader reader = newReader();
		Object body = mockReturn(reader);
		assertSame(body, d.fromReader(reader, new Hint<Object>() {}.getType()));
	}

	@Test
	void throwsUncheckedIOExceptionIfGsonThrowsJsonIOException() {
		Reader reader = newReader();
		Throwable cause = mockThrow(reader, JsonIOException.class);
		Exception exception = assertThrows(UncheckedIOException.class, () -> {
			d.fromReader(reader, Object.class);
		});
		assertSame(cause, exception.getCause().getCause());
	}

	@Test
	void throwsDeserializingExceptionIfGsonThrowsJsonSyntaxException() {
		Reader reader = newReader();
		Throwable cause = mockThrow(reader, JsonSyntaxException.class);
		Exception exception = assertThrows(DeserializingException.class, () -> {
			d.fromReader(reader, Object.class);
		});
		assertSame(cause, exception.getCause());
	}

	private Reader newReader() {
		return new StringReader("content");
	}

	private Object mockReturn(Reader reader) {
		Object body = new Object();
		when(gson.fromJson(eq(reader), (Type) eq(Object.class))).thenReturn(body);
		return body;
	}

	private Throwable mockThrow(Reader reader, Class<? extends Exception> type) {
		Throwable cause = mock(type);
		when(gson.fromJson(eq(reader), (Type) eq(Object.class))).thenThrow(cause);
		return cause;
	}
}
