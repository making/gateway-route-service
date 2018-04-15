/*
 * Copyright 2013-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.springframework.cloud.gateway.filter.factory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.route.Route;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR;

public class RequestHeaderToRouteUrlGatewayFilterFactory
		extends AbstractGatewayFilterFactory<AbstractGatewayFilterFactory.NameConfig> {

	public RequestHeaderToRouteUrlGatewayFilterFactory() {
		super(NameConfig.class);
	}

	@Override
	public List<String> shortcutFieldOrder() {
		return Arrays.asList(NAME_KEY);
	}

	public GatewayFilter apply(NameConfig config) {
		return (exchange, chain) -> {
			String requestUrl = exchange.getRequest().getHeaders()
					.getFirst(config.getName());
			if (requestUrl != null) {
				Map<String, Object> attributes = exchange.getAttributes();
				Route route = (Route) attributes.get(GATEWAY_ROUTE_ATTR);
				Route newRoute = Route.builder() //
						.id(route.getId()) //
						.uri(requestUrl) //
						.order(route.getOrder()) //
						.predicate(route.getPredicate()) //
						.filters(route.getFilters()) //
						.build();
				attributes.put(GATEWAY_ROUTE_ATTR, newRoute);
			}
			return chain.filter(exchange);
		};
	}
}
