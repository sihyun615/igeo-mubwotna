package com.sparta.igeomubwotna.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

	private User user;

	@BeforeEach
	void setUp() {
		user = new User("123syihyun123", "Qkrtlgus11!", "sihyun", "111lch_n9@df.com", "Hi");
	}

	@Test
	@DisplayName("User 생성자 테스트")
	void testUserConstructor() {
		assertEquals("123syihyun123", user.getUserId());
		assertEquals("Qkrtlgus11!", user.getPassword());
		assertEquals("sihyun", user.getName());
		assertEquals("111lch_n9@df.com", user.getEmail());
		assertEquals("Hi", user.getDescription());
		assertEquals(UserStatusEnum.ACTIVE, user.getStatus());
	}

	@Test
	@DisplayName("이름 변경 테스트")
	void testUpdateName() {
		user.updateName("Updated Name");
		assertEquals("Updated Name", user.getName());
	}

	@Test
	@DisplayName("설명 변경 테스트")
	void testUpdateDescription() {
		user.updateDescription("Updated Description");
		assertEquals("Updated Description", user.getDescription());
	}

	@Test
	@DisplayName("비밀번호 변경 테스트")
	void testUpdatePassword() {
		user.updatePassword("newPassword123!");
		assertEquals("newPassword123!", user.getPassword());
	}

	@Test
	@DisplayName("Refresh Token 업데이트 테스트")
	void testUpdateRefreshToken() {
		user.updateRefreshToken("newRefreshToken");
		assertEquals("newRefreshToken", user.getRefreshToken());
	}

	@Test
	@DisplayName("회원 탈퇴 테스트")
	void testWithdraw() {
		assertFalse(user.isWithdrawn());

		user.withdraw();

		assertTrue(user.isWithdrawn());
		assertNotNull(user.getStatusModifiedAt());
		assertTrue(user.getStatusModifiedAt().isBefore(LocalDateTime.now().plusSeconds(1))); // 현재 시간 이전인지 확인
	}
}
