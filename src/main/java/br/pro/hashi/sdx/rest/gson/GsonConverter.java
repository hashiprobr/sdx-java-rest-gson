package br.pro.hashi.sdx.rest.gson;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import br.pro.hashi.sdx.rest.transform.extension.Converter;

/**
 * Stub.
 * 
 * @param <S> stub
 * @param <T> stub
 */
public abstract class GsonConverter<S, T> extends Converter<S, T> {
	private final JsonSerializer<S> serializer;
	private final JsonDeserializer<S> deserializer;

	/**
	 * Stub.
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
	 * Stub.
	 * 
	 * @return stub
	 */
	public final JsonSerializer<S> getSerializer() {
		return serializer;
	}

	/**
	 * Stub.
	 * 
	 * @return stub
	 */
	public final JsonDeserializer<S> getDeserializer() {
		return deserializer;
	}
}
