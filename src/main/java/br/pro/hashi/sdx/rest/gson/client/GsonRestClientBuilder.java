package br.pro.hashi.sdx.rest.gson.client;

import com.google.gson.Gson;

import br.pro.hashi.sdx.rest.client.RestClientBuilder;
import br.pro.hashi.sdx.rest.gson.GsonInjector;

public class GsonRestClientBuilder extends RestClientBuilder {
	public GsonRestClientBuilder() {
		new GsonInjector().inject(this);
	}

	public GsonRestClientBuilder(String packageName) {
		new GsonInjector().inject(this, packageName);
	}

	public GsonRestClientBuilder(Gson gson) {
		new GsonInjector().inject(this, gson);
	}
}
