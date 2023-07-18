package br.pro.hashi.sdx.rest.gson.mock;

public class ObjectWithoutTransient {
	private boolean value;

	ObjectWithoutTransient() {
	}

	public ObjectWithoutTransient(boolean value) {
		this.value = value;
	}

	public boolean isValue() {
		return value;
	}
}
