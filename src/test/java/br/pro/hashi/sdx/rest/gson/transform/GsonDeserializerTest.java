package br.pro.hashi.sdx.rest.gson.transform;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.Reader;
import java.io.StringReader;
import java.io.UncheckedIOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import br.pro.hashi.sdx.rest.transform.exception.DeserializingException;

class GsonDeserializerTest {
	private Gson gson;
	private GsonDeserializer d;

	@BeforeEach
	void setUp() {
		gson = mock(Gson.class);
		d = new GsonDeserializer(gson);
	}

	@Test
	void returnsWhatGsonReturns() {
		Reader reader = newReader();
		Object expected = new Object();
		when(gson.fromJson(reader, Object.class)).thenReturn(expected);
		assertSame(expected, d.fromReader(reader, Object.class));
	}

	@Test
	void throwsUncheckedIOExceptionIfGsonThrowsJsonIOException() {
		Reader reader = newReader();
		JsonIOException cause = mock(JsonIOException.class);
		when(gson.fromJson(reader, Object.class)).thenThrow(cause);
		Exception exception = assertThrows(UncheckedIOException.class, () -> {
			d.fromReader(reader, Object.class);
		});
		assertSame(cause, exception.getCause().getCause());
	}

	@Test
	void throwsDeserializingExceptionIfGsonThrowsJsonSyntaxException() {
		Reader reader = newReader();
		JsonSyntaxException cause = mock(JsonSyntaxException.class);
		when(gson.fromJson(reader, Object.class)).thenThrow(cause);
		Exception exception = assertThrows(DeserializingException.class, () -> {
			d.fromReader(reader, Object.class);
		});
		assertSame(cause, exception.getCause());
	}

	private Reader newReader() {
		return new StringReader("content");
	}
}
