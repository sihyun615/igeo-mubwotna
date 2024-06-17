package com.sparta.igeomubwotna.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import static org.junit.jupiter.api.Assertions.*;

public class SigninRequestDtoTest {

	private SigninRequestDto signinRequestDto;
	private LocalValidatorFactoryBean validator;

	@BeforeEach
	void setUp() {
		validator = new LocalValidatorFactoryBean();
		validator.afterPropertiesSet();

		signinRequestDto = new SigninRequestDto(
			"username",
			"password"
		);
	}

	@Test
	@DisplayName("유효한 SigninRequestDto 테스트")
	void testValidSigninRequestDto() {
		BindingResult bindingResult = new BeanPropertyBindingResult(signinRequestDto, "signinRequestDto");
		validator.validate(signinRequestDto, bindingResult);
		assertFalse(bindingResult.hasErrors());
	}

	@Test
	@DisplayName("아이디가 공백인 경우")
	void testBlankUserId() {
		SigninRequestDto invalidDto = new SigninRequestDto("", "password");
		BindingResult bindingResult = new BeanPropertyBindingResult(invalidDto, "signinRequestDto");
		validator.validate(invalidDto, bindingResult);
		FieldError error = bindingResult.getFieldError("userId");

		assertNotNull(error);
		assertEquals("아이디는 공백일 수 없습니다.", error.getDefaultMessage());
	}

	@Test
	@DisplayName("비밀번호가 공백인 경우")
	void testBlankPassword() {
		SigninRequestDto invalidDto = new SigninRequestDto("username", "");
		BindingResult bindingResult = new BeanPropertyBindingResult(invalidDto, "signinRequestDto");
		validator.validate(invalidDto, bindingResult);
		FieldError error = bindingResult.getFieldError("password");

		assertNotNull(error);
		assertEquals("비밀번호는 공백일 수 없습니다.", error.getDefaultMessage());
	}

	@Test
	@DisplayName("아이디와 비밀번호가 모두 공백인 경우")
	void testBlankUserIdAndPassword() {
		SigninRequestDto invalidDto = new SigninRequestDto("", "");
		BindingResult bindingResult = new BeanPropertyBindingResult(invalidDto, "signinRequestDto");
		validator.validate(invalidDto, bindingResult);

		assertTrue(bindingResult.hasFieldErrors("userId"));
		assertTrue(bindingResult.hasFieldErrors("password"));
		assertEquals("아이디는 공백일 수 없습니다.", bindingResult.getFieldError("userId").getDefaultMessage());
		assertEquals("비밀번호는 공백일 수 없습니다.", bindingResult.getFieldError("password").getDefaultMessage());
	}
}
