package br.pro.hashi.sdx.rest.gson.client;

import br.pro.hashi.sdx.rest.client.RestClient;

/**
 * Convenience class to quickly build a REST client with Gson support.
 */
public final class GsonRestClient {
	/**
	 * Instantiates a REST client using a specified URL prefix.
	 * 
	 * @param urlPrefix the URL prefix
	 * @return the REST client
	 */
	public static RestClient to(String urlPrefix) {
		return new GsonRestClientBuilder().build(urlPrefix);
	}

	private GsonRestClient() {
	}
}
