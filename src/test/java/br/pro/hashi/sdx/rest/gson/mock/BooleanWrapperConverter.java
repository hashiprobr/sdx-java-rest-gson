package br.pro.hashi.sdx.rest.gson.mock;

import br.pro.hashi.sdx.rest.gson.GsonConverter;

public class BooleanWrapperConverter implements GsonConverter<Wrapper<Boolean>, String> {
	@Override
	public String to(Wrapper<Boolean> source) {
		return Boolean.toString(source.getValue());
	}

	@Override
	public Wrapper<Boolean> from(String target) {
		return new Wrapper<>(Boolean.parseBoolean(target));
	}
}
