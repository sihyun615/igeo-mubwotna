package com.sparta.igeomubwotna.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CommentRequestDtoTest {

	@Test
	@DisplayName("CommentRequestDto 생성 테스트")
	void testDtoCreationAndGetterMethods() {
		// given
		String content = "This is a test comment";

		// when
		CommentRequestDto dto = new CommentRequestDto(content);

		// then
		assertNotNull(dto);
		assertEquals(content, dto.getContent());
	}

}
