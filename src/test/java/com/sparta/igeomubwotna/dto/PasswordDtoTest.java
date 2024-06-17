package com.sparta.igeomubwotna.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PasswordDtoTest {

	@Test
	@DisplayName("PasswordDto 생성 테스트")
	void testPasswordDtoCreation() {
		// given
		String password = "Password123!";

		// when
		PasswordDto passwordDto = new PasswordDto(password);

		// then
		assertNotNull(passwordDto);
		assertEquals(password, passwordDto.getPassword());
	}
}