package com.sparta.igeomubwotna.dto;

import com.sparta.igeomubwotna.entity.Comment;
import com.sparta.igeomubwotna.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class CommentResponseDtoTest {

	@Mock
	private Comment comment;

	@Mock
	private User user;


	@Test
	@DisplayName("CommentResponseDto 의 toDto() 테스트")
	void testDtoStaticFactoryMethod() {
		// given
		Long id = 1L;
		String content = "This is a comment";
		String userId = "user123";
		LocalDateTime createdAt = LocalDateTime.now();
		Long likeCount = 10L;

		given(comment.getId()).willReturn(id);
		given(comment.getContent()).willReturn(content);
		given(comment.getUser()).willReturn(user);
		given(user.getUserId()).willReturn(userId);
		given(comment.getCreatedAt()).willReturn(createdAt);
		given(comment.getLikeCount()).willReturn(likeCount);

		// when
		CommentResponseDto dto = CommentResponseDto.toDto(comment);

		// then
		assertNotNull(dto);
		assertEquals(comment.getId(), dto.getId());
		assertEquals(comment.getContent(), dto.getContent());
		assertEquals(user.getUserId(), dto.getUserId());
		assertEquals(comment.getCreatedAt(), dto.getCreatedAt());
		assertEquals(comment.getLikeCount(), dto.getLikeCount());
	}
}
