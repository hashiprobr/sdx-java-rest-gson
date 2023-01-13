package br.pro.hashi.sdx.rest.gson.server;

import br.pro.hashi.sdx.rest.server.RestServer;

/**
 * Convenience class to quickly build a REST server with Gson support.
 */
public final class GsonRestServer {
	/**
	 * Instantiates a REST server using the resources of a specified package.
	 * 
	 * @param packageName the package name
	 * @return the REST server
	 */
	public static RestServer from(String packageName) {
		return new GsonRestServerBuilder().build(packageName);
	}

	private GsonRestServer() {
	}
}
