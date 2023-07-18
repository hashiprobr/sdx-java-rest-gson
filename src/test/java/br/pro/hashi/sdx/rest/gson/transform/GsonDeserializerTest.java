package br.pro.hashi.sdx.rest.gson.transform;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.Reader;
import java.io.UncheckedIOException;
import java.lang.reflect.Type;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import br.pro.hashi.sdx.rest.transform.exception.DeserializingException;

class GsonDeserializerTest {
	private AutoCloseable mocks;
	private @Mock Gson gson;
	private GsonDeserializer d;

	@BeforeEach
	void setUp() {
		mocks = MockitoAnnotations.openMocks(this);

		d = new GsonDeserializer(gson);
	}

	@AfterEach
	void tearDown() {
		assertDoesNotThrow(() -> {
			mocks.close();
		});
	}

	@Test
	void doesNotReadIfGsonThrowsSyntaxException() {
		Reader reader = Reader.nullReader();
		Throwable cause = mock(JsonSyntaxException.class);
		when(gson.fromJson(reader, (Type) Object.class)).thenThrow(cause);
		Exception exception = assertThrows(DeserializingException.class, () -> {
			d.read(reader, Object.class);
		});
		assertSame(cause, exception.getCause());
	}

	@Test
	void doesNotReadIfGsonThrowsIOException() {
		Reader reader = Reader.nullReader();
		Throwable cause = mock(JsonIOException.class);
		when(gson.fromJson(reader, (Type) Object.class)).thenThrow(cause);
		Exception exception = assertThrows(UncheckedIOException.class, () -> {
			d.read(reader, Object.class);
		});
		assertSame(cause, exception.getCause().getCause());
	}
}
