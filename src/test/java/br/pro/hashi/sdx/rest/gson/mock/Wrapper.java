package br.pro.hashi.sdx.rest.gson.mock;

public class Wrapper<T> {
	T value;

	Wrapper() {
	}

	public Wrapper(T value) {
		this.value = value;
	}

	public T getValue() {
		return value;
	}
}
