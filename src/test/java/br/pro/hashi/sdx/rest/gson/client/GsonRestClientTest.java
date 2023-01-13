package br.pro.hashi.sdx.rest.gson.client;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import br.pro.hashi.sdx.rest.client.RestClient;

class GsonRestClientTest {
	private RestClient client;

	@BeforeEach
	void setUp() {
		client = mock(RestClient.class);
	}

	@Test
	void builds() {
		try (MockedConstruction<GsonRestClientBuilder> construction = mockBuilderConstruction()) {
			assertSame(client, GsonRestClient.to("http://a"));
		}
	}

	private MockedConstruction<GsonRestClientBuilder> mockBuilderConstruction() {
		return mockConstruction(GsonRestClientBuilder.class, (mock, context) -> {
			when(mock.build(any())).thenReturn(client);
		});
	}
}
