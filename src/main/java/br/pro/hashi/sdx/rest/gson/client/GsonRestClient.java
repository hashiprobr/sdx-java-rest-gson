package br.pro.hashi.sdx.rest.gson.client;

import br.pro.hashi.sdx.rest.client.RestClient;
import br.pro.hashi.sdx.rest.client.RestClientBuilder;

/**
 * Convenience class to quickly build a REST client with Gson support.
 */
public final class GsonRestClient {
	/**
	 * Instantiates a REST client using a specified URL prefix.
	 * 
	 * @param urlPrefix the URL prefix
	 * @return the client
	 */
	public static RestClient to(String urlPrefix) {
		return builder().build(urlPrefix);
	}

	/**
	 * Convenience method for instantiating a REST client builder.
	 * 
	 * @return the client builder
	 */
	public static RestClientBuilder builder() {
		return new GsonRestClientBuilder();
	}

	private GsonRestClient() {
	}
}
