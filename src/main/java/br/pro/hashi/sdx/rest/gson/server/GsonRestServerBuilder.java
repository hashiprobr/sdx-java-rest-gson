package br.pro.hashi.sdx.rest.gson.server;

import com.google.gson.GsonBuilder;

import br.pro.hashi.sdx.rest.gson.GsonInjector;
import br.pro.hashi.sdx.rest.server.RestServerBuilder;

/**
 * Stub.
 */
public class GsonRestServerBuilder extends RestServerBuilder {
	/**
	 * Stub.
	 */
	public GsonRestServerBuilder() {
		new GsonInjector().inject(this);
	}

	/**
	 * Stub.
	 * 
	 * @param packageName stub
	 */
	public GsonRestServerBuilder(String packageName) {
		new GsonInjector().inject(this, packageName);
	}

	/**
	 * Stub.
	 * 
	 * @param gsonBuilder stub
	 * @param packageName stub
	 */
	public GsonRestServerBuilder(GsonBuilder gsonBuilder, String packageName) {
		new GsonInjector().inject(this, gsonBuilder, packageName);
	}

	/**
	 * Stub.
	 * 
	 * @param gsonBuilder stub
	 */
	public GsonRestServerBuilder(GsonBuilder gsonBuilder) {
		new GsonInjector().inject(this, gsonBuilder);
	}
}
