package br.pro.hashi.sdx.rest.gson.mock;

public class ObjectWithTransient {
	private transient boolean value;

	ObjectWithTransient() {
	}

	public ObjectWithTransient(boolean value) {
		this.value = value;
	}

	public boolean isValue() {
		return value;
	}
}
