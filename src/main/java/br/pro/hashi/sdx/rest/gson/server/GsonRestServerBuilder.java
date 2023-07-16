package br.pro.hashi.sdx.rest.gson.server;

import com.google.gson.GsonBuilder;

import br.pro.hashi.sdx.rest.gson.GsonInjector;
import br.pro.hashi.sdx.rest.server.RestServerBuilder;

/**
 * Builds REST servers with Gson support.
 */
public class GsonRestServerBuilder extends RestServerBuilder {
	/**
	 * <p>
	 * Constructs a new builder with a default Gson serializer and a default Gson
	 * deserializer.
	 * </p>
	 * <p>
	 * See {@link GsonInjector#inject(br.pro.hashi.sdx.rest.Builder)}.
	 * </p>
	 */
	public GsonRestServerBuilder() {
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
	public GsonRestServerBuilder(GsonBuilder gsonBuilder) {
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
	public GsonRestServerBuilder(String packageName) {
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
	public GsonRestServerBuilder(GsonBuilder gsonBuilder, String packageName) {
		GsonInjector.getInstance().inject(this, gsonBuilder, packageName);
		configure();
	}

	private void configure() {
		withExtensionType("json", GsonInjector.JSON_TYPE);
		withFallbackType(GsonInjector.JSON_TYPE);
	}
}
