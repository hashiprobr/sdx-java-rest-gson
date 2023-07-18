package br.pro.hashi.sdx.rest.gson.transform;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import java.io.UncheckedIOException;
import java.io.Writer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;

class GsonSerializerTest {
	private AutoCloseable mocks;
	private @Mock Gson gson;
	private GsonSerializer s;

	@BeforeEach
	void setUp() {
		mocks = MockitoAnnotations.openMocks(this);

		s = new GsonSerializer(gson);
	}

	@AfterEach
	void tearDown() {
		assertDoesNotThrow(() -> {
			mocks.close();
		});
	}

	@Test
	void doesNotWriteIfGsonThrowsIOException() {
		Object body = new Object();
		Writer writer = Writer.nullWriter();
		Throwable cause = mock(JsonIOException.class);
		doThrow(cause).when(gson).toJson(body, Object.class, writer);
		Exception exception = assertThrows(UncheckedIOException.class, () -> {
			s.write(body, Object.class, writer);
		});
		assertSame(cause, exception.getCause().getCause());
	}
}
