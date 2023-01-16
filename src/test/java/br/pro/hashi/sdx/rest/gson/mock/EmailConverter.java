package br.pro.hashi.sdx.rest.gson.mock;

import br.pro.hashi.sdx.rest.gson.GsonConverter;

public class EmailConverter implements GsonConverter<Email, String> {
	@Override
	public String to(Email source) {
		return "%s@%s".formatted(source.getLogin(), source.getDomain());
	}

	@Override
	public Email from(String target) {
		String[] items = target.split("@");
		Email email = new Email();
		email.setLogin(items[0]);
		email.setDomain(items[1]);
		return email;
	}
}
