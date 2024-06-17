package com.sparta.igeomubwotna.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.sparta.igeomubwotna.dto.CommentRequestDto;
import com.sparta.igeomubwotna.dto.CommentResponseDto;
import com.sparta.igeomubwotna.entity.Comment;
import com.sparta.igeomubwotna.entity.Recipe;
import com.sparta.igeomubwotna.entity.User;
import com.sparta.igeomubwotna.repository.CommentRepository;

@ExtendWith(MockitoExtension.class)
class CommentServiceIntegrationTest {

	@Mock
	private CommentRepository commentRepository;

	@Mock
	private RecipeService recipeService;

	@InjectMocks
	private CommentService commentService;

	private User user;
	private Recipe recipe;
	private Comment comment;

	@BeforeEach
	void setUp() {
		user = new User("123syihyun123", "Qkrtlgus11!", "sihyun", "111lch_n9@df.com", "Hi");
		user.setId(1L);

		recipe = new Recipe();
		recipe.setId(1L);

		comment = new Comment(new CommentRequestDto("Test comment"), recipe, user);
		comment.setId(1L);
	}

	@Test
	@DisplayName("댓글 작성")
	void testCreateComment() {
		CommentRequestDto requestDto = new CommentRequestDto("Test comment");
		ResponseEntity<String> responseEntity = ResponseEntity.status(HttpStatus.OK).body("comment가 등록되었습니다.");

		given(recipeService.findById(anyLong())).willReturn(recipe);
		given(commentRepository.save(any(Comment.class))).willReturn(comment);

		ResponseEntity<String> result = commentService.createComment(requestDto, recipe.getId(), user);

		assertNotNull(result);
		assertEquals(responseEntity.getStatusCode(), result.getStatusCode());
		assertEquals(responseEntity.getBody(), result.getBody());
	}

	@Test
	@DisplayName("댓글 조회")
	void testGetComment() {
		List<Comment> comments = new ArrayList<>();
		comments.add(comment);

		given(commentRepository.findByRecipeId(anyLong())).willReturn(comments);

		List<CommentResponseDto> commentResponseDtoList = commentService.getComment(recipe.getId());

		assertFalse(commentResponseDtoList.isEmpty());
		assertEquals(1, commentResponseDtoList.size());
		assertEquals(comment.getContent(), commentResponseDtoList.get(0).getContent());
	}

	@Test
	@DisplayName("댓글 수정")
	void testUpdateComment() {
		CommentRequestDto requestDto = new CommentRequestDto("Updated comment");
		ResponseEntity<String> responseEntity = ResponseEntity.status(HttpStatus.OK).body("comment가 수정되었습니다.");

		given(recipeService.findById(anyLong())).willReturn(recipe);
		given(commentRepository.findById(anyLong())).willReturn(Optional.of(comment));

		ResponseEntity<String> result = commentService.updateComment(recipe.getId(), comment.getId(), requestDto, user);

		assertNotNull(result);
		assertEquals(responseEntity.getStatusCode(), result.getStatusCode());
		assertEquals(responseEntity.getBody(), result.getBody());
	}

	@Test
	@DisplayName("댓글 삭제")
	void testDeleteComment() {
		given(recipeService.findById(anyLong())).willReturn(recipe);
		given(commentRepository.findById(anyLong())).willReturn(Optional.of(comment));

		assertDoesNotThrow(() -> commentService.deleteComment(recipe.getId(), comment.getId(), user));
	}

	@Test
	@DisplayName("댓글 삭제 - 다른 사용자의 경우 예외 발생")
	void testDeleteCommentDifferentUser() {
		User anotherUser = new User("otheruser", "password", "Other User", "other@example.com", "Hello");

		// Given
		given(recipeService.findById(recipe.getId())).willReturn(recipe);
		given(commentRepository.findById(comment.getId())).willReturn(java.util.Optional.ofNullable(comment));

		// When, Then
		assertThrows(IllegalArgumentException.class, () -> {
			commentService.deleteComment(recipe.getId(), comment.getId(), anotherUser);
		});
	}

	@Test
	@DisplayName("댓글 찾기")
	void testFindByIdExistingComment() {
		given(commentRepository.findById(anyLong())).willReturn(Optional.of(comment));

		Comment foundComment = commentService.findById(comment.getId());

		assertNotNull(foundComment);
		assertEquals(comment.getId(), foundComment.getId());
		assertEquals(comment.getContent(), foundComment.getContent());
	}

}
