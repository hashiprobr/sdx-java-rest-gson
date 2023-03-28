package br.pro.hashi.sdx.rest.gson.transform;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import java.io.StringWriter;
import java.io.UncheckedIOException;
import java.io.Writer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;

import br.pro.hashi.sdx.rest.transform.Hint;
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
		Object body = mockGsonReturn();
		StringWriter writer = new StringWriter();
		s.write(body, writer);
		assertEqualsBody(writer);
	}

	@Test
	void writesWithHint() {
		Object body = mockGsonReturn();
		StringWriter writer = new StringWriter();
		s.write(body, new Hint<Object>() {}.getType(), writer);
		assertEqualsBody(writer);
	}

	private void assertEqualsBody(StringWriter writer) {
		assertEquals("body", writer.toString());
	}

	private Object mockGsonReturn() {
		Object body = new Object();
		doAnswer((invocation) -> {
			Appendable appendable = invocation.getArgument(2);
			appendable.append("body");
			return null;
		}).when(gson).toJson(eq(body), eq(Object.class), any(Appendable.class));
		return body;
	}

	@Test
	void doesNotWriteIfGsonThrowsJsonIOException() {
		Object body = new Object();
		Throwable cause = mock(JsonIOException.class);
		doThrow(cause).when(gson).toJson(eq(body), eq(Object.class), any(Appendable.class));
		Writer writer = new StringWriter();
		Exception exception = assertThrows(UncheckedIOException.class, () -> {
			s.write(body, writer);
		});
		assertSame(cause, exception.getCause().getCause());
	}
}
