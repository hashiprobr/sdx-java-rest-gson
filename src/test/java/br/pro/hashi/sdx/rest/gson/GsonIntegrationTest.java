package br.pro.hashi.sdx.rest.gson;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import br.pro.hashi.sdx.rest.Builder;
import br.pro.hashi.sdx.rest.Hint;
import br.pro.hashi.sdx.rest.client.RestClientBuilder;
import br.pro.hashi.sdx.rest.gson.mock.Address;
import br.pro.hashi.sdx.rest.gson.mock.Email;
import br.pro.hashi.sdx.rest.gson.mock.ObjectWithDouble;
import br.pro.hashi.sdx.rest.gson.mock.ObjectWithString;
import br.pro.hashi.sdx.rest.gson.mock.ObjectWithTransient;
import br.pro.hashi.sdx.rest.gson.mock.ObjectWithoutTransient;
import br.pro.hashi.sdx.rest.gson.mock.Sheet;
import br.pro.hashi.sdx.rest.gson.mock.User;
import br.pro.hashi.sdx.rest.gson.mock.Wrapper;
import br.pro.hashi.sdx.rest.server.RestServerBuilder;
import br.pro.hashi.sdx.rest.transform.Deserializer;
import br.pro.hashi.sdx.rest.transform.Serializer;

class GsonIntegrationTest {
	private Builder<?> builder;
	private Map<String, Serializer> serializers;
	private Map<String, Deserializer> deserializers;
	private GsonInjector injector;

	@ParameterizedTest
	@ValueSource(classes = {
			RestClientBuilder.class,
			RestServerBuilder.class })
	<T extends Builder<T>> void serializesUserWithoutConverters(Class<T> type) {
		setUp(type);
		injectWithoutConverters();
		assertSerializesUser("""
				{
				  "name": "Serializing Name",
				  "address": {
				    "street": "Serializing Street",
				    "number": 0,
				    "city": "Serializing City"
				  },
				  "email": {
				    "login": "serializing",
				    "domain": "email.com"
				  },
				  "active": true
				}
				""");
	}

	@ParameterizedTest
	@ValueSource(classes = {
			RestClientBuilder.class,
			RestServerBuilder.class })
	<T extends Builder<T>> void serializesUserWithConverters(Class<T> type) {
		setUp(type);
		injectWithConverters();
		assertSerializesUser("""
				{
				  "name": "Serializing Name",
				  "address": [
				    "Serializing City",
				    "0",
				    "Serializing Street"
				  ],
				  "email": "serializing@email.com",
				  "active": true
				}
				""");
	}

	private void assertSerializesUser(String content) {
		Email email = new Email();
		email.setLogin("serializing");
		email.setDomain("email.com");
		Address address = new Address("Serializing Street", 0, "Serializing City");
		User user = new User();
		user.setName("Serializing Name");
		user.setAddress(address);
		user.setEmail(email);
		user.setActive(true);
		assertSerializes(content, user, User.class);
	}

	@ParameterizedTest
	@ValueSource(classes = {
			RestClientBuilder.class,
			RestServerBuilder.class })
	<T extends Builder<T>> void deserializesUserWithoutConverters(Class<T> type) {
		setUp(type);
		injectWithoutConverters();
		assertDeserializesUser("""
				{
				  "name": "Deserializing Name",
				  "address": {
				    "street": "Deserializing Street",
				    "number": 1,
				    "city": "Deserializing City"
				  },
				  "email": {
				  	"login": "deserializing",
				  	"domain": "email.com"
				  },
				  "active": false
				}
				""");
	}

	@ParameterizedTest
	@ValueSource(classes = {
			RestClientBuilder.class,
			RestServerBuilder.class })
	<T extends Builder<T>> void deserializesUserWithConverters(Class<T> type) {
		setUp(type);
		injectWithConverters();
		assertDeserializesUser("""
				{
				  "name": "Deserializing Name",
				  "address": [
				    "Deserializing City",
				    "1",
				    "Deserializing Street"
				  ],
				  "email": "deserializing@email.com",
				  "active": false
				}
				""");
	}

	private void assertDeserializesUser(String content) {
		User user = deserialize(content, User.class);
		assertEquals("Deserializing Name", user.getName());
		Address address = user.getAddress();
		assertEquals("Deserializing Street", address.getStreet());
		assertEquals(1, address.getNumber());
		assertEquals("Deserializing City", address.getCity());
		Email email = user.getEmail();
		assertEquals("deserializing", email.getLogin());
		assertEquals("email.com", email.getDomain());
		assertFalse(user.isActive());
	}

	@ParameterizedTest
	@ValueSource(classes = {
			RestClientBuilder.class,
			RestServerBuilder.class })
	<T extends Builder<T>> void serializesSheetWithoutConverters(Class<T> type) {
		setUp(type);
		injectWithoutConverters();
		assertSerializesSheet("""
				{
				  "rows": [
				    [
				      "Street 0",
				      "0",
				      "City 0"
				    ],
				    [
				      "Street 1",
				      "1",
				      "City 1"
				    ]
				  ]
				}
				""");
	}

	@ParameterizedTest
	@ValueSource(classes = {
			RestClientBuilder.class,
			RestServerBuilder.class })
	<T extends Builder<T>> void serializesSheetWithConverters(Class<T> type) {
		setUp(type);
		injectWithConverters();
		assertSerializesSheet("""
				[
				  [
				    "City 0",
				    "0",
				    "Street 0"
				  ],
				  [
				    "City 1",
				    "1",
				    "Street 1"
				  ]
				]
				""");
	}

	private void assertSerializesSheet(String content) {
		Sheet sheet = new Sheet();
		sheet.addRow("Street 0", 0, "City 0");
		sheet.addRow("Street 1", 1, "City 1");
		assertSerializes(content, sheet, Sheet.class);
	}

	@ParameterizedTest
	@ValueSource(classes = {
			RestClientBuilder.class,
			RestServerBuilder.class })
	<T extends Builder<T>> void deserializesSheetWithoutConverters(Class<T> type) {
		setUp(type);
		injectWithoutConverters();
		assertDeserializesSheet("""
				{
				  "rows": [
				    [
				      "Street 1",
				      "1",
				      "City 1"
				    ],
				    [
				      "Street 0",
				      "0",
				      "City 0"
				    ]
				  ]
				}
				""");
	}

	@ParameterizedTest
	@ValueSource(classes = {
			RestClientBuilder.class,
			RestServerBuilder.class })
	<T extends Builder<T>> void deserializesSheetWithConverters(Class<T> type) {
		setUp(type);
		injectWithConverters();
		assertDeserializesSheet("""
				[
				  [
				    "City 1",
				    "1",
				    "Street 1"
				  ],
				  [
				    "City 0",
				    "0",
				    "Street 0"
				  ]
				]
				""");
	}

	private void assertDeserializesSheet(String content) {
		Sheet sheet = deserialize(content, Sheet.class);
		assertEquals(List.of("Street 1", "1", "City 1"), sheet.getRow(0));
		assertEquals(List.of("Street 0", "0", "City 0"), sheet.getRow(1));
		assertThrows(IndexOutOfBoundsException.class, () -> {
			sheet.getRow(2);
		});
	}

	@ParameterizedTest
	@ValueSource(classes = {
			RestClientBuilder.class,
			RestServerBuilder.class })
	<T extends Builder<T>> void serializesBooleanWrappersWithoutConverters(Class<T> type) {
		setUp(type);
		injectWithoutConverters();
		assertSerializesBooleanWrappers("""
				[
				  {
				    "value": false
				  },
				  {
				    "value": true
				  }
				]
				""", List.class);
	}

	@ParameterizedTest
	@ValueSource(classes = {
			RestClientBuilder.class,
			RestServerBuilder.class })
	<T extends Builder<T>> void serializesBooleanWrappersWithConverters(Class<T> type) {
		setUp(type);
		injectWithConverters();
		assertSerializesBooleanWrappers("""
				[
				  "false",
				  "true"
				]
				""", new Hint<List<Wrapper<Boolean>>>() {}.getType());
	}

	private void assertSerializesBooleanWrappers(String content, Type type) {
		List<Wrapper<Boolean>> wrappers = new ArrayList<>();
		wrappers.add(new Wrapper<>(false));
		wrappers.add(new Wrapper<>(true));
		assertSerializes(content, wrappers, type);
	}

	@ParameterizedTest
	@ValueSource(classes = {
			RestClientBuilder.class,
			RestServerBuilder.class })
	<T extends Builder<T>> void deserializesBooleanWrappersWithoutConverters(Class<T> type) {
		setUp(type);
		injectWithoutConverters();
		assertDeserializesBooleanWrappers("""
				[
				  {
				    "value": true
				  },
				  {
				    "value": false
				  }
				]
				""", new Hint<List<Wrapper<Boolean>>>() {}.getType());
	}

	@ParameterizedTest
	@ValueSource(classes = {
			RestClientBuilder.class,
			RestServerBuilder.class })
	<T extends Builder<T>> void deserializesBooleanWrappersWithConverters(Class<T> type) {
		setUp(type);
		injectWithConverters();
		assertDeserializesBooleanWrappers("""
				[
				  "true",
				  "false"
				]
				""", new Hint<List<Wrapper<Boolean>>>() {}.getType());
	}

	private void assertDeserializesBooleanWrappers(String content, Type type) {
		List<Wrapper<Boolean>> wrappers = deserialize(content, type);
		assertEquals(2, wrappers.size());
		assertTrue(wrappers.get(0).getValue());
		assertFalse(wrappers.get(1).getValue());
	}

	@ParameterizedTest
	@ValueSource(classes = {
			RestClientBuilder.class,
			RestServerBuilder.class })
	<T extends Builder<T>> void serializesByteWrappersWithoutConverters(Class<T> type) {
		setUp(type);
		injectWithoutConverters();
		assertSerializesByteWrappers("""
				[
				  {
				    "value": 63
				  },
				  {
				    "value": 127
				  }
				]
				""", List.class);
	}

	@ParameterizedTest
	@ValueSource(classes = {
			RestClientBuilder.class,
			RestServerBuilder.class })
	<T extends Builder<T>> void serializesByteWrappersWithConverters(Class<T> type) {
		setUp(type);
		injectWithConverters();
		assertSerializesByteWrappers("""
				[
				  [
				    "6",
				    "3"
				  ],
				  [
				    "1",
				    "2",
				    "7"
				  ]
				]
				""", new Hint<List<Wrapper<Byte>>>() {}.getType());
	}

	private void assertSerializesByteWrappers(String content, Type type) {
		List<Wrapper<Byte>> wrappers = new ArrayList<>();
		wrappers.add(new Wrapper<>((byte) 63));
		wrappers.add(new Wrapper<>((byte) 127));
		assertSerializes(content, wrappers, type);
	}

	@ParameterizedTest
	@ValueSource(classes = {
			RestClientBuilder.class,
			RestServerBuilder.class })
	<T extends Builder<T>> void deserializesByteWrappersWithoutConverters(Class<T> type) {
		setUp(type);
		injectWithoutConverters();
		assertDeserializesByteWrappers("""
				[
				  {
				    "value": 127
				  },
				  {
				    "value": 63
				  }
				]
				""", new Hint<List<Wrapper<Byte>>>() {}.getType());
	}

	@ParameterizedTest
	@ValueSource(classes = {
			RestClientBuilder.class,
			RestServerBuilder.class })
	<T extends Builder<T>> void deserializesByteWrappersWithConverters(Class<T> type) {
		setUp(type);
		injectWithConverters();
		assertDeserializesByteWrappers("""
				[
				  [
				    "1",
				    "2",
				    "7"
				  ],
				  [
				    "6",
				    "3"
				  ]
				]
				""", new Hint<List<Wrapper<Byte>>>() {}.getType());
	}

	private void assertDeserializesByteWrappers(String content, Type type) {
		List<Wrapper<Byte>> wrappers = deserialize(content, type);
		assertEquals(2, wrappers.size());
		assertEquals(127, (byte) wrappers.get(0).getValue());
		assertEquals(63, (byte) wrappers.get(1).getValue());
	}

	@ParameterizedTest
	@ValueSource(classes = {
			RestClientBuilder.class,
			RestServerBuilder.class })
	public <T extends Builder<T>> void serializesWithoutTransient(Class<T> type) {
		setUp(type);
		injectWithoutConverters();
		ObjectWithoutTransient object = new ObjectWithoutTransient(true);
		assertSerializes("""
				{
				  "value": true
				}""", object, ObjectWithoutTransient.class);
	}

	@ParameterizedTest
	@ValueSource(classes = {
			RestClientBuilder.class,
			RestServerBuilder.class })
	public <T extends Builder<T>> void deserializesWithoutTransient(Class<T> type) {
		setUp(type);
		injectWithoutConverters();
		ObjectWithoutTransient object = deserialize("""
				{
				  "value": true
				}""", ObjectWithoutTransient.class);
		assertTrue(object.isValue());
	}

	@ParameterizedTest
	@ValueSource(classes = {
			RestClientBuilder.class,
			RestServerBuilder.class })
	public <T extends Builder<T>> void serializesWithTransient(Class<T> type) {
		setUp(type);
		injectWithoutConverters();
		ObjectWithTransient object = new ObjectWithTransient(true);
		assertSerializes("""
				{}""", object, ObjectWithTransient.class);
	}

	@ParameterizedTest
	@ValueSource(classes = {
			RestClientBuilder.class,
			RestServerBuilder.class })
	public <T extends Builder<T>> void deserializesWithTransient(Class<T> type) {
		setUp(type);
		injectWithoutConverters();
		ObjectWithTransient object = deserialize("""
				{
				  "value": true
				}""", ObjectWithTransient.class);
		assertFalse(object.isValue());
	}

	@ParameterizedTest
	@ValueSource(classes = {
			RestClientBuilder.class,
			RestServerBuilder.class })
	public <T extends Builder<T>> void serializesWithNaN(Class<T> type) {
		setUp(type);
		injectWithoutConverters();
		ObjectWithDouble object = new ObjectWithDouble(Double.NaN);
		assertSerializes("""
				{
				  "value": NaN
				}""", object, ObjectWithDouble.class);
	}

	@ParameterizedTest
	@ValueSource(classes = {
			RestClientBuilder.class,
			RestServerBuilder.class })
	public <T extends Builder<T>> void deserializesWithNaN(Class<T> type) {
		setUp(type);
		injectWithoutConverters();
		ObjectWithDouble object = deserialize("""
				{
				  "value": NaN
				}""", ObjectWithDouble.class);
		assertTrue(Double.isNaN(object.getValue()));
	}

	@ParameterizedTest
	@ValueSource(classes = {
			RestClientBuilder.class,
			RestServerBuilder.class })
	public <T extends Builder<T>> void serializesWithNegativeInfinity(Class<T> type) {
		setUp(type);
		injectWithoutConverters();
		ObjectWithDouble object = new ObjectWithDouble(Double.NEGATIVE_INFINITY);
		assertSerializes("""
				{
				  "value": -Infinity
				}""", object, ObjectWithDouble.class);
	}

	@ParameterizedTest
	@ValueSource(classes = {
			RestClientBuilder.class,
			RestServerBuilder.class })
	public <T extends Builder<T>> void deserializesWithNegativeInfinity(Class<T> type) {
		setUp(type);
		injectWithoutConverters();
		ObjectWithDouble object = deserialize("""
				{
				  "value": -Infinity
				}""", ObjectWithDouble.class);
		double value = object.getValue();
		assertTrue(value < 0);
		assertTrue(Double.isInfinite(value));
	}

	@ParameterizedTest
	@ValueSource(classes = {
			RestClientBuilder.class,
			RestServerBuilder.class })
	public <T extends Builder<T>> void serializesWithPositiveInfinity(Class<T> type) {
		setUp(type);
		injectWithoutConverters();
		ObjectWithDouble object = new ObjectWithDouble(Double.POSITIVE_INFINITY);
		assertSerializes("""
				{
				  "value": Infinity
				}""", object, ObjectWithDouble.class);
	}

	@ParameterizedTest
	@ValueSource(classes = {
			RestClientBuilder.class,
			RestServerBuilder.class })
	public <T extends Builder<T>> void deserializesWithPositiveInfinity(Class<T> type) {
		setUp(type);
		injectWithoutConverters();
		ObjectWithDouble object = deserialize("""
				{
				  "value": Infinity
				}""", ObjectWithDouble.class);
		double value = object.getValue();
		assertTrue(value > 0);
		assertTrue(Double.isInfinite(value));
	}

	@ParameterizedTest
	@ValueSource(classes = {
			RestClientBuilder.class,
			RestServerBuilder.class })
	public <T extends Builder<T>> void serializesWithHtml(Class<T> type) {
		setUp(type);
		injectWithoutConverters();
		ObjectWithString object = new ObjectWithString("<div></div>");
		assertSerializes("""
				{
				  "value": "<div></div>"
				}""", object, ObjectWithString.class);
	}

	@ParameterizedTest
	@ValueSource(classes = {
			RestClientBuilder.class,
			RestServerBuilder.class })
	public <T extends Builder<T>> void deserializesWithHtml(Class<T> type) {
		setUp(type);
		injectWithoutConverters();
		ObjectWithString object = deserialize("""
				{
				  "value": "<div></div>"
				}""", ObjectWithString.class);
		assertEquals("<div></div>", object.getValue());
	}

	@ParameterizedTest
	@ValueSource(classes = {
			RestClientBuilder.class,
			RestServerBuilder.class })
	public <T extends Builder<T>> void serializesWithNull(Class<T> type) {
		setUp(type);
		injectWithoutConverters();
		ObjectWithString object = new ObjectWithString(null);
		assertSerializes("""
				{
				  "value": null
				}""", object, ObjectWithString.class);
	}

	@ParameterizedTest
	@ValueSource(classes = {
			RestClientBuilder.class,
			RestServerBuilder.class })
	public <T extends Builder<T>> void deserializesWithNull(Class<T> type) {
		setUp(type);
		injectWithoutConverters();
		ObjectWithString object = deserialize("""
				{
				  "value": null
				}""", ObjectWithString.class);
		assertNull(object.getValue());
	}

	@ParameterizedTest
	@ValueSource(classes = {
			RestClientBuilder.class,
			RestServerBuilder.class })
	public <T extends Builder<T>> void serializesElement(Class<T> type) {
		setUp(type);
		injectWithoutConverters();
		JsonArray jsonArray = new JsonArray();
		jsonArray.add("s");
		jsonArray.add(6.6);
		jsonArray.add(3);
		jsonArray.add(false);
		jsonArray.add(true);
		jsonArray.add(JsonNull.INSTANCE);
		JsonObject jsonObject = new JsonObject();
		jsonObject.add("value", jsonArray);
		assertSerializes("""
				{
				  "value": [
				    "s",
				    6.6,
				    3,
				    false,
				    true,
				    null
				  ]
				}""", jsonObject, JsonElement.class);
	}

	@ParameterizedTest
	@ValueSource(classes = {
			RestClientBuilder.class,
			RestServerBuilder.class })
	public <T extends Builder<T>> void serializesJsonReader(Class<T> type) {
		setUp(type);
		injectWithoutConverters();
		JsonReader jsonReader = new JsonReader(new StringReader("""
				{
				  "value": [
				    "s",
				    6.6,
				    3,
				    false,
				    true,
				    null
				  ]
				}"""));
		assertSerializes("""
				{
				  "value": [
				    "s",
				    6.6,
				    3,
				    false,
				    true,
				    null
				  ]
				}""", jsonReader, JsonReader.class);
	}

	@ParameterizedTest
	@ValueSource(classes = {
			RestClientBuilder.class,
			RestServerBuilder.class })
	public <T extends Builder<T>> void serializesConsumer(Class<T> type) {
		setUp(type);
		injectWithoutConverters();
		Consumer<JsonWriter> consumer = (jsonWriter) -> {
			assertDoesNotThrow(() -> {
				jsonWriter.beginObject();
				jsonWriter.name("value");
				jsonWriter.beginArray();
				jsonWriter.value("s");
				jsonWriter.value(6.6);
				jsonWriter.value(3);
				jsonWriter.value(false);
				jsonWriter.value(true);
				jsonWriter.nullValue();
				jsonWriter.endArray();
				jsonWriter.endObject();
				jsonWriter.flush();
			});
		};
		assertSerializes("""
				{
				  "value": [
				    "s",
				    6.6,
				    3,
				    false,
				    true,
				    null
				  ]
				}""", consumer, new Hint<Consumer<JsonWriter>>() {}.getType());
	}

	@ParameterizedTest
	@ValueSource(classes = {
			RestClientBuilder.class,
			RestServerBuilder.class })
	public <T extends Builder<T>> void deserializesElement(Class<T> type) {
		setUp(type);
		injectWithoutConverters();
		JsonElement element = deserialize("""
				{
				  "value": [
				    "s",
				    6.6,
				    3,
				    false,
				    true,
				    null
				  ]
				}""", JsonElement.class);
		JsonObject jsonObject = element.getAsJsonObject();
		assertEquals(Set.of("value"), jsonObject.keySet());
		JsonArray jsonArray = jsonObject.get("value").getAsJsonArray();
		assertEquals(6, jsonArray.size());
		assertEquals("s", jsonArray.get(0).getAsString());
		assertEquals(6.6, jsonArray.get(1).getAsDouble());
		assertEquals(3, jsonArray.get(2).getAsInt());
		assertFalse(jsonArray.get(3).getAsBoolean());
		assertTrue(jsonArray.get(4).getAsBoolean());
		assertTrue(jsonArray.get(5).isJsonNull());
	}

	@ParameterizedTest
	@ValueSource(classes = {
			RestClientBuilder.class,
			RestServerBuilder.class })
	public <T extends Builder<T>> void deserializesJsonReader(Class<T> type) {
		setUp(type);
		injectWithoutConverters();
		JsonReader jsonReader = deserialize("""
				{
				  "value": [
				    "s",
				    6.6,
				    3,
				    false,
				    true,
				    null
				  ]
				}""", JsonReader.class);
		assertDoesNotThrow(() -> {
			jsonReader.beginObject();
			assertEquals("value", jsonReader.nextName());
			jsonReader.beginArray();
			assertEquals("s", jsonReader.nextString());
			assertEquals(6.6, jsonReader.nextDouble());
			assertEquals(3, jsonReader.nextInt());
			assertFalse(jsonReader.nextBoolean());
			assertTrue(jsonReader.nextBoolean());
			jsonReader.nextNull();
			jsonReader.endArray();
			jsonReader.endObject();
			jsonReader.close();
		});
	}

	private <T extends Builder<T>> void setUp(Class<T> type) {
		builder = mock(type);
		serializers = new HashMap<>();
		deserializers = new HashMap<>();
		when(builder.withSerializer(any(String.class), any(Serializer.class))).thenAnswer((invocation) -> {
			String contentType = invocation.getArgument(0);
			Serializer serializer = invocation.getArgument(1);
			serializers.put(contentType, serializer);
			return null;
		});
		when(builder.withDeserializer(any(String.class), any(Deserializer.class))).thenAnswer((invocation) -> {
			String contentType = invocation.getArgument(0);
			Deserializer deserializer = invocation.getArgument(1);
			deserializers.put(contentType, deserializer);
			return null;
		});
		injector = GsonInjector.getInstance();
	}

	private void injectWithoutConverters() {
		injector.inject(builder);
	}

	private void injectWithConverters() {
		injector.inject(builder, "br.pro.hashi.sdx.rest.gson.mock");
	}

	private void assertSerializes(String content, Object object, Type type) {
		Serializer serializer = serializers.get(GsonInjector.JSON_TYPE);
		StringWriter writer = new StringWriter();
		serializer.write(object, type, writer);
		assertDoesNotThrow(() -> {
			writer.close();
		});
		assertEquals(content.strip(), writer.toString());
	}

	private <T> T deserialize(String content, Type type) {
		Deserializer deserializer = deserializers.get(GsonInjector.JSON_TYPE);
		Reader reader = new StringReader(content);
		return deserializer.read(reader, type);
	}

	@ParameterizedTest
	@ValueSource(classes = {
			RestClientBuilder.class,
			RestServerBuilder.class })
	void doesNotInjectWithNullBuilder() {
		injector = GsonInjector.getInstance();
		assertThrows(NullPointerException.class, () -> {
			injector.inject(null);
		});
	}
}
