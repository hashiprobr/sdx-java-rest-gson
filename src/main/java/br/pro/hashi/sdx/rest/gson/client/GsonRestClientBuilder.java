package br.pro.hashi.sdx.rest.gson.client;

import com.google.gson.GsonBuilder;

import br.pro.hashi.sdx.rest.client.RestClientBuilder;
import br.pro.hashi.sdx.rest.gson.GsonInjector;

/**
 * Builds REST clients with Gson support.
 */
public class GsonRestClientBuilder extends RestClientBuilder {
	/**
	 * <p>
	 * Constructs a new builder with a default Gson serializer and a default Gson
	 * deserializer.
	 * </p>
	 * <p>
	 * See {@link GsonInjector#inject(br.pro.hashi.sdx.rest.Builder)}.
	 * </p>
	 */
	public GsonRestClientBuilder() {
		GsonInjector.getInstance().inject(this);
		configure();
	}

	/**
	 * <p>
	 * Constructs a new builder with a custom Gson serializer and a custom Gson
	 * deserializer.
	 * </p>
	 * <p>
	 * See {@link GsonInjector#inject(br.pro.hashi.sdx.rest.Builder, GsonBuilder)}.
	 * </p>
	 * 
	 * @param gsonBuilder the Gson builder
	 */
	public GsonRestClientBuilder(GsonBuilder gsonBuilder) {
		GsonInjector.getInstance().inject(this, gsonBuilder);
		configure();
	}

	/**
	 * <p>
	 * Constructs a new builder with an extended default Gson serializer and an
	 * extended default Gson deserializer.
	 * </p>
	 * <p>
	 * See {@link GsonInjector#inject(br.pro.hashi.sdx.rest.Builder, String)}.
	 * </p>
	 * 
	 * @param packageName the package name
	 */
	public GsonRestClientBuilder(String packageName) {
		GsonInjector.getInstance().inject(this, packageName);
		configure();
	}

	/**
	 * <p>
	 * Constructs a new builder with an extended custom Gson serializer and an
	 * extended custom Gson deserializer.
	 * </p>
	 * <p>
	 * See
	 * {@link GsonInjector#inject(br.pro.hashi.sdx.rest.Builder, GsonBuilder, String)}.
	 * </p>
	 * 
	 * @param gsonBuilder the Gson builder
	 * @param packageName the package name
	 */
	public GsonRestClientBuilder(GsonBuilder gsonBuilder, String packageName) {
		GsonInjector.getInstance().inject(this, gsonBuilder, packageName);
		configure();
	}

	private void configure() {
		withFallbackType(GsonInjector.JSON_TYPE);
	}
}
