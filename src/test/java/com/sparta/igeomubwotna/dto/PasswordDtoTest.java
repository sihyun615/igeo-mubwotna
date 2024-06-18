package com.sparta.igeomubwotna.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class PasswordDtoTest {

	private PasswordDto passwordDto;
	private Validator validator;

	@BeforeEach
	void setUp() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	@DisplayName("PasswordDto 유효성 검사 - 올바른 패스워드")
	void testValidPassword() {
		passwordDto = new PasswordDto("ValidPassword123!");
		Set<ConstraintViolation<PasswordDto>> violations = validator.validate(passwordDto);

		assertTrue(violations.isEmpty());
	}

	@Test
	@DisplayName("PasswordDto 유효성 검사 - 패스워드가 Null")
	void testNullPassword() {
		passwordDto = new PasswordDto(null);
		Set<ConstraintViolation<PasswordDto>> violations = validator.validate(passwordDto);

		assertFalse(violations.isEmpty());
		ConstraintViolation<PasswordDto> violation = violations.iterator().next();
		assertEquals("password는 null이 될 수 없습니다.", violation.getMessage());
	}
}
