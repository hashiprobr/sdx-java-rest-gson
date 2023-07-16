package br.pro.hashi.sdx.rest.gson.server;

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

import br.pro.hashi.sdx.rest.server.RestServer;

class GsonRestServerTest {
	private static final String PACKAGE_NAME = "package";

	private AutoCloseable mocks;
	private @Mock RestServer s;

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
		MockInitializer<GsonRestServerBuilder> initializer = (mock, context) -> {
			when(mock.build(PACKAGE_NAME)).thenReturn(s);
		};
		try (MockedConstruction<GsonRestServerBuilder> construction = mockConstruction(GsonRestServerBuilder.class, initializer)) {
			assertSame(s, GsonRestServer.from(PACKAGE_NAME));
		}
	}
}
