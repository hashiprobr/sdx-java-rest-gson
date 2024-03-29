package br.pro.hashi.sdx.rest.gson.client;

import br.pro.hashi.sdx.rest.client.RestClient;
import br.pro.hashi.sdx.rest.client.RestClientBuilder;

/**
 * Convenience class that builds a default REST client with Gson support.
 */
public final class GsonRestClient {
	/**
	 * <p>
	 * Builds a dynamic HTTP/2 and HTTP/1.1 client with a default configuration to
	 * the specified URL prefix.
	 * </p>
	 *
	 * @param urlPrefix the URL prefix
	 * @return the client
	 * @throws NullPointerException     if the URL prefix is null
	 * @throws IllegalArgumentException if the URL prefix is invalid
	 */
	public static RestClient to(String urlPrefix) {
		RestClientBuilder builder = new GsonRestClientBuilder();
		return builder.build(urlPrefix);
	}

	private GsonRestClient() {
	}
}
