package com.example.demogateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class LoggingGatewayFilterFactory extends AbstractGatewayFilterFactory {
	private final Logger log = LoggerFactory.getLogger(LoggingGatewayFilterFactory.class);

	@Override
	public GatewayFilter apply(Object config) {
		return (exchange, chain) -> {
			ServerHttpRequest request = exchange.getRequest();
			log.info("Start  path={},headers={},params={}", request.getPath().value(),
					request.getHeaders(), request.getQueryParams());
			return chain.filter(exchange) //
					.doOnSuccess(x -> log.info("End    path={},headers={},params={}",
							request.getPath().value(), request.getHeaders(),
							request.getQueryParams())) //
					.doOnError(e -> log.error("Failed path=" + request.getPath().value()
							+ ",headers=" + request.getHeaders() + ",params="
							+ request.getQueryParams(), e));
		};
	}
}
