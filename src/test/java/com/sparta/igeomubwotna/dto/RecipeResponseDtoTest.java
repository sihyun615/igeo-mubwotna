package com.sparta.igeomubwotna.dto;

import com.sparta.igeomubwotna.entity.Recipe;
import com.sparta.igeomubwotna.entity.User;
import com.sparta.igeomubwotna.entity.UserStatusEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class RecipeResponseDtoTest {

	private RecipeResponseDto recipeResponseDto;

	@BeforeEach
	void setUp() {
		Recipe recipe = mock(Recipe.class);
		given(recipe.getTitle()).willReturn("Test Recipe");
		given(recipe.getContent()).willReturn("Recipe Content");

		User user = new User("123syihyun123", "Qkrtlgus11!", "sihyun", "111lch_n9@df.com", "Hi");
		user.setStatus(UserStatusEnum.ACTIVE);

		given(recipe.getUser()).willReturn(user);
		given(recipe.getRecipeLikes()).willReturn(10L);
		LocalDateTime createdAt = LocalDateTime.of(2023, 6, 15, 10, 0);
		given(recipe.getCreatedAt()).willReturn(createdAt);
		LocalDateTime modifiedAt = LocalDateTime.of(2023, 6, 16, 11, 0);
		given(recipe.getModifiedAt()).willReturn(modifiedAt);

		recipeResponseDto = new RecipeResponseDto(recipe);
	}

	@Test
	@DisplayName("RecipeResponseDto 생성 테스트")
	void testRecipeResponseDtoCreation() {
		assertNotNull(recipeResponseDto);
		assertEquals("Test Recipe", recipeResponseDto.getTitle());
		assertEquals("Recipe Content", recipeResponseDto.getContent());
		assertEquals("123syihyun123", recipeResponseDto.getUserId());
		assertEquals(10L, recipeResponseDto.getRecipeLikes());
		assertEquals(LocalDateTime.of(2023, 6, 15, 10, 0), recipeResponseDto.getCreatedAt());
		assertEquals(LocalDateTime.of(2023, 6, 16, 11, 0), recipeResponseDto.getModifiedAt());
	}
}
