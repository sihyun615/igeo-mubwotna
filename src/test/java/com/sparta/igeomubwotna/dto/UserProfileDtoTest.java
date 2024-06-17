package com.sparta.igeomubwotna.dto;

import com.sparta.igeomubwotna.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class UserProfileDtoTest {

	private UserProfileDto userProfileDto;

	@BeforeEach
	void setUp() {
		User user = mock(User.class);
		given(user.getUserId()).willReturn("123syihyun123");
		given(user.getName()).willReturn("sihyun");
		given(user.getDescription()).willReturn("Hi");
		given(user.getEmail()).willReturn("111lch_n9@df.com");

		userProfileDto = new UserProfileDto(user);
	}

	@Test
	@DisplayName("UserProfileDto 생성 테스트")
	void testUserProfileDtoCreation() {
		assertNotNull(userProfileDto);
		assertEquals("123syihyun123", userProfileDto.getUserId());
		assertEquals("sihyun", userProfileDto.getName());
		assertEquals("Hi", userProfileDto.getDescription());
		assertEquals("111lch_n9@df.com", userProfileDto.getEmail());
	}
}
