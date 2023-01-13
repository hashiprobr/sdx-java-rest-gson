package br.pro.hashi.sdx.rest.gson;

import java.lang.reflect.Type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.pro.hashi.sdx.rest.Builder;
import br.pro.hashi.sdx.rest.gson.transform.GsonDeserializer;
import br.pro.hashi.sdx.rest.gson.transform.GsonSerializer;
import br.pro.hashi.sdx.rest.transform.extension.Injector;

public class GsonInjector extends Injector {
	private static final String JSON_TYPE = "application/json";

	private final Logger logger;

	public GsonInjector() {
		this.logger = LoggerFactory.getLogger(GsonInjector.class);
	}

	public void inject(Builder<?> builder) {
		inject(builder, newGsonBuilder());
	}

	public void inject(Builder<?> builder, String packageName) {
		inject(builder, newGsonBuilder(), packageName);
	}

	public void inject(Builder<?> builder, GsonBuilder gsonBuilder, String packageName) {
		for (GsonConverter<?, ?> converter : getSubConverters(packageName, GsonConverter.class)) {
			Type type = converter.getSourceType();
			gsonBuilder.registerTypeAdapter(type, converter.getSerializer());
			gsonBuilder.registerTypeAdapter(type, converter.getDeserializer());
			logger.info("Registered %s".formatted(converter.getClass().getName()));
		}
		inject(builder, gsonBuilder);
	}

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
