package br.pro.hashi.sdx.rest.gson.client;

import br.pro.hashi.sdx.rest.client.RestClient;

public final class GsonRestClient {
	public static RestClient to(String urlPrefix) {
		return new GsonRestClientBuilder().build(urlPrefix);
	}

	private GsonRestClient() {
	}
}
