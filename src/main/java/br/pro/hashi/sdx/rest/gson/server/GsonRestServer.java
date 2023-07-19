package br.pro.hashi.sdx.rest.gson.server;

import br.pro.hashi.sdx.rest.server.RestServer;
import br.pro.hashi.sdx.rest.server.RestServerBuilder;

/**
 * Convenience class that builds a default REST server with Gson support.
 */
public final class GsonRestServer {
	/**
	 * Builds a server with a default configuration from the resources in the
	 * specified package.
	 *
	 * @param packageName the package name
	 * @return the server
	 * @throws NullPointerException if the package name is null
	 */
	public static RestServer from(String packageName) {
		RestServerBuilder builder = new GsonRestServerBuilder();
		return builder.build(packageName);
	}

	private GsonRestServer() {
	}
}
