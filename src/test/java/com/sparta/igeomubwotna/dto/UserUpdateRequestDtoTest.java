package com.sparta.igeomubwotna.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import static org.junit.jupiter.api.Assertions.*;

public class UserUpdateRequestDtoTest {

	private UserUpdateRequestDto userUpdateRequestDto;
	private LocalValidatorFactoryBean validator;

	@BeforeEach
	void setUp() {
		validator = new LocalValidatorFactoryBean();
		validator.afterPropertiesSet();

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
		BindingResult bindingResult = new BeanPropertyBindingResult(userUpdateRequestDto, "userUpdateRequestDto");
		validator.validate(userUpdateRequestDto, bindingResult);
		assertFalse(bindingResult.hasErrors());
	}

	@Test
	@DisplayName("이름이 공백인 경우")
	void testBlankName() {
		UserUpdateRequestDto invalidDto = new UserUpdateRequestDto("", "Hi Hello", "Qkrtlgus11!", "NewPassword1!");
		BindingResult bindingResult = new BeanPropertyBindingResult(invalidDto, "userUpdateRequestDto");
		validator.validate(invalidDto, bindingResult);
		FieldError error = bindingResult.getFieldError("name");

		assertNotNull(error);
		assertEquals("공백일 수 없습니다", error.getDefaultMessage());
	}

	@Test
	@DisplayName("설명이 공백인 경우")
	void testBlankDescription() {
		UserUpdateRequestDto invalidDto = new UserUpdateRequestDto("sihyun222", "", "Qkrtlgus11!", "NewPassword1!");
		BindingResult bindingResult = new BeanPropertyBindingResult(invalidDto, "userUpdateRequestDto");
		validator.validate(invalidDto, bindingResult);
		FieldError error = bindingResult.getFieldError("description");

		assertNotNull(error);
		assertEquals("공백일 수 없습니다", error.getDefaultMessage());
	}

	@Test
	@DisplayName("새로운 비밀번호가 유효하지 않은 경우")
	void testInvalidNewPassword() {
		UserUpdateRequestDto invalidDto = new UserUpdateRequestDto("sihyun222", "Hi Hello", "Qkrtlgus11!", "weakPassword");
		BindingResult bindingResult = new BeanPropertyBindingResult(invalidDto, "userUpdateRequestDto");
		validator.validate(invalidDto, bindingResult);
		FieldError error = bindingResult.getFieldError("newPassword");

		assertNotNull(error);
		assertEquals("password는 알파벳 대소문자(a~z, A~Z), 숫자(0~9), 특수문자로만 구성되어야 합니다.", error.getDefaultMessage());
	}

}