/**
 * Defines a Gson extension for sdx-rest.
 */
module br.pro.hashi.sdx.rest.gson {
	requires transitive br.pro.hashi.sdx.rest;
	requires transitive com.google.gson;

	exports br.pro.hashi.sdx.rest.gson;
	exports br.pro.hashi.sdx.rest.gson.client;
	exports br.pro.hashi.sdx.rest.gson.server;
}