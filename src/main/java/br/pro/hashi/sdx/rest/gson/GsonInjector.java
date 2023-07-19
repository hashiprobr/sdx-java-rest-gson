package br.pro.hashi.sdx.rest.gson;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
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
 * Injects a Gson serializer and a Gson deserializer in a
 * {@link RestClientBuilder} or a {@link RestServerBuilder}.
 */
public class GsonInjector extends Injector {
	private static final GsonInjector INSTANCE = new GsonInjector();
	private static final Lookup LOOKUP = MethodHandles.lookup();

	/**
	 * Represents the JSON content type.
	 */
	public static final String JSON_TYPE = "application/json";

	/**
	 * Obtains an injector instance.
	 *
	 * @return the instance
	 */
	public static GsonInjector getInstance() {
		return INSTANCE;
	}

	private final Logger logger;

	private GsonInjector() {
		this.logger = LoggerFactory.getLogger(GsonInjector.class);
	}

	/**
	 * <p>
	 * Injects a default serializer and a default deserializer in the specified
	 * client or server builder.
	 * </p>
	 * <p>
	 * This method uses a {@link GsonBuilder} with a default configuration. Namely,
	 * with the options below.
	 * </p>
	 *
	 * <pre>
	 * {@code   .disableJdkUnsafe()
	 *   .disableHtmlEscaping()
	 *   .setPrettyPrinting()
	 *   .serializeNulls()
	 *   .serializeSpecialFloatingPointValues()}
	 * </pre>
	 *
	 * @param builder the client or server builder
	 * @throws NullPointerException if the client or server builder is null
	 */
	public final void inject(Builder<?> builder) {
		inject(builder, defaultGsonBuilder());
	}

	/**
	 * <p>
	 * Injects a custom serializer and a custom deserializer in the specified client
	 * or server builder.
	 * </p>
	 * <p>
	 * This method uses the specified {@link GsonBuilder}.
	 * </p>
	 *
	 * @param builder     the client or server builder
	 * @param gsonBuilder the Gson builder
	 * @throws NullPointerException if the client or server builder is null or the
	 *                              Gson builder is null
	 */
	public final void inject(Builder<?> builder, GsonBuilder gsonBuilder) {
		inject(builder, gsonBuilder.create());
	}

	/**
	 * <p>
	 * Injects an extended default serializer and an extended default deserializer
	 * in the specified client or server builder.
	 * </p>
	 * <p>
	 * This method uses a {@link GsonBuilder} with a default configuration (see
	 * {@code inject(Builder<?>)}) and extends its type support with instances of
	 * all concrete implementations of {@link GsonConverter} in the specified
	 * package (including subpackages).
	 * </p>
	 *
	 * @param builder     the client or server builder
	 * @param packageName the package name
	 * @throws NullPointerException if the client or server builder is null or the
	 *                              package name is null
	 */
	public final void inject(Builder<?> builder, String packageName) {
		inject(builder, defaultGsonBuilder(), packageName);
	}

	/**
	 * <p>
	 * Injects an extended custom serializer and an extended custom deserializer in
	 * the specified client or server builder.
	 * </p>
	 * <p>
	 * This method uses the specified {@link GsonBuilder} and extends its type
	 * support (see {@code inject(Builder<?>, String)}).
	 * </p>
	 *
	 * @param builder     the client or server builder
	 * @param gsonBuilder the Gson builder
	 * @param packageName the package name
	 * @throws NullPointerException if the client or server builder is null, the
	 *                              Gson builder is null, or the package name is
	 *                              null
	 */
	public final void inject(Builder<?> builder, GsonBuilder gsonBuilder, String packageName) {
		for (GsonConverter<?, ?> converter : getSubConverters(packageName, GsonConverter.class, LOOKUP)) {
			Type sourceType = converter.getSourceType();
			Type targetType = converter.getTargetType();
			gsonBuilder.registerTypeAdapter(sourceType, converter.getGsonSerializer(targetType));
			gsonBuilder.registerTypeAdapter(sourceType, converter.getGsonDeserializer(targetType));
			logger.info("Registered %s".formatted(converter.getClass().getName()));
		}
		inject(builder, gsonBuilder.create());
	}

	private void inject(Builder<?> builder, Gson gson) {
		if (builder == null) {
			throw new NullPointerException("Builder cannot be null");
		}
		builder.withSerializer(JSON_TYPE, new GsonSerializer(gson));
		builder.withDeserializer(JSON_TYPE, new GsonDeserializer(gson));
	}

	private GsonBuilder defaultGsonBuilder() {
		return new GsonBuilder()
				.disableJdkUnsafe()
				.disableHtmlEscaping()
				.setPrettyPrinting()
				.serializeNulls()
				.serializeSpecialFloatingPointValues();
	}
}
