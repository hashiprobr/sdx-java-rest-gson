package br.pro.hashi.sdx.rest.gson.client;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedConstruction.MockInitializer;
import org.mockito.MockitoAnnotations;

import br.pro.hashi.sdx.rest.client.RestClient;

class GsonRestClientTest {
	private static final String URL_PREFIX = "http://a";

	private AutoCloseable mocks;
	private @Mock RestClient c;

	@BeforeEach
	void setUp() {
		mocks = MockitoAnnotations.openMocks(this);
	}

	@AfterEach
	void tearDown() {
		assertDoesNotThrow(() -> {
			mocks.close();
		});
	}

	@Test
	void gets() {
		MockInitializer<GsonRestClientBuilder> initializer = (mock, context) -> {
			when(mock.build(URL_PREFIX)).thenReturn(c);
		};
		try (MockedConstruction<GsonRestClientBuilder> construction = mockConstruction(GsonRestClientBuilder.class, initializer)) {
			assertSame(c, GsonRestClient.to(URL_PREFIX));
		}
	}
}
