package com.sparta.igeomubwotna.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UserUpdateRequestDtoTest {

	// UserUpdateRequestDto 인스턴스와 Validator 인스턴스를 선언
	private UserUpdateRequestDto userUpdateRequestDto;
	private Validator validator;

	@BeforeEach
	void setUp() {
		// ValidatorFactory를 생성하고 이를 통해 Validator 인스턴스를 초기화함
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();

		userUpdateRequestDto = new UserUpdateRequestDto(
			"sihyun222",
			"Hi Hello",
			"Qkrtlgus11!",
			"NewPassword1!"
		);
	}

	@Test
	@DisplayName("유효한 UserUpdateRequestDto 테스트")
	void testValidUserUpdateRequestDto() {
		// userUpdateRequestDto의 유효성을 검사하고 결과를 violations에 저장
		Set<ConstraintViolation<UserUpdateRequestDto>> violations = validator.validate(userUpdateRequestDto);
		// 유효성 검사 결과가 없음을 확인
		assertTrue(violations.isEmpty());
	}

	@Test
	@DisplayName("이름이 공백인 경우")
	void testBlankName() {
		UserUpdateRequestDto invalidDto = new UserUpdateRequestDto("", "Hi Hello", "Qkrtlgus11!", "NewPassword1!");
		// invalidDto의 유효성을 검사하고 결과를 violations에 저장
		Set<ConstraintViolation<UserUpdateRequestDto>> violations = validator.validate(invalidDto);
		// name 필드에서 발생한 유효성 검사 오류를 확인
		ConstraintViolation<UserUpdateRequestDto> error = violations.stream()
			.filter(violation -> "name".equals(violation.getPropertyPath().toString()))
			.findFirst()
			.orElse(null);

		assertNotNull(error);
		assertEquals("공백일 수 없습니다", error.getMessage());
	}

	@Test
	@DisplayName("설명이 공백인 경우")
	void testBlankDescription() {
		UserUpdateRequestDto invalidDto = new UserUpdateRequestDto("sihyun222", "", "Qkrtlgus11!", "NewPassword1!");
		// invalidDto의 유효성을 검사하고 결과를 violations에 저장
		Set<ConstraintViolation<UserUpdateRequestDto>> violations = validator.validate(invalidDto);
		// description 필드에서 발생한 유효성 검사 오류를 확인
		ConstraintViolation<UserUpdateRequestDto> error = violations.stream()
			.filter(violation -> "description".equals(violation.getPropertyPath().toString()))
			.findFirst()
			.orElse(null);

		assertNotNull(error);
		assertEquals("공백일 수 없습니다", error.getMessage());
	}

	@Test
	@DisplayName("새로운 비밀번호가 유효하지 않은 경우")
	void testInvalidNewPassword() {
		UserUpdateRequestDto invalidDto = new UserUpdateRequestDto("sihyun222", "Hi Hello", "Qkrtlgus11!", "weakPassword");
		// invalidDto의 유효성을 검사하고 결과를 violations에 저장
		Set<ConstraintViolation<UserUpdateRequestDto>> violations = validator.validate(invalidDto);
		// newPassword 필드에서 발생한 유효성 검사 오류를 확인
		ConstraintViolation<UserUpdateRequestDto> error = violations.stream()
			.filter(violation -> "newPassword".equals(violation.getPropertyPath().toString()))
			.findFirst()
			.orElse(null);

		assertNotNull(error);
		assertEquals("password는 알파벳 대소문자(a~z, A~Z), 숫자(0~9), 특수문자로만 구성되어야 합니다.", error.getMessage());
	}
}
