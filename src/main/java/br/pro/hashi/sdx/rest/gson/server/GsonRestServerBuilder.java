package br.pro.hashi.sdx.rest.gson.server;

import com.google.gson.Gson;

import br.pro.hashi.sdx.rest.gson.GsonInjector;
import br.pro.hashi.sdx.rest.server.RestServerBuilder;

public class GsonRestServerBuilder extends RestServerBuilder {
	public GsonRestServerBuilder() {
		new GsonInjector().inject(this);
	}

	public GsonRestServerBuilder(String packageName) {
		new GsonInjector().inject(this, packageName);
	}

	public GsonRestServerBuilder(Gson gson) {
		new GsonInjector().inject(this, gson);
	}
}
