package com.sparta.igeomubwotna.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Locale;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class SignupRequestDtoTest {

	private SignupRequestDto signupRequestDto;
	private Validator validator;

	@BeforeEach
	void setUp() {
		Locale.setDefault(Locale.KOREAN);
		// ValidatorFactory를 통해 Validator 객체를 생성
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();

		signupRequestDto = new SignupRequestDto(
			"123syihyun123",
			"Qkrtlgus11!",
			"sihyun",
			"111lch_n9@df.com",
			"description test"
		);
	}

	@Test
	@DisplayName("유효한 SignupRequestDto 테스트")
	void testValidSignupRequestDto() {
		// Validator를 사용하여 DTO를 검증하고, 결과를 violations에 저장
		Set<ConstraintViolation<SignupRequestDto>> violations = validator.validate(signupRequestDto);
		assertTrue(violations.isEmpty());  // violations이 비어있음을 확인하여, DTO가 유효함을 검증
	}


	@Test
	@DisplayName("길이를 충족하지 못하는 사용자 ID 테스트")
	void testShortUserId() {
		SignupRequestDto invalidDto = new SignupRequestDto("shortId", "Qkrtlgus11!", "sihyun", "111lch_n9@df.com", "description test");
		// Validator를 사용하여 DTO를 검증하고, 결과를 violations에 저장
		Set<ConstraintViolation<SignupRequestDto>> violations = validator.validate(invalidDto);

		assertFalse(violations.isEmpty());  // violations이 비어있지 않음을 확인하여, DTO에 유효성 오류가 있음을 검증
		// violations 중에서 userId 필드에 대한 오류 메시지를 확인
		assertTrue(violations.stream().anyMatch(violation ->
			"userId".equals(violation.getPropertyPath().toString()) &&  //violation의 propertyPath(어떤 필드에 오류가 있는지 나타냄)를 문자열로 변환하여, 그 값이 "userId"와 같은지 확인
				"사용자 ID는 최소 10글자 이상, 20글자 이하이어야 합니다.".equals(violation.getMessage())
		));
	}

	@Test
	@DisplayName("유효하지 않은 사용자 ID 테스트")
	void testInvalidUserId() {
		SignupRequestDto invalidDto = new SignupRequestDto("sihyun123123!", "Qkrtlgus11!", "sihyun", "111lch_n9@df.com", "description test");
		// Validator를 사용하여 DTO를 검증하고, 결과를 violations에 저장
		Set<ConstraintViolation<SignupRequestDto>> violations = validator.validate(invalidDto);

		assertFalse(violations.isEmpty());  // violations이 비어있지 않음을 확인하여, DTO에 유효성 오류가 있음을 검증
		// violations 중에서 email 필드에 대한 오류 메시지를 확인
		assertTrue(violations.stream().anyMatch(violation ->
			"userId".equals(violation.getPropertyPath().toString()) &&
				"사용자 ID는 알파벳 대소문자, 숫자로만 구성되어야 합니다.".equals(violation.getMessage())
		));
	}


	@Test
	@DisplayName("길이를 충족하지 못하는 비밀번호 테스트")
	void testShortPassword() {
		SignupRequestDto invalidDto = new SignupRequestDto("123syihyun123", "Shorpwd1!", "sihyun", "111lch_n9@df.com", "description test");
		// Validator를 사용하여 DTO를 검증하고, 결과를 violations에 저장
		Set<ConstraintViolation<SignupRequestDto>> violations = validator.validate(invalidDto);

		// violations이 비어있지 않음을 확인하여, DTO에 유효성 오류가 있음을 검증
		assertFalse(violations.isEmpty());
		assertTrue(violations.stream().anyMatch(violation ->
			"password".equals(violation.getPropertyPath().toString()) &&
				"password는 최소 10글자 이상이어야 합니다.".equals(violation.getMessage())
		));
	}

	@Test
	@DisplayName("유효하지 않은 비밀번호 테스트")
	void testInvalidPassword() {
		SignupRequestDto invalidDto = new SignupRequestDto("123syihyun123", "shortpwd", "sihyun", "111lch_n9@df.com", "description test");
		// Validator를 사용하여 DTO를 검증하고, 결과를 violations에 저장
		Set<ConstraintViolation<SignupRequestDto>> violations = validator.validate(invalidDto);

		// violations이 비어있지 않음을 확인하여, DTO에 유효성 오류가 있음을 검증
		assertFalse(violations.isEmpty());
		assertTrue(violations.stream().anyMatch(violation ->
			"password".equals(violation.getPropertyPath().toString()) &&
				"password는 알파벳 대소문자(a~z, A~Z), 숫자(0~9), 특수문자로만 구성되어야 합니다.".equals(violation.getMessage())
		));
	}


	@Test
	@DisplayName("유효하지 않은 이메일 테스트")
	void testInvalidEmail() {
		SignupRequestDto invalidDto = new SignupRequestDto("123syihyun123", "Qkrtlgus11!", "sihyun", "invalidEmail", "description test");
		// Validator를 사용하여 DTO를 검증하고, 결과를 violations에 저장
		Set<ConstraintViolation<SignupRequestDto>> violations = validator.validate(invalidDto);

		// violations이 비어있지 않음을 확인하여, DTO에 유효성 오류가 있음을 검증
		assertFalse(violations.isEmpty());
		assertTrue(violations.stream().anyMatch(violation ->
			"email".equals(violation.getPropertyPath().toString()) &&
				"올바른 형식의 이메일 주소여야 합니다".equals(violation.getMessage())
		));
	}

	@Test
	@DisplayName("유효하지 않은 빈 필드 테스트")
	void testBlankFields() {
		SignupRequestDto invalidDto = new SignupRequestDto("", "", "", "", "");
		// Validator를 사용하여 DTO를 검증하고, 결과를 violations에 저장
		Set<ConstraintViolation<SignupRequestDto>> violations = validator.validate(invalidDto);

		// violations이 비어있지 않음을 확인하여, DTO에 유효성 오류가 있음을 검증
		assertFalse(violations.isEmpty());
		assertTrue(violations.stream().anyMatch(violation ->
			"userId".equals(violation.getPropertyPath().toString()) &&
				"사용자 ID는 최소 10글자 이상, 20글자 이하이어야 합니다.".equals(violation.getMessage())
		));
		assertTrue(violations.stream().anyMatch(violation ->
			"password".equals(violation.getPropertyPath().toString()) &&
				"password는 알파벳 대소문자(a~z, A~Z), 숫자(0~9), 특수문자로만 구성되어야 합니다.".equals(violation.getMessage())
		));
		assertTrue(violations.stream().anyMatch(violation ->
			"name".equals(violation.getPropertyPath().toString()) &&
				"공백일 수 없습니다".equals(violation.getMessage())
		));
		assertTrue(violations.stream().anyMatch(violation ->
			"email".equals(violation.getPropertyPath().toString()) &&
				"공백일 수 없습니다".equals(violation.getMessage())
		));
		assertTrue(violations.stream().anyMatch(violation ->
			"description".equals(violation.getPropertyPath().toString()) &&
				"공백일 수 없습니다".equals(violation.getMessage())
		));
	}


}
