package br.pro.hashi.sdx.rest.gson.client;

import com.google.gson.GsonBuilder;

import br.pro.hashi.sdx.rest.client.RestClientBuilder;
import br.pro.hashi.sdx.rest.gson.GsonInjector;

public class GsonRestClientBuilder extends RestClientBuilder {
	public GsonRestClientBuilder() {
		new GsonInjector().inject(this);
	}

	public GsonRestClientBuilder(String packageName) {
		new GsonInjector().inject(this, packageName);
	}

	public GsonRestClientBuilder(GsonBuilder gsonBuilder, String packageName) {
		new GsonInjector().inject(this, gsonBuilder, packageName);
	}

	public GsonRestClientBuilder(GsonBuilder gsonBuilder) {
		new GsonInjector().inject(this, gsonBuilder);
	}
}
