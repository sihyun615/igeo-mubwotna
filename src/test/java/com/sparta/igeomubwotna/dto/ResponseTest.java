package com.sparta.igeomubwotna.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ResponseTest {

	@Test
	@DisplayName("Response 생성자 테스트")
	void testResponseConstructor() {
		// given
		int statusCode = 200;
		String message = "성공";

		// when
		Response response = new Response(statusCode, message);

		// then
		assertNotNull(response);
		assertEquals(statusCode, response.getStatusCode());
		assertEquals(message, response.getMessage());
	}
}