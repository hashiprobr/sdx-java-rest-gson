package br.pro.hashi.sdx.rest.gson.client;

import com.google.gson.GsonBuilder;

import br.pro.hashi.sdx.rest.client.RestClientBuilder;
import br.pro.hashi.sdx.rest.gson.GsonInjector;

/**
 * Stub.
 */
public class GsonRestClientBuilder extends RestClientBuilder {
	/**
	 * Stub.
	 */
	public GsonRestClientBuilder() {
		new GsonInjector().inject(this);
	}

	/**
	 * Stub.
	 * 
	 * @param packageName stub
	 */
	public GsonRestClientBuilder(String packageName) {
		new GsonInjector().inject(this, packageName);
	}

	/**
	 * Stub.
	 * 
	 * @param gsonBuilder stub
	 * @param packageName stub
	 */
	public GsonRestClientBuilder(GsonBuilder gsonBuilder, String packageName) {
		new GsonInjector().inject(this, gsonBuilder, packageName);
	}

	/**
	 * Stub.
	 * 
	 * @param gsonBuilder stub
	 */
	public GsonRestClientBuilder(GsonBuilder gsonBuilder) {
		new GsonInjector().inject(this, gsonBuilder);
	}
}
