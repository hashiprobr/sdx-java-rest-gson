package br.pro.hashi.sdx.rest.gson.server;

import br.pro.hashi.sdx.rest.server.RestServer;

public final class GsonRestServer {
	public static RestServer from(String packageName) {
		return new GsonRestServerBuilder().build();
	}

	private GsonRestServer() {
	}
}
