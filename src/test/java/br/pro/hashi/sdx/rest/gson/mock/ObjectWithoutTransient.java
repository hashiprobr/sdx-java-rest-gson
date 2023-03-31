package br.pro.hashi.sdx.rest.gson.mock;

public class ObjectWithoutTransient {
	private boolean field;

	public ObjectWithoutTransient() {
		this.field = true;
	}

	public void printField() {
		System.out.println(field);
	}
}
