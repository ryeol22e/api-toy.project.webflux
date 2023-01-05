package com.example.api.api.main.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1.0")
@RequiredArgsConstructor
public class MainController {
	
	@GetMapping("/test/data1")
	public Mono<String> data1() {
		try {
			Thread.sleep(4000L);
		} catch (Exception e) {
			// TODO: handle exception
		}

		return Mono.just("hello world data1");
	}

	@GetMapping("/test/data2")
	public Mono<String> data2() {
		return Mono.just("good bye data2");
	}
}
