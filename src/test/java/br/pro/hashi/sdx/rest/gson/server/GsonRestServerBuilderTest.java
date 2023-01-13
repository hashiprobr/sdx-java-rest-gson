package br.pro.hashi.sdx.rest.gson.server;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import com.google.gson.GsonBuilder;

import br.pro.hashi.sdx.rest.gson.GsonInjector;

class GsonRestServerBuilderTest {
	private MockedConstruction<GsonInjector> construction;
	private GsonRestServerBuilder b;

	@BeforeEach
	void setUp() {
		construction = mockConstruction(GsonInjector.class);
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
