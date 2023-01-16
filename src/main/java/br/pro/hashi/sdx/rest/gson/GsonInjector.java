package br.pro.hashi.sdx.rest.gson;

import java.lang.reflect.Type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.pro.hashi.sdx.rest.Builder;
import br.pro.hashi.sdx.rest.client.RestClientBuilder;
import br.pro.hashi.sdx.rest.gson.transform.GsonDeserializer;
import br.pro.hashi.sdx.rest.gson.transform.GsonSerializer;
import br.pro.hashi.sdx.rest.server.RestServerBuilder;
import br.pro.hashi.sdx.rest.transform.extension.Injector;

/**
 * A Gson injector can inject a Gson-based serializer and a Gson-based
 * deserializer in an object of type {@link RestClientBuilder} or an object of
 * type {@link RestServerBuilder}.
 */
public class GsonInjector extends Injector {
	private static final String JSON_TYPE = "application/json";

	private final Logger logger;

	/**
	 * Constructs a new Gson injector.
	 */
	public GsonInjector() {
		this.logger = LoggerFactory.getLogger(GsonInjector.class);
	}

	/**
	 * <p>
	 * Injects a default serializer and a default deserializer in a client or server
	 * builder.
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
	 * 
	 * @param builder the client or server builder
	 */
	public void inject(Builder<?> builder) {
		inject(builder, newGsonBuilder());
	}

	/**
	 * <p>
	 * Injects an extended default serializer and an extended default deserializer
	 * in a client or server builder.
	 * </p>
	 * <p>
	 * This method instantiates a {@link Gson} with a default configuration (see
	 * {@code inject(Builder<?>)}) and extends its type support with instances of
	 * all concrete implementations of {@link GsonConverter} in a specified package.
	 * </p>
	 * 
	 * @param builder     the client or server builder
	 * @param packageName the package name
	 */
	public void inject(Builder<?> builder, String packageName) {
		inject(builder, newGsonBuilder(), packageName);
	}

	/**
	 * <p>
	 * Injects an extended custom serializer and an extended custom deserializer in
	 * a client or server builder.
	 * </p>
	 * <p>
	 * This method uses a specified {@link GsonBuilder} to instantiate a
	 * {@link Gson} and extends its type support with instances of all concrete
	 * implementations of {@link GsonConverter} in a specified package.
	 * </p>
	 * 
	 * @param builder     the client or server builder
	 * @param gsonBuilder the Gson builder
	 * @param packageName the package name
	 */
	public void inject(Builder<?> builder, GsonBuilder gsonBuilder, String packageName) {
		for (GsonConverter<?, ?> converter : getSubConverters(packageName, GsonConverter.class)) {
			Type type = converter.getSourceType();
			gsonBuilder.registerTypeAdapter(type, converter.getGsonSerializer());
			gsonBuilder.registerTypeAdapter(type, converter.getGsonDeserializer());
			logger.info("Registered %s".formatted(converter.getClass().getName()));
		}
		inject(builder, gsonBuilder);
	}

	/**
	 * <p>
	 * Injects a custom serializer and a custom deserializer in a client or server
	 * builder.
	 * </p>
	 * <p>
	 * This method uses a specified {@link GsonBuilder} to instantiate a
	 * {@link Gson}.
	 * </p>
	 * 
	 * @param builder     the client or server builder
	 * @param gsonBuilder the Gson builder
	 */
	public void inject(Builder<?> builder, GsonBuilder gsonBuilder) {
		Gson gson = gsonBuilder.create();
		builder.withSerializer(JSON_TYPE, new GsonSerializer(gson));
		builder.withDeserializer(JSON_TYPE, new GsonDeserializer(gson));
	}

	private GsonBuilder newGsonBuilder() {
		return new GsonBuilder()
				.disableJdkUnsafe()
				.disableHtmlEscaping()
				.serializeNulls()
				.setPrettyPrinting();
	}
}
