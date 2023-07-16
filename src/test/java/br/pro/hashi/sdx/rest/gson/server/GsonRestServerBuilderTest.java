package br.pro.hashi.sdx.rest.gson.server;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;

import com.google.gson.GsonBuilder;

import br.pro.hashi.sdx.rest.Builder;
import br.pro.hashi.sdx.rest.gson.GsonInjector;
import br.pro.hashi.sdx.rest.transform.Deserializer;
import br.pro.hashi.sdx.rest.transform.Serializer;

class GsonRestServerBuilderTest {
	private AutoCloseable mocks;
	private @Mock GsonInjector injector;
	private Answer<GsonInjector> answer;
	private MockedStatic<GsonInjector> injectorStatic;
	private GsonRestServerBuilder b;

	@BeforeEach
	void setUp() {
		mocks = MockitoAnnotations.openMocks(this);

		answer = (invocation) -> {
			Builder<?> builder = invocation.getArgument(0);
			builder.withSerializer("application/json", mock(Serializer.class));
			builder.withDeserializer("application/json", mock(Deserializer.class));
			return null;
		};

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
		doAnswer(answer).when(injector).inject(any());
		b = new GsonRestServerBuilder();
		verify(injector).inject(b);
	}

	@Test
	void constructsWithGsonBuilder() {
		GsonBuilder gsonBuilder = mock(GsonBuilder.class);
		doAnswer(answer).when(injector).inject(any(), eq(gsonBuilder));
		b = new GsonRestServerBuilder(gsonBuilder);
		verify(injector).inject(b, gsonBuilder);
	}

	@Test
	void constructsWithPackageName() {
		String packageName = "package";
		doAnswer(answer).when(injector).inject(any(), eq(packageName));
		b = new GsonRestServerBuilder(packageName);
		verify(injector).inject(b, packageName);
	}

	@Test
	void constructsWithGsonBuilderAndPackageName() {
		GsonBuilder gsonBuilder = mock(GsonBuilder.class);
		String packageName = "package";
		doAnswer(answer).when(injector).inject(any(), eq(gsonBuilder), eq(packageName));
		b = new GsonRestServerBuilder(gsonBuilder, packageName);
		verify(injector).inject(b, gsonBuilder, packageName);
	}
}
