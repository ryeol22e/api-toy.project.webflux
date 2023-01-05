package com.example.api.api.zconfig.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import com.example.api.api.zconfig.security.filter.JwtWebFilter;

import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public SecurityWebFilterChain webFilterChain(ServerHttpSecurity http, ReactiveAuthenticationManager authenticationManager) {
		http.csrf().disable().formLogin().disable().logout().disable().httpBasic().disable()
			.cors()
			.and()
				.authenticationManager(authenticationManager)
				.securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
				.authorizeExchange()
					.pathMatchers("/").permitAll()
					.anyExchange().permitAll()
			.and()
				.addFilterAt(new JwtWebFilter(), SecurityWebFiltersOrder.CORS);

		return http.build();
	}

	@Bean
	public CorsWebFilter corsWebFilter() {
		CorsConfiguration corsConfig = new CorsConfiguration();
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

		corsConfig.setAllowCredentials(true);
		corsConfig.setAllowedOriginPatterns(List.of("http://localhost:5555", "http://172.15.2.16:5555"));
		
		corsConfig.addAllowedHeader("*");
		corsConfig.addAllowedMethod("*");
		corsConfig.setMaxAge(8000L);

		source.registerCorsConfiguration("/**", corsConfig);

		return new CorsWebFilter(source);
	}

	@Bean
	public ReactiveAuthenticationManager reactiveAuthenticationManager(ReactiveUserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
		UserDetailsRepositoryReactiveAuthenticationManager authenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
		authenticationManager.setPasswordEncoder(passwordEncoder);

		return authenticationManager;
	}

	@Bean
	public ReactiveUserDetailsService reactiveUserDetailsService() {
		return username-> {
			SecurityUserDetails userDetails = new SecurityUserDetails();
			
			return Mono.just(userDetails);
		};
	}
}
