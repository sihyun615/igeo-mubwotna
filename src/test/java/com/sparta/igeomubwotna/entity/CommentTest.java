package com.sparta.igeomubwotna.entity;

import com.sparta.igeomubwotna.dto.CommentRequestDto;
import com.sparta.igeomubwotna.dto.RecipeRequestDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CommentTest {

	private Comment comment;
	private Recipe recipe;
	private User user;

	@BeforeEach
	void setUp() {
		user = new User("123syihyun123", "Qkrtlgus11!", "sihyun", "111lch_n9@df.com", "Hi");
		recipe = new Recipe(new RecipeRequestDto("Recipe Title", "Recipe Content"), user);
		comment = new Comment(new CommentRequestDto("Comment Content"), recipe, user);
	}

	@Test
	@DisplayName("Comment 생성자 테스트")
	void testCommentConstructor() {
		assertEquals("Comment Content", comment.getContent());
		assertEquals(0L, comment.getLikeCount());
		assertEquals(recipe, comment.getRecipe());
		assertEquals(user, comment.getUser());
	}

	@Test
	@DisplayName("좋아요 추가 테스트")
	void testAddLike() {
		comment.addLike();
		assertEquals(1L, comment.getLikeCount());
	}

	@Test
	@DisplayName("좋아요 감소 테스트")
	void testMinusLike() {
		comment.minusLike();
		assertEquals(-1L, comment.getLikeCount());
	}

	@Test
	@DisplayName("Comment 업데이트 테스트")
	void testUpdate() {
		comment.update("Updated Content");
		assertEquals("Updated Content", comment.getContent());
	}
}
