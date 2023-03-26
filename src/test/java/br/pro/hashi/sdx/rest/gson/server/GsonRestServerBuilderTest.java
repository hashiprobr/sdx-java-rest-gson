package br.pro.hashi.sdx.rest.gson.server;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

import com.google.gson.GsonBuilder;

import br.pro.hashi.sdx.rest.gson.GsonInjector;
import br.pro.hashi.sdx.rest.transform.Serializer;

class GsonRestServerBuilderTest {
	private MockedConstruction<GsonInjector> construction;
	private GsonRestServerBuilder b;

	@BeforeEach
	void setUp() {
		construction = Mockito.mockConstruction(GsonInjector.class, (mock, context) -> {
			doAnswer((invocation) -> {
				mockContentType(invocation);
				return null;
			}).when(mock).inject(any());
			doAnswer((invocation) -> {
				mockContentType(invocation);
				return null;
			}).when(mock).inject(any(), any(String.class));
			doAnswer((invocation) -> {
				mockContentType(invocation);
				return null;
			}).when(mock).inject(any(), any(GsonBuilder.class));
			doAnswer((invocation) -> {
				mockContentType(invocation);
				return null;
			}).when(mock).inject(any(), any(), any());
		});
	}

	private void mockContentType(InvocationOnMock invocation) {
		GsonRestServerBuilder builder = invocation.getArgument(0);
		builder.withSerializer("application/json", mock(Serializer.class));
	}

	@AfterEach
	void tearDown() {
		construction.close();
	}

	@Test
	void constructsWithNoArgs() {
		b = new GsonRestServerBuilder();
		verify(construction.constructed().get(0)).inject(b);
	}

	@Test
	void constructsWithPackageName() {
		b = new GsonRestServerBuilder("package");
		verify(construction.constructed().get(0)).inject(b, "package");
	}

	@Test
	void constructsWithGsonBuilderAndPackageName() {
		GsonBuilder gsonBuilder = mock(GsonBuilder.class);
		b = new GsonRestServerBuilder(gsonBuilder, "package");
		verify(construction.constructed().get(0)).inject(b, gsonBuilder, "package");
	}

	@Test
	void constructsWithGsonBuilder() {
		GsonBuilder gsonBuilder = mock(GsonBuilder.class);
		b = new GsonRestServerBuilder(gsonBuilder);
		verify(construction.constructed().get(0)).inject(b, gsonBuilder);
	}
}
