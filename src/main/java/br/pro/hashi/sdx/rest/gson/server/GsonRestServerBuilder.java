package br.pro.hashi.sdx.rest.gson.server;

import com.google.gson.GsonBuilder;

import br.pro.hashi.sdx.rest.gson.GsonInjector;
import br.pro.hashi.sdx.rest.server.RestServerBuilder;

public class GsonRestServerBuilder extends RestServerBuilder {
	public GsonRestServerBuilder() {
		new GsonInjector().inject(this);
	}

	public GsonRestServerBuilder(String packageName) {
		new GsonInjector().inject(this, packageName);
	}

	public GsonRestServerBuilder(GsonBuilder gsonBuilder, String packageName) {
		new GsonInjector().inject(this, gsonBuilder, packageName);
	}

	public GsonRestServerBuilder(GsonBuilder gsonBuilder) {
		new GsonInjector().inject(this, gsonBuilder);
	}
}
