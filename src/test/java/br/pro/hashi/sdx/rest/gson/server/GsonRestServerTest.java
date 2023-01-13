package br.pro.hashi.sdx.rest.gson.server;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import br.pro.hashi.sdx.rest.server.RestServer;

class GsonRestServerTest {
	private RestServer server;

	@BeforeEach
	void setUp() {
		server = mock(RestServer.class);
	}

	@Test
	void builds() {
		try (MockedConstruction<GsonRestServerBuilder> construction = mockBuilderConstruction()) {
			assertSame(server, GsonRestServer.from("package"));
		}
	}

	private MockedConstruction<GsonRestServerBuilder> mockBuilderConstruction() {
		return mockConstruction(GsonRestServerBuilder.class, (mock, context) -> {
			when(mock.build(any())).thenReturn(server);
		});
	}
}
