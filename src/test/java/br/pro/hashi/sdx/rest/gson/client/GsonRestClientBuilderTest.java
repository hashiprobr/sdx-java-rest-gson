package br.pro.hashi.sdx.rest.gson.client;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import com.google.gson.GsonBuilder;

import br.pro.hashi.sdx.rest.gson.GsonInjector;

class GsonRestClientBuilderTest {
	private AutoCloseable mocks;
	private @Mock GsonInjector injector;
	private MockedStatic<GsonInjector> injectorStatic;
	private GsonRestClientBuilder b;

	@BeforeEach
	void setUp() {
		mocks = MockitoAnnotations.openMocks(this);

		injectorStatic = mockStatic(GsonInjector.class);
		injectorStatic.when(() -> GsonInjector.getInstance()).thenReturn(injector);
	}

	@AfterEach
	void tearDown() {
		injectorStatic.close();
		assertDoesNotThrow(() -> {
			mocks.close();
		});
	}

	@Test
	void constructsWithNoArgs() {
		b = new GsonRestClientBuilder();
		verify(injector).inject(b);
	}

	@Test
	void constructsWithGsonBuilder() {
		GsonBuilder gsonBuilder = mock(GsonBuilder.class);
		b = new GsonRestClientBuilder(gsonBuilder);
		verify(injector).inject(b, gsonBuilder);
	}

	@Test
	void constructsWithPackageName() {
		String packageName = "package";
		b = new GsonRestClientBuilder(packageName);
		verify(injector).inject(b, packageName);
	}

	@Test
	void constructsWithGsonBuilderAndPackageName() {
		GsonBuilder gsonBuilder = mock(GsonBuilder.class);
		String packageName = "package";
		b = new GsonRestClientBuilder(gsonBuilder, packageName);
		verify(injector).inject(b, gsonBuilder, packageName);
	}
}
