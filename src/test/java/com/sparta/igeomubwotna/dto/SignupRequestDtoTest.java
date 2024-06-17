package com.sparta.igeomubwotna.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import static org.junit.jupiter.api.Assertions.*;

class SignupRequestDtoTest {

	private SignupRequestDto signupRequestDto;
	private LocalValidatorFactoryBean validator;

	@BeforeEach
	void setUp() {
		validator = new LocalValidatorFactoryBean();
		validator.afterPropertiesSet();

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
		BindingResult bindingResult = new BeanPropertyBindingResult(signupRequestDto, "signupRequestDto");
		validator.validate(signupRequestDto, bindingResult);
		assertFalse(bindingResult.hasErrors());
	}


	@Test
	@DisplayName("길이를 충족하지 못하는 사용자 ID 테스트")
	void testShortUserId() {
		SignupRequestDto invalidDto = new SignupRequestDto("shortId", "Qkrtlgus11!", "sihyun", "111lch_n9@df.com", "description test");
		BindingResult bindingResult = new BeanPropertyBindingResult(invalidDto, "signupRequestDto");
		validator.validate(invalidDto, bindingResult);
		FieldError error = bindingResult.getFieldError("userId");

		assertNotNull(error);
		assertEquals("사용자 ID는 최소 10글자 이상, 20글자 이하이어야 합니다.", error.getDefaultMessage());
	}

	@Test
	@DisplayName("유효하지 않은 사용자 ID 테스트")
	void testInvalidUserId() {
		SignupRequestDto invalidDto = new SignupRequestDto("sihyun123123!", "Qkrtlgus11!", "sihyun", "111lch_n9@df.com", "description test");
		BindingResult bindingResult = new BeanPropertyBindingResult(invalidDto, "signupRequestDto");
		validator.validate(invalidDto, bindingResult);
		FieldError error = bindingResult.getFieldError("userId");

		assertNotNull(error);
		assertEquals("사용자 ID는 알파벳 대소문자, 숫자로만 구성되어야 합니다.", error.getDefaultMessage());
	}


	@Test
	@DisplayName("길이를 충족하지 못하는 비밀번호 테스트")
	void testShortPassword() {
		SignupRequestDto invalidDto = new SignupRequestDto("123syihyun123", "Shorpwd1!", "sihyun", "111lch_n9@df.com", "description test");
		BindingResult bindingResult = new BeanPropertyBindingResult(invalidDto, "signupRequestDto");
		validator.validate(invalidDto, bindingResult);
		FieldError error = bindingResult.getFieldError("password");

		assertNotNull(error);
		assertEquals("password는 최소 10글자 이상이어야 합니다.", error.getDefaultMessage());
	}

	@Test
	@DisplayName("유효하지 않은 비밀번호 테스트")
	void testInvalidPassword() {
		SignupRequestDto invalidDto = new SignupRequestDto("123syihyun123", "shortpwd", "sihyun", "111lch_n9@df.com", "description test");
		BindingResult bindingResult = new BeanPropertyBindingResult(invalidDto, "signupRequestDto");
		validator.validate(invalidDto, bindingResult);
		FieldError error = bindingResult.getFieldError("password");

		assertNotNull(error);
		assertEquals("password는 알파벳 대소문자(a~z, A~Z), 숫자(0~9), 특수문자로만 구성되어야 합니다.", error.getDefaultMessage());
	}


	@Test
	@DisplayName("유효하지 않은 이메일 테스트")
	void testInvalidEmail() {
		SignupRequestDto invalidDto = new SignupRequestDto("123syihyun123", "Qkrtlgus11!", "sihyun", "invalidEmail", "description test");
		BindingResult bindingResult = new BeanPropertyBindingResult(invalidDto, "signupRequestDto");
		validator.validate(invalidDto, bindingResult);
		FieldError error = bindingResult.getFieldError("email");

		assertNotNull(error);
		assertEquals("올바른 형식의 이메일 주소여야 합니다", error.getDefaultMessage());
	}

	@Test
	@DisplayName("유효하지 않은 빈 필드 테스트")
	void testBlankFields() {
		SignupRequestDto invalidDto = new SignupRequestDto("", "", "", "", "");
		BindingResult bindingResult = new BeanPropertyBindingResult(invalidDto, "signupRequestDto");
		validator.validate(invalidDto, bindingResult);

		assertTrue(bindingResult.hasFieldErrors("userId"));
		assertTrue(bindingResult.hasFieldErrors("password"));
		assertTrue(bindingResult.hasFieldErrors("name"));
		assertTrue(bindingResult.hasFieldErrors("email"));
		assertTrue(bindingResult.hasFieldErrors("description"));

		assertEquals("사용자 ID는 최소 10글자 이상, 20글자 이하이어야 합니다.", bindingResult.getFieldError("userId").getDefaultMessage());
		assertEquals("password는 알파벳 대소문자(a~z, A~Z), 숫자(0~9), 특수문자로만 구성되어야 합니다.", bindingResult.getFieldError("password").getDefaultMessage());
		assertEquals("공백일 수 없습니다", bindingResult.getFieldError("name").getDefaultMessage());
		assertEquals("공백일 수 없습니다", bindingResult.getFieldError("email").getDefaultMessage());
		assertEquals("공백일 수 없습니다", bindingResult.getFieldError("description").getDefaultMessage());
	}


}
