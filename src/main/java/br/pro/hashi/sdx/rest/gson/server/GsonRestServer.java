package br.pro.hashi.sdx.rest.gson.server;

import br.pro.hashi.sdx.rest.server.RestServer;

/**
 * Stub.
 */
public final class GsonRestServer {
	/**
	 * Stub.
	 * 
	 * @param packageName stub
	 * @return stub
	 */
	public static RestServer from(String packageName) {
		return new GsonRestServerBuilder().build(packageName);
	}

	private GsonRestServer() {
	}
}
