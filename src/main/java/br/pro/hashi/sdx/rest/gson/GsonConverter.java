package br.pro.hashi.sdx.rest.gson;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import br.pro.hashi.sdx.rest.transform.extension.Converter;

/**
 * <p>
 * A Gson converter can convert objects of a source type to and from objects of
 * a target type.
 * </p>
 * <p>
 * The idea is that the source type is not supported by Gson, but the target
 * type is (possibly via another converter).
 * </p>
 * 
 * @param <S> the source type
 * @param <T> the target type
 */
public abstract class GsonConverter<S, T> extends Converter<S, T> {
	private final JsonSerializer<S> serializer;
	private final JsonDeserializer<S> deserializer;

	/**
	 * Constructs a new Gson converter.
	 */
	protected GsonConverter() {
		this.serializer = new JsonSerializer<>() {
			@Override
			public JsonElement serialize(S src, Type typeOfSrc, JsonSerializationContext context) {
				return context.serialize(to(src), getTargetType());
			}
		};
		this.deserializer = new JsonDeserializer<>() {
			@Override
			public S deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
				return from(context.deserialize(json, getTargetType()));
			}
		};
	}

	/**
	 * Obtains a {@link JsonSerializer<T>} based on this converter.
	 * 
	 * @return the Gson serializer
	 */
	public final JsonSerializer<S> getSerializer() {
		return serializer;
	}

	/**
	 * Obtains a {@link JsonDeserializer<T>} based on this converter.
	 * 
	 * @return the Gson deserializer
	 */
	public final JsonDeserializer<S> getDeserializer() {
		return deserializer;
	}
}
