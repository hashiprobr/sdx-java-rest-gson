package br.pro.hashi.sdx.rest.gson.transform;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.function.Consumer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;

import br.pro.hashi.sdx.rest.transform.Hint;
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
	void writesWhatGsonWrites() {
		Object body = mockGson();
		StringWriter writer = new StringWriter();
		s.write(body, writer);
		assertEqualsBody(writer);
	}

	@Test
	void writesWhatGsonWritesWithHint() {
		Object body = mockGson();
		StringWriter writer = new StringWriter();
		s.write(body, new Hint<Object>() {}.getType(), writer);
		assertEqualsBody(writer);
	}

	private void assertEqualsBody(StringWriter writer) {
		assertEqualsBody(writer.toString());
	}

	@Test
	void writeThrowsUncheckedIOExceptionIfGsonThrowsJsonIOException() throws IOException {
		Object body = new Object();
		Throwable cause = mock(JsonIOException.class);
		doThrow(cause).when(gson).toJson(eq(body), (Type) eq(Object.class), any(Appendable.class));
		Writer writer = new StringWriter();
		Exception exception = assertThrows(UncheckedIOException.class, () -> {
			s.write(body, writer);
		});
		assertSame(cause, exception.getCause().getCause());
	}

	@Test
	void readsWhatGsonWrites() throws IOException {
		Object body = mockGson();
		mockPlumber();
		Reader reader = s.toReader(body);
		assertEqualsBody(reader);
	}

	@Test
	void readsWhatGsonWritesWithHint() throws IOException {
		Object body = mockGson();
		mockPlumber();
		Reader reader = s.toReader(body, new Hint<Object>() {}.getType());
		assertEqualsBody(reader);
	}

	private void assertEqualsBody(Reader reader) throws IOException {
		char[] chars = new char[4];
		int offset = 0;
		int remaining = chars.length;
		while (remaining > 0) {
			int length = reader.read(chars, offset, remaining);
			offset += length;
			remaining -= length;
		}
		assertEquals(-1, reader.read());
		assertEqualsBody(new String(chars));
		reader.close();
	}

	@Test
	void throwsUncheckedIOExceptionIfPlumberThrowsIOException() throws IOException {
		Object body = mockGson();
		Throwable cause = new IOException();
		when(plumber.connect(any())).thenThrow(cause);
		Exception exception = assertThrows(UncheckedIOException.class, () -> {
			s.toReader(body);
		});
		assertSame(cause, exception.getCause());
	}

	private Object mockGson() {
		Object body = new Object();
		doAnswer((invocation) -> {
			Appendable appendable = invocation.getArgument(2);
			appendable.append("body");
			return null;
		}).when(gson).toJson(eq(body), (Type) eq(Object.class), any(Appendable.class));
		return body;
	}

	private void mockPlumber() throws IOException {
		when(plumber.connect(any())).thenAnswer((invocation) -> {
			Consumer<Writer> consumer = invocation.getArgument(0);
			Writer writer = new StringWriter();
			consumer.accept(writer);
			return new StringReader(writer.toString());
		});
	}

	private void assertEqualsBody(String content) {
		assertEquals("body", content);
	}
}
