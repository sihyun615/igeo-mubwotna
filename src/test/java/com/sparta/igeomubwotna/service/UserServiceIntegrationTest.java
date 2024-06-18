package com.sparta.igeomubwotna.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;

import com.sparta.igeomubwotna.dto.PasswordDto;
import com.sparta.igeomubwotna.dto.Response;
import com.sparta.igeomubwotna.dto.SignupRequestDto;
import com.sparta.igeomubwotna.dto.UserProfileDto;
import com.sparta.igeomubwotna.dto.UserUpdateRequestDto;
import com.sparta.igeomubwotna.entity.User;
import com.sparta.igeomubwotna.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceIntegrationTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@InjectMocks
	private UserService userService;

	private User user;

	@BeforeEach
	void setUp() {
		user = new User("123syihyun123", "Qkrtlgus11!", "sihyun", "111lch_n9@df.com", "Hi");
		user.setId(1L);
	}

	@Test
	@DisplayName("회원가입 테스트")
	void testSignupSuccess() {
		SignupRequestDto requestDto = mock(SignupRequestDto.class);
		BindingResult bindingResult = mock(BindingResult.class);
		Response response = new Response(HttpStatus.OK.value(), "회원가입에 성공하였습니다.");
		ResponseEntity<Response> responseEntity = ResponseEntity.status(HttpStatus.OK).body(response);

		given(requestDto.getUserId()).willReturn("123syihyun123");
		given(requestDto.getPassword()).willReturn("Qkrtlgus11!");
		given(requestDto.getName()).willReturn("sihyun");
		given(requestDto.getEmail()).willReturn("111lch_n9@df.com");
		given(requestDto.getDescription()).willReturn("Hi");

		given(userRepository.findByUserId(anyString())).willReturn(Optional.empty());
		given(userRepository.findByEmail(anyString())).willReturn(Optional.empty());
		given(passwordEncoder.encode(anyString())).willReturn("encodedPassword");
		given(userRepository.save(any(User.class))).willReturn(user);

		ResponseEntity<Response> result = userService.signup(requestDto, bindingResult);

		assertNotNull(result);
		assertEquals(responseEntity.getStatusCode(), result.getStatusCode());
		assertEquals(responseEntity.getBody().getMessage(), result.getBody().getMessage());
	}

	@Test
	@DisplayName("회원가입 실패: 중복된 아이디")
	void testSignupFailureDuplicateUserId() {
		SignupRequestDto requestDto = mock(SignupRequestDto.class);
		BindingResult bindingResult = mock(BindingResult.class);
		Response response = new Response(HttpStatus.BAD_REQUEST.value(), "중복된 아이디가 존재합니다.");
		ResponseEntity<Response> responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

		given(requestDto.getUserId()).willReturn("123syihyun123");

		given(userRepository.findByUserId(anyString())).willReturn(Optional.of(user));

		ResponseEntity<Response> result = userService.signup(requestDto, bindingResult);

		assertNotNull(result);
		assertEquals(responseEntity.getStatusCode(), result.getStatusCode());
		assertEquals(responseEntity.getBody().getMessage(), result.getBody().getMessage());
	}

	@Test
	@DisplayName("회원가입 실패: 중복된 이메일")
	void testSignupFailureDuplicateEmail() {
		SignupRequestDto requestDto = mock(SignupRequestDto.class);
		BindingResult bindingResult = mock(BindingResult.class);
		Response response = new Response(HttpStatus.BAD_REQUEST.value(), "중복된 이메일이 존재합니다.");
		ResponseEntity<Response> responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

		given(requestDto.getUserId()).willReturn("123syihyun123");
		given(requestDto.getEmail()).willReturn("111lch_n9@df.com");

		given(userRepository.findByUserId(anyString())).willReturn(Optional.empty());
		given(userRepository.findByEmail(anyString())).willReturn(Optional.of(user));

		ResponseEntity<Response> result = userService.signup(requestDto, bindingResult);

		assertNotNull(result);
		assertEquals(responseEntity.getStatusCode(), result.getStatusCode());
		assertEquals(responseEntity.getBody().getMessage(), result.getBody().getMessage());
	}

	@Test
	@DisplayName("사용자 프로필 조회 테스트")
	void testGetUserProfileSuccess() {
		Long userId = 1L;
		given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

		UserProfileDto userProfileDto = userService.getUserProfile(userId);

		assertEquals(user.getUserId(), userProfileDto.getUserId());
		assertEquals(user.getName(), userProfileDto.getName());
		assertEquals(user.getEmail(), userProfileDto.getEmail());
		assertEquals(user.getDescription(), userProfileDto.getDescription());
	}

	@Test
	@DisplayName("사용자 프로필 수정 테스트")
	void testUpdateUserProfileSuccess() {
		Long userId = 1L;
		UserUpdateRequestDto requestDto = mock(UserUpdateRequestDto.class);
		Response response = new Response(HttpStatus.OK.value(), "프로필 정보를 성공적으로 수정하였습니다.");
		ResponseEntity<Response> responseEntity = ResponseEntity.status(HttpStatus.OK).body(response);

		given(requestDto.getName()).willReturn("Updated Name");
		given(requestDto.getDescription()).willReturn("Updated Description");
		given(requestDto.getCurrentPassword()).willReturn("Qkrtlgus11!");
		given(requestDto.getNewPassword()).willReturn("Qqkrtlgus11!");

		given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
		given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);
		given(passwordEncoder.matches("Qqkrtlgus11!", user.getPassword())).willReturn(false);
		given(passwordEncoder.encode(anyString())).willReturn("encodedNewPassword");

		ResponseEntity<Response> result = userService.updateUserProfile(requestDto, userId);

		assertNotNull(result);
		assertEquals(responseEntity.getStatusCode(), result.getStatusCode());
		assertEquals(responseEntity.getBody().getMessage(), result.getBody().getMessage());
	}

	@Test
	@DisplayName("사용자 프로필 수정 실패: 현재 비밀번호 불일치")
	void testUpdateUserProfileFailure() {
		Long userId = 1L;
		UserUpdateRequestDto requestDto = mock(UserUpdateRequestDto.class);
		Response response = new Response(HttpStatus.BAD_REQUEST.value(), "입력한 현재 비밀번호가 일치하지 않습니다.");
		ResponseEntity<Response> responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

		given(requestDto.getCurrentPassword()).willReturn("Qkrtlgus11!");
		given(requestDto.getNewPassword()).willReturn("Qqkrdtlgus11!");

		given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
		given(passwordEncoder.matches(anyString(), anyString())).willReturn(false);

		ResponseEntity<Response> result = userService.updateUserProfile(requestDto, userId);

		assertNotNull(result);
		assertEquals(responseEntity.getStatusCode(), result.getStatusCode());
		assertEquals(responseEntity.getBody().getMessage(), result.getBody().getMessage());
	}

	@Test
	@DisplayName("사용자 로그아웃 테스트")
	void testLogout() {
		Long userId = 1L;
		Response response = new Response(HttpStatus.OK.value(), "로그아웃이 완료되었습니다.");
		ResponseEntity<Response> responseEntity = ResponseEntity.status(HttpStatus.OK).body(response);

		given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

		ResponseEntity<Response> result = userService.logout(userId);

		assertNotNull(result);
		assertEquals(responseEntity.getStatusCode(), result.getStatusCode());
		assertEquals(responseEntity.getBody().getMessage(), result.getBody().getMessage());
	}

	@Test
	@DisplayName("사용자 탈퇴 테스트")
	void testWithdrawUser() {
		Long userId = 1L;
		PasswordDto passwordDto = mock(PasswordDto.class);
		Response response = new Response(HttpStatus.OK.value(), "회원 탈퇴가 성공적으로 완료되었습니다.");
		ResponseEntity<Response> responseEntity = ResponseEntity.status(HttpStatus.OK).body(response);

		given(passwordDto.getPassword()).willReturn("Qkrtlgus11!");

		// 모의된 User 객체의 비밀번호 설정
		user.setPassword("Qkrtlgus11!");
		given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
		given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);

		ResponseEntity<Response> result = userService.withdrawUser(passwordDto, userId);

		assertNotNull(result);
		assertEquals(responseEntity.getStatusCode(), result.getStatusCode());
		assertEquals(responseEntity.getBody().getMessage(), result.getBody().getMessage());
	}

	@Test
	@DisplayName("사용자 탈퇴 실패: 비밀번호 불일치")
	void testWithdrawUserFailureIncorrectPassword() {
		Long userId = 1L;
		PasswordDto passwordDto = mock(PasswordDto.class);
		Response response = new Response(HttpStatus.BAD_REQUEST.value(), "비밀번호가 일치하지 않습니다.");
		ResponseEntity<Response> responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

		given(passwordDto.getPassword()).willReturn("IncorrectPassword");

		given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
		given(passwordEncoder.matches(anyString(), anyString())).willReturn(false);

		ResponseEntity<Response> result = userService.withdrawUser(passwordDto, userId);

		assertNotNull(result);
		assertEquals(responseEntity.getStatusCode(), result.getStatusCode());
		assertEquals(responseEntity.getBody().getMessage(), result.getBody().getMessage());
	}

	@Test
	@DisplayName("비밀번호 암호화 확인 테스트")
	void testPasswordEncryption() {
		String rawPassword = "Qkrtlgus11!";
		String encodedPassword = "encodedPassword";

		given(passwordEncoder.encode(anyString())).willReturn(encodedPassword);

		String result = passwordEncoder.encode(rawPassword);

		assertEquals(encodedPassword, result);
	}
}
