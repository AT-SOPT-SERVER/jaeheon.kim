package org.sopt.authentication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

public class CustomHttpServletRequestWrapper extends HttpServletRequestWrapper {
	private final Map<String, String> customHeaders;

	public CustomHttpServletRequestWrapper(HttpServletRequest request) {
		super(request);
		this.customHeaders = new HashMap<>();
	}

	@Override
	public String getHeader(String name) {
		if (customHeaders.containsKey(name)) {
			return customHeaders.get(name);
		}
		return super.getHeader(name);
	}

	public void addHeader(String key, String value) {
		customHeaders.put(key, value);
	}

	@Override
	public Enumeration<String> getHeaderNames() {
		Set<String> headerNames = customHeaders.keySet();
		Enumeration<String> originHeaderNames = super.getHeaderNames();

		while (originHeaderNames.hasMoreElements()) {
			headerNames.add(originHeaderNames.nextElement());
		}

		return Collections.enumeration(headerNames);
	}

	@Override
	public Enumeration<String> getHeaders(String name) {
		List<String> values = new ArrayList<>();
		if (customHeaders.containsKey(name)) {
			values.add(customHeaders.get(name));
		}
		Enumeration<String> originValues = super.getHeaders(name);
		while (originValues.hasMoreElements()) {
			values.add(originValues.nextElement());
		}

		return Collections.enumeration(values);
	}
}
