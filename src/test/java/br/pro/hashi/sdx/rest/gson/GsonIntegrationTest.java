package br.pro.hashi.sdx.rest.gson;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import br.pro.hashi.sdx.rest.Builder;
import br.pro.hashi.sdx.rest.client.RestClientBuilder;
import br.pro.hashi.sdx.rest.gson.mock.Address;
import br.pro.hashi.sdx.rest.gson.mock.Email;
import br.pro.hashi.sdx.rest.gson.mock.User;
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
	<T extends Builder<T>> void serializesWithConverter(Class<T> type) {
		mockBuilderAndConstructInjector(type);
		injectWithConverter();
		assertReads("""
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
				""", getSerializer());
	}

	@ParameterizedTest
	@ValueSource(classes = {
			RestClientBuilder.class,
			RestServerBuilder.class })
	<T extends Builder<T>> void deserializesWithConverter(Class<T> type) {
		mockBuilderAndConstructInjector(type);
		injectWithConverter();
		Deserializer deserializer = getDeserializer();
		assertWrites(deserializer, """
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

	@ParameterizedTest
	@ValueSource(classes = {
			RestClientBuilder.class,
			RestServerBuilder.class })
	<T extends Builder<T>> void serializesWithoutConverter(Class<T> type) {
		mockBuilderAndConstructInjector(type);
		injectWithoutConverter();
		assertReads("""
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
				""", getSerializer());
	}

	@ParameterizedTest
	@ValueSource(classes = {
			RestClientBuilder.class,
			RestServerBuilder.class })
	<T extends Builder<T>> void deserializesWithoutConverter(Class<T> type) {
		mockBuilderAndConstructInjector(type);
		injectWithoutConverter();
		assertWrites(getDeserializer(), """
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

	private <T extends Builder<T>> void mockBuilderAndConstructInjector(Class<T> type) {
		serializers = new HashMap<>();
		deserializers = new HashMap<>();
		builder = mock(type);
		when(builder.withSerializer(any(String.class), any(Serializer.class))).thenAnswer((invocation) -> {
			serializers.put(invocation.getArgument(0), invocation.getArgument(1));
			return null;
		});
		when(builder.withDeserializer(any(String.class), any(Deserializer.class))).thenAnswer((invocation) -> {
			deserializers.put(invocation.getArgument(0), invocation.getArgument(1));
			return null;
		});
		injector = new GsonInjector();
	}

	private void injectWithConverter() {
		injector.inject(builder, "br.pro.hashi.sdx.rest.gson.mock");
	}

	private void injectWithoutConverter() {
		injector.inject(builder);
	}

	private Serializer getSerializer() {
		return serializers.get("application/json");
	}

	private Deserializer getDeserializer() {
		return deserializers.get("application/json");
	}

	private void assertReads(String expected, Serializer serializer) {
		Address address = new Address("Serializing Street", 0, "Serializing City");
		Email email = new Email();
		email.setLogin("serializing");
		email.setDomain("email.com");
		User user = new User();
		user.setName("Serializing Name");
		user.setAddress(address);
		user.setEmail(email);
		user.setActive(true);
		Reader reader = serializer.toReader(user);
		expected = expected.strip();
		char[] chars = new char[expected.length()];
		int b;
		try {
			reader.read(chars);
			b = reader.read();
			reader.close();
		} catch (IOException exception) {
			throw new AssertionError(exception);
		}
		assertEquals(-1, b);
		assertEquals(expected, new String(chars));
	}

	private void assertWrites(Deserializer deserializer, String content) {
		Reader reader = new StringReader(content);
		User user = deserializer.fromReader(reader, User.class);
		Address address = user.getAddress();
		Email email = user.getEmail();
		assertEquals("Deserializing Name", user.getName());
		assertEquals("Deserializing Street", address.getStreet());
		assertEquals(1, address.getNumber());
		assertEquals("Deserializing City", address.getCity());
		assertEquals("deserializing", email.getLogin());
		assertEquals("email.com", email.getDomain());
		assertFalse(user.isActive());
	}
}
