/*
 * Copyright (c) 2023 Marcelo Hashimoto
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

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
