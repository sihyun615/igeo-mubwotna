package com.sparta.igeomubwotna.entity;

import com.sparta.igeomubwotna.dto.RecipeRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RecipeTest {

	private Recipe recipe;
	private User user;

	@BeforeEach
	void setUp() {
		user = new User("123syihyun123", "Qkrtlgus11!", "sihyun", "111lch_n9@df.com", "Hi");
		recipe = new Recipe(new RecipeRequestDto("Title", "Content"), user);
	}

	@Test
	@DisplayName("Recipe 생성자 테스트")
	void testRecipeConstructor() {
		assertEquals("Title", recipe.getTitle());
		assertEquals("Content", recipe.getContent());
		assertEquals(0L, recipe.getRecipeLikes());
		assertEquals(user, recipe.getUser());
	}

	@Test
	@DisplayName("좋아요 추가 테스트")
	void testAddLike() {
		recipe.addLike();
		assertEquals(1L, recipe.getRecipeLikes());
	}

	@Test
	@DisplayName("좋아요 감소 테스트")
	void testMinusLike() {
		recipe.minusLike();
		assertEquals(-1L, recipe.getRecipeLikes());
	}

	@Test
	@DisplayName("Recipe 업데이트 테스트 - 제목만 업데이트")
	void testUpdateTitle() {
		RecipeRequestDto updateDto = new RecipeRequestDto("Updated Title", null);
		recipe.update(updateDto);
		assertEquals("Updated Title", recipe.getTitle());
		assertEquals("Content", recipe.getContent()); // 내용은 변경되지 않아야 함
	}

	@Test
	@DisplayName("Recipe 업데이트 테스트 - 내용만 업데이트")
	void testUpdateContent() {
		RecipeRequestDto updateDto = new RecipeRequestDto(null, "Updated Content");
		recipe.update(updateDto);
		assertEquals("Title", recipe.getTitle()); // 제목은 변경되지 않아야 함
		assertEquals("Updated Content", recipe.getContent());
	}

	@Test
	@DisplayName("Recipe 업데이트 테스트 - 제목과 내용 모두 업데이트")
	void testUpdateBothTitleAndContent() {
		RecipeRequestDto updateDto = new RecipeRequestDto("Updated Title", "Updated Content");
		recipe.update(updateDto);
		assertEquals("Updated Title", recipe.getTitle());
		assertEquals("Updated Content", recipe.getContent());
	}
}

