package com.sparta.igeomubwotna.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.sparta.igeomubwotna.entity.User;
import com.sparta.igeomubwotna.repository.UserRepository;
import com.sparta.igeomubwotna.security.UserDetailsServiceImpl;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserDetailsServiceImpl userDetailsService;

	private User user;

	@BeforeEach
	void setUp() {
		user = new User("123syihyun123", "Qkrtlgus11!", "sihyun", "111lch_n9@df.com", "Hi");
		user.setId(1L);
	}

	@Test
	@DisplayName("loadUserByUsername 테스트")
	void testLoadUserByUsernameSuccess() {
		String userId = "123syihyun123";
		given(userRepository.findByUserId(anyString())).willReturn(Optional.of(user));

		UserDetails result = userDetailsService.loadUserByUsername(userId);

		assertNotNull(result);
		assertEquals(user.getUserId(), result.getUsername());
	}

	@Test
	@DisplayName("loadUserByUsername 테스트 실패 : User Not Found")
	void testLoadUserByUsernameNotFound() {
		String userId = "123syihyun123";
		given(userRepository.findByUserId(anyString())).willReturn(Optional.empty());

		UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () ->
			userDetailsService.loadUserByUsername(userId));

		assertEquals("User not found with userId: " + userId, exception.getMessage());
	}
}
