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

public class SigninRequestDtoTest {

	// SigninRequestDto 인스턴스와 Validator 인스턴스를 선언
	private SigninRequestDto signinRequestDto;
	private Validator validator;

	@BeforeEach
	void setUp() {
		// ValidatorFactory를 생성하고 이를 통해 Validator 인스턴스를 초기화
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();

		// 유효한 SigninRequestDto 인스턴스를 초기화
		signinRequestDto = new SigninRequestDto("username", "password");
	}

	@Test
	@DisplayName("유효한 SigninRequestDto 테스트")
	void testValidSigninRequestDto() {
		// signinRequestDto의 유효성을 검사하고 결과를 violations에 저장
		Set<ConstraintViolation<SigninRequestDto>> violations = validator.validate(signinRequestDto);
		// 유효성 검사 결과가 없음을 확인
		assertTrue(violations.isEmpty());
	}

	@Test
	@DisplayName("아이디가 공백인 경우")
	void testBlankUserId() {
		SigninRequestDto invalidDto = new SigninRequestDto("", "password");
		// invalidDto의 유효성을 검사하고 결과를 violations에 저장
		Set<ConstraintViolation<SigninRequestDto>> violations = validator.validate(invalidDto);
		// userId 필드에서 발생한 유효성 검사 오류를 확인
		ConstraintViolation<SigninRequestDto> error = violations.stream()
			.filter(violation -> "userId".equals(violation.getPropertyPath().toString()))
			.findFirst()
			.orElse(null);

		assertNotNull(error);
		assertEquals("아이디는 공백일 수 없습니다.", error.getMessage());
	}

	@Test
	@DisplayName("비밀번호가 공백인 경우")
	void testBlankPassword() {
		SigninRequestDto invalidDto = new SigninRequestDto("username", "");
		// invalidDto의 유효성을 검사하고 결과를 violations에 저장
		Set<ConstraintViolation<SigninRequestDto>> violations = validator.validate(invalidDto);
		// password 필드에서 발생한 유효성 검사 오류를 확인
		ConstraintViolation<SigninRequestDto> error = violations.stream()
			.filter(violation -> "password".equals(violation.getPropertyPath().toString()))
			.findFirst()
			.orElse(null);

		assertNotNull(error);
		assertEquals("비밀번호는 공백일 수 없습니다.", error.getMessage());
	}

	@Test
	@DisplayName("아이디와 비밀번호가 모두 공백인 경우")
	void testBlankUserIdAndPassword() {
		SigninRequestDto invalidDto = new SigninRequestDto("", "");
		// invalidDto의 유효성을 검사하고 결과를 violations에 저장
		Set<ConstraintViolation<SigninRequestDto>> violations = validator.validate(invalidDto);

		// userId 필드에서 발생한 유효성 검사 오류를 확인
		boolean userIdError = violations.stream().anyMatch(violation ->
			"userId".equals(violation.getPropertyPath().toString()) &&
				"아이디는 공백일 수 없습니다.".equals(violation.getMessage()));

		// password 필드에서 발생한 유효성 검사 오류를 확인
		boolean passwordError = violations.stream().anyMatch(violation ->
			"password".equals(violation.getPropertyPath().toString()) &&
				"비밀번호는 공백일 수 없습니다.".equals(violation.getMessage()));

		// userId와 password 필드 모두에서 오류가 발생했는지 확인
		assertTrue(userIdError);
		assertTrue(passwordError);
	}
}
