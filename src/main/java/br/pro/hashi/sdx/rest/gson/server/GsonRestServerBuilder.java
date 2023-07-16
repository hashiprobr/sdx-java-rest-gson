package br.pro.hashi.sdx.rest.gson.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.pro.hashi.sdx.rest.gson.GsonConverter;
import br.pro.hashi.sdx.rest.gson.GsonInjector;
import br.pro.hashi.sdx.rest.server.RestServer;
import br.pro.hashi.sdx.rest.server.RestServerBuilder;

/**
 * Convenience class to configure and build objects of type {@link RestServer}
 * with Gson support.
 */
public class GsonRestServerBuilder extends RestServerBuilder {
	/**
	 * <p>
	 * Constructs a server builder with a default Gson serializer and a default Gson
	 * deserializer.
	 * </p>
	 * <p>
	 * This method instantiates a {@link Gson} with a default configuration. Namely,
	 * with the options below.
	 * </p>
	 * 
	 * <pre>
	 * {@code   .disableJdkUnsafe()
	 *   .disableHtmlEscaping()
	 *   .serializeNulls()
	 *   .serializeSpecialFloatingPointValues()
	 *   .setPrettyPrinting()}
	 * </pre>
	 */
	public GsonRestServerBuilder() {
		new GsonInjector().inject(this);
		addExtension();
	}

	/**
	 * <p>
	 * Constructs a server builder with an extended default Gson serializer and an
	 * extended default Gson deserializer.
	 * </p>
	 * <p>
	 * This method instantiates a {@link Gson} with a default configuration (see
	 * no-args constructor) and extends its type support with instances of all
	 * concrete implementations of {@link GsonConverter} in a specified package.
	 * </p>
	 * 
	 * @param packageName the package name
	 */
	public GsonRestServerBuilder(String packageName) {
		new GsonInjector().inject(this, packageName);
		addExtension();
	}

	/**
	 * <p>
	 * Constructs a server builder with an extended custom Gson serializer and an
	 * extended custom Gson deserializer.
	 * </p>
	 * <p>
	 * This method uses a specified {@link GsonBuilder} to instantiate a
	 * {@link Gson} and extends its type support with instances of all concrete
	 * implementations of {@link GsonConverter} in a specified package.
	 * </p>
	 * 
	 * @param gsonBuilder the Gson builder
	 * @param packageName the package name
	 */
	public GsonRestServerBuilder(GsonBuilder gsonBuilder, String packageName) {
		new GsonInjector().inject(this, gsonBuilder, packageName);
		addExtension();
	}

	/**
	 * <p>
	 * Constructs a server builder with a custom Gson serializer and a custom Gson
	 * deserializer.
	 * </p>
	 * <p>
	 * This method uses a specified {@link GsonBuilder} to instantiate a
	 * {@link Gson}.
	 * </p>
	 * 
	 * @param gsonBuilder the Gson builder
	 */
	public GsonRestServerBuilder(GsonBuilder gsonBuilder) {
		new GsonInjector().inject(this, gsonBuilder);
		addExtension();
	}

	private void addExtension() {
		this.withExtensionType("json", "application/json");
	}
}
