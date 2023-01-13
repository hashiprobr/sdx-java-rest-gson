package br.pro.hashi.sdx.rest.gson.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.pro.hashi.sdx.rest.client.RestClient;
import br.pro.hashi.sdx.rest.client.RestClientBuilder;
import br.pro.hashi.sdx.rest.gson.GsonConverter;
import br.pro.hashi.sdx.rest.gson.GsonInjector;

/**
 * Convenience class to configure and build objects of type {@link RestClient}
 * with Gson support.
 */
public class GsonRestClientBuilder extends RestClientBuilder {
	/**
	 * <p>
	 * Constructs a client builder with a default Gson serializer and a default Gson
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
	 *   .setPrettyPrinting()}
	 * </pre>
	 */
	public GsonRestClientBuilder() {
		new GsonInjector().inject(this);
	}

	/**
	 * <p>
	 * Constructs a client builder with an extended default Gson serializer and an
	 * extended default Gson deserializer.
	 * </p>
	 * <p>
	 * This method instantiates a {@link Gson} with a default configuration (see
	 * {@code inject(Builder<?>)}) and extends its type support with instances of
	 * all concrete subclasses of {@link GsonConverter} in a specified package.
	 * </p>
	 * 
	 * @param packageName the package name
	 */
	public GsonRestClientBuilder(String packageName) {
		new GsonInjector().inject(this, packageName);
	}

	/**
	 * <p>
	 * Constructs a client builder with an extended custom Gson serializer and an
	 * extended custom Gson deserializer.
	 * </p>
	 * <p>
	 * This method uses a specified {@link GsonBuilder} to instantiate a
	 * {@link Gson} and extends its type support with instances of all concrete
	 * subclasses of {@link GsonConverter} in a specified package.
	 * </p>
	 * 
	 * @param gsonBuilder the Gson builder
	 * @param packageName the package name
	 */
	public GsonRestClientBuilder(GsonBuilder gsonBuilder, String packageName) {
		new GsonInjector().inject(this, gsonBuilder, packageName);
	}

	/**
	 * <p>
	 * Constructs a client builder with a custom Gson serializer and a custom Gson
	 * deserializer.
	 * </p>
	 * <p>
	 * This method uses a specified {@link GsonBuilder} to instantiate a
	 * {@link Gson}.
	 * </p>
	 * 
	 * @param gsonBuilder the Gson builder
	 */
	public GsonRestClientBuilder(GsonBuilder gsonBuilder) {
		new GsonInjector().inject(this, gsonBuilder);
	}
}
