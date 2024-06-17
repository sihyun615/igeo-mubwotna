package com.sparta.igeomubwotna.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import static org.junit.jupiter.api.Assertions.*;

public class RecipeRequestDtoTest {

	private RecipeRequestDto recipeRequestDto;
	private LocalValidatorFactoryBean validator;

	@BeforeEach
	void setUp() {
		validator = new LocalValidatorFactoryBean();
		validator.afterPropertiesSet();

		recipeRequestDto = new RecipeRequestDto("Recipe Title", "Recipe Content");
	}

	@Test
	@DisplayName("유효한 RecipeRequestDto 테스트")
	void testValidRecipeRequestDto() {
		BindingResult bindingResult = new BeanPropertyBindingResult(recipeRequestDto, "recipeRequestDto");
		validator.validate(recipeRequestDto, bindingResult);
		assertFalse(bindingResult.hasErrors());
	}

	@Test
	@DisplayName("제목이 공백인 경우")
	void testBlankTitle() {
		RecipeRequestDto invalidDto = new RecipeRequestDto("", "Recipe Content");
		BindingResult bindingResult = new BeanPropertyBindingResult(invalidDto, "recipeRequestDto");
		validator.validate(invalidDto, bindingResult);
		FieldError error = bindingResult.getFieldError("title");

		assertNotNull(error);
		assertEquals("공백일 수 없습니다", error.getDefaultMessage());
	}

	@Test
	@DisplayName("내용이 공백인 경우")
	void testBlankContent() {
		RecipeRequestDto invalidDto = new RecipeRequestDto("Recipe Title", "");
		BindingResult bindingResult = new BeanPropertyBindingResult(invalidDto, "recipeRequestDto");
		validator.validate(invalidDto, bindingResult);
		FieldError error = bindingResult.getFieldError("content");

		assertNotNull(error);
		assertEquals("공백일 수 없습니다", error.getDefaultMessage());
	}

	@Test
	@DisplayName("제목과 내용이 모두 공백인 경우")
	void testBlankTitleAndContent() {
		RecipeRequestDto invalidDto = new RecipeRequestDto("", "");
		BindingResult bindingResult = new BeanPropertyBindingResult(invalidDto, "recipeRequestDto");
		validator.validate(invalidDto, bindingResult);

		assertTrue(bindingResult.hasFieldErrors("title"));
		assertTrue(bindingResult.hasFieldErrors("content"));
		assertEquals("공백일 수 없습니다", bindingResult.getFieldError("title").getDefaultMessage());
		assertEquals("공백일 수 없습니다", bindingResult.getFieldError("content").getDefaultMessage());
	}
}
