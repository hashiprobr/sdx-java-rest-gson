package br.pro.hashi.sdx.rest.gson.mock;

import java.util.ArrayList;
import java.util.List;

import br.pro.hashi.sdx.rest.gson.GsonConverter;

public class ByteWrapperConverter implements GsonConverter<Wrapper<Byte>, List<Character>> {
	@Override
	public List<Character> to(Wrapper<Byte> source) {
		List<Character> target = new ArrayList<>();
		String value = Byte.toString(source.getValue());
		for (int i = 0; i < value.length(); i++) {
			target.add(value.charAt(i));
		}
		return target;
	}

	@Override
	public Wrapper<Byte> from(List<Character> target) {
		byte value = 0;
		for (char c : target) {
			value *= 10;
			value += Byte.parseByte(new String(new char[] { c }));
		}
		return new Wrapper<>(value);
	}
}
