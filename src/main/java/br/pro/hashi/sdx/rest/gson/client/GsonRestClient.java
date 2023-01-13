package br.pro.hashi.sdx.rest.gson.client;

import br.pro.hashi.sdx.rest.client.RestClient;

/**
 * Stub.
 */
public final class GsonRestClient {
	/**
	 * Stub.
	 * 
	 * @param urlPrefix stub
	 * @return stub
	 */
	public static RestClient to(String urlPrefix) {
		return new GsonRestClientBuilder().build(urlPrefix);
	}

	private GsonRestClient() {
	}
}
