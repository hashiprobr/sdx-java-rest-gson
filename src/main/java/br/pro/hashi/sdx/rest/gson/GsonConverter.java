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
 * Implemented to convert objects of a source type to and from objects of a
 * target type.
 * </p>
 * <p>
 * The idea is that the source type is not supported by Gson but the target type
 * is (possibly via other converters).
 * </p>
 * 
 * @param <S> the source type
 * @param <T> the target type
 */
public interface GsonConverter<S, T> extends Converter<S, T> {
	/**
	 * <p>
	 * Obtains a {@link JsonSerializer<S>} based on this converter.
	 * </p>
	 * <p>
	 * Classes are encouraged to provide an alternative implementation.
	 * </p>
	 * 
	 * @param targetType a {@link Type} representing {@code T}
	 * @return the Gson serializer
	 */
	default JsonSerializer<S> getGsonSerializer(Type targetType) {
		return new JsonSerializer<>() {
			@Override
			public JsonElement serialize(S src, Type typeOfSrc, JsonSerializationContext context) {
				return context.serialize(to(src), targetType);
			}
		};
	}

	/**
	 * <p>
	 * Obtains a {@link JsonDeserializer<S>} based on this converter.
	 * </p>
	 * <p>
	 * Classes are encouraged to provide an alternative implementation.
	 * </p>
	 * 
	 * @param targetType a {@link Type} representing {@code T}
	 * @return the Gson deserializer
	 */
	default JsonDeserializer<S> getGsonDeserializer(Type targetType) {
		return new JsonDeserializer<>() {
			@Override
			public S deserialize(JsonElement json, Type typeOfS, JsonDeserializationContext context) {
				return from(context.deserialize(json, targetType));
			}
		};
	}
}
