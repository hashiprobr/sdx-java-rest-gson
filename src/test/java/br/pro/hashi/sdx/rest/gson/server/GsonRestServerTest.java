package br.pro.hashi.sdx.rest.gson.server;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import br.pro.hashi.sdx.rest.server.RestServer;
import br.pro.hashi.sdx.rest.server.RestServerBuilder;

class GsonRestServerTest {
	private RestServer server;

	@Test
	void builds() {
		try (MockedConstruction<GsonRestServerBuilder> construction = mockBuilderConstruction()) {
			assertSame(server, GsonRestServer.from("package"));
			RestServerBuilder builder = construction.constructed().get(0);
			verify(builder).build("package");
		}
	}

	private MockedConstruction<GsonRestServerBuilder> mockBuilderConstruction() {
		server = mock(RestServer.class);
		return mockConstruction(GsonRestServerBuilder.class, (mock, context) -> {
			when(mock.build("package")).thenReturn(server);
		});
	}
}
