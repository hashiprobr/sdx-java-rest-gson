package br.pro.hashi.sdx.rest.gson.transform;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.io.Reader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;

class GsonSerializerTest {
	private Gson gson;
	private GsonSerializer s;

	@BeforeEach
	void setUp() {
		gson = mock(Gson.class);
		s = new GsonSerializer(gson);
	}

	@Test
	void readsWhatGsonWrites() throws IOException {
		Object body = new Object();
		doAnswer((invocation) -> {
			Appendable appendable = invocation.getArgument(2);
			appendable.append("content");
			return null;
		}).when(gson).toJson(eq(body), eq(Object.class), any(Appendable.class));
		Reader reader = s.toReader(body, Object.class);
		char[] chars = new char[7];
		reader.read(chars, 0, 7);
		assertEquals(-1, reader.read());
		assertEquals("content", new String(chars));
		reader.close();
	}
}
