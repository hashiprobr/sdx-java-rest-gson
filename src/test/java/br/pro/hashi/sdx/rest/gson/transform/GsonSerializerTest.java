package br.pro.hashi.sdx.rest.gson.transform;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.util.function.Consumer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import com.google.gson.Gson;

import br.pro.hashi.sdx.rest.transform.Serializer;
import br.pro.hashi.sdx.rest.transform.extension.Plumber;

class GsonSerializerTest {
	private MockedConstruction<Plumber> construction;
	private Gson gson;
	private Serializer s;
	private Plumber plumber;

	@BeforeEach
	void setUp() {
		construction = mockConstruction(Plumber.class);
		gson = mock(Gson.class);
		s = new GsonSerializer(gson);
		plumber = construction.constructed().get(0);
	}

	@AfterEach
	void tearDown() {
		construction.close();
	}

	@Test
	void readsWhatGsonWrites() throws IOException {
		Object body = new Object();
		doAnswer((invocation) -> {
			Appendable appendable = invocation.getArgument(2);
			appendable.append("content");
			return null;
		}).when(gson).toJson(eq(body), eq(Object.class), any(Appendable.class));
		when(plumber.connect(any())).thenAnswer((invocation) -> {
			Consumer<Writer> consumer = invocation.getArgument(0);
			Writer writer = new StringWriter();
			consumer.accept(writer);
			return new StringReader(writer.toString());
		});
		Reader reader = s.toReader(body, Object.class);
		char[] chars = new char[7];
		reader.read(chars, 0, 7);
		assertEquals(-1, reader.read());
		assertEquals("content", new String(chars));
		reader.close();
	}

	@Test
	void throwsUncheckedIOExceptionIfPlumberThrowsIOException() throws IOException {
		Object body = new Object();
		when(plumber.connect(any())).thenThrow(IOException.class);
		assertThrows(UncheckedIOException.class, () -> {
			s.toReader(body, Object.class);
		});
	}
}
