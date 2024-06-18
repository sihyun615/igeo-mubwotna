package com.sparta.igeomubwotna.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Locale;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class RecipeRequestDtoTest {

	// RecipeRequestDto 인스턴스와 Validator 인스턴스를 선언
	private RecipeRequestDto recipeRequestDto;
	private Validator validator;

	@BeforeEach
	void setUp() {
		Locale.setDefault(Locale.KOREAN);
		// ValidatorFactory를 생성하고 이를 통해 Validator 인스턴스를 초기화함
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();

		recipeRequestDto = new RecipeRequestDto("Recipe Title", "Recipe Content");
	}

	@Test
	@DisplayName("유효한 RecipeRequestDto 테스트")
	void testValidRecipeRequestDto() {
		// recipeRequestDto의 유효성을 검사하고 결과를 violations에 저장
		Set<ConstraintViolation<RecipeRequestDto>> violations = validator.validate(recipeRequestDto);
		// 유효성 검사 결과가 없음을 확인
		assertTrue(violations.isEmpty());
	}

	@Test
	@DisplayName("제목이 공백인 경우")
	void testBlankTitle() {
		RecipeRequestDto invalidDto = new RecipeRequestDto("", "Recipe Content");
		// invalidDto의 유효성을 검사하고 결과를 violations에 저장
		Set<ConstraintViolation<RecipeRequestDto>> violations = validator.validate(invalidDto);
		// title 필드에서 발생한 유효성 검사 오류를 확인
		ConstraintViolation<RecipeRequestDto> error = violations.stream()
			.filter(violation -> "title".equals(violation.getPropertyPath().toString()))
			.findFirst()
			.orElse(null);

		assertNotNull(error);
		assertEquals("공백일 수 없습니다", error.getMessage());
	}

	@Test
	@DisplayName("내용이 공백인 경우")
	void testBlankContent() {
		RecipeRequestDto invalidDto = new RecipeRequestDto("Recipe Title", "");
		// invalidDto의 유효성을 검사하고 결과를 violations에 저장
		Set<ConstraintViolation<RecipeRequestDto>> violations = validator.validate(invalidDto);
		// content 필드에서 발생한 유효성 검사 오류를 확인
		ConstraintViolation<RecipeRequestDto> error = violations.stream()
			.filter(violation -> "content".equals(violation.getPropertyPath().toString()))
			.findFirst()
			.orElse(null);

		assertNotNull(error);
		assertEquals("공백일 수 없습니다", error.getMessage());
	}

	@Test
	@DisplayName("제목과 내용이 모두 공백인 경우")
	void testBlankTitleAndContent() {
		RecipeRequestDto invalidDto = new RecipeRequestDto("", "");
		// invalidDto의 유효성을 검사하고 결과를 violations에 저장
		Set<ConstraintViolation<RecipeRequestDto>> violations = validator.validate(invalidDto);

		// title 필드에서 발생한 유효성 검사 오류를 확인
		boolean titleError = violations.stream().anyMatch(violation ->
			"title".equals(violation.getPropertyPath().toString()) &&
				"공백일 수 없습니다".equals(violation.getMessage()));

		// content 필드에서 발생한 유효성 검사 오류를 확인
		boolean contentError = violations.stream().anyMatch(violation ->
			"content".equals(violation.getPropertyPath().toString()) &&
				"공백일 수 없습니다".equals(violation.getMessage()));

		assertTrue(titleError);
		assertTrue(contentError);
	}
}
