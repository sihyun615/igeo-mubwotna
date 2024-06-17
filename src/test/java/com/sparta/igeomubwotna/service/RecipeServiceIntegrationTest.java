package com.sparta.igeomubwotna.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.sparta.igeomubwotna.dto.RecipeRequestDto;
import com.sparta.igeomubwotna.dto.RecipeResponseDto;
import com.sparta.igeomubwotna.entity.Recipe;
import com.sparta.igeomubwotna.entity.User;
import com.sparta.igeomubwotna.repository.RecipeRepository;

@ExtendWith(MockitoExtension.class)
class RecipeServiceIntegrationTest {

	@Mock
	RecipeRepository recipeRepository;

	@InjectMocks
	private RecipeService recipeService;

	private User user;
	private RecipeRequestDto requestDto;
	private Recipe existingRecipe;
	private RecipeResponseDto responseDto;

	@BeforeEach
	void setUp() {
		user = new User("123syihyun123", "Qkrtlgus11!", "sihyun", "111lch_n9@df.com", "Hi");
		user.setId(1L);

		requestDto = new RecipeRequestDto("Test Recipe", "Test content");

		existingRecipe = new Recipe(1L, user, "Initial Recipe Title", "Initial recipe content", 0L);

		responseDto = new RecipeResponseDto(new Recipe(requestDto, user));
	}

	@Test
	@DisplayName("게시물 등록")
	void testSaveRecipe() {
		ResponseEntity<RecipeResponseDto> responseEntity = ResponseEntity.status(HttpStatus.CREATED).body(responseDto);

		given(recipeRepository.save(any(Recipe.class))).willReturn(existingRecipe);

		ResponseEntity<RecipeResponseDto> result = recipeService.saveRecipe(requestDto, user);

		assertNotNull(responseDto);
		assertNotNull(result);
		assertEquals(responseEntity.getStatusCode(), result.getStatusCode());
		assertEquals(responseEntity.getBody().getClass(), result.getBody().getClass());
		assertEquals(existingRecipe.getTitle(), result.getBody().getTitle());
		assertEquals(existingRecipe.getContent(), result.getBody().getContent());
	}

	@Test
	@DisplayName("게시물 조회")
	void testGetRecipe() {
		ResponseEntity<RecipeResponseDto> responseEntity = ResponseEntity.status(HttpStatus.OK).body(responseDto);

		given(recipeRepository.findById(any(Long.class))).willReturn(Optional.of(existingRecipe));

		ResponseEntity<RecipeResponseDto> result = recipeService.getRecipe(any(Long.class));

		assertNotNull(responseDto);
		assertNotNull(result);
		assertEquals(responseEntity.getStatusCode(), result.getStatusCode());
		assertEquals(responseEntity.getBody().getClass(), result.getBody().getClass());
		assertEquals(existingRecipe.getTitle(), result.getBody().getTitle());
		assertEquals(existingRecipe.getContent(), result.getBody().getContent());
	}

	@Test
	@DisplayName("게시물 수정")
	void testEditRecipe() {
		Long recipeId = 1L;
		Recipe updatedRecipe = new Recipe(requestDto, user);
		ResponseEntity<RecipeResponseDto> responseEntity = ResponseEntity.status(HttpStatus.OK).body(responseDto);

		given(recipeRepository.findById(any(Long.class))).willReturn(Optional.of(existingRecipe));

		ResponseEntity<RecipeResponseDto> result = recipeService.editRecipe(recipeId, requestDto, user);

		assertNotNull(responseDto);
		assertNotNull(result);
		assertEquals(responseEntity.getStatusCode(), result.getStatusCode());
		assertEquals(responseEntity.getBody().getClass(), result.getBody().getClass());
		assertEquals(updatedRecipe.getTitle(), result.getBody().getTitle());
		assertEquals(updatedRecipe.getContent(), result.getBody().getContent());
	}

	@Test
	@DisplayName("게시물 수정 실패: 작성자가 아님")
	void testEditRecipeFailureNotAuthor() {
		Long recipeId = 1L;
		User anotherUser = new User("otheruser", "password", "Other User", "other@example.com", "Hello");

		given(recipeRepository.findById(any(Long.class))).willReturn(Optional.of(existingRecipe));

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			recipeService.editRecipe(recipeId, requestDto, anotherUser);
		});

		assertEquals("작성자만 접근할 수 있습니다.", exception.getMessage());
	}

	@Test
	@DisplayName("게시물 삭제")
	void testDeleteRecipe() {
		Long recipeId = 1L;
		ResponseEntity<String> responseEntity = ResponseEntity.status(HttpStatus.OK).body(recipeId + " 번 삭제 완료");

		given(recipeRepository.findById(any(Long.class))).willReturn(Optional.of(existingRecipe));

		ResponseEntity<String> result = recipeService.deleteRecipe(recipeId, user);

		assertNotNull(result);
		assertEquals(responseEntity.getStatusCode(), result.getStatusCode());
		assertEquals(responseEntity.getBody(), result.getBody());
	}

	@Test
	@DisplayName("게시물 삭제 실패: 작성자가 아님")
	void testDeleteRecipeFailureNotAuthor() {
		Long recipeId = 1L;
		User anotherUser = new User("otheruser", "password", "Other User", "other@example.com", "Hello");

		given(recipeRepository.findById(any(Long.class))).willReturn(Optional.of(existingRecipe));

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			recipeService.deleteRecipe(recipeId, anotherUser);
		});

		assertEquals("작성자만 접근할 수 있습니다.", exception.getMessage());
	}

	@Test
	@DisplayName("모든 게시물 조회")
	void testGetAllRecipe() {
		List<Recipe> recipeList = List.of(
			new Recipe(1L, user, "Recipe 1", "Content 1", 0L),
			new Recipe(2L, user, "Recipe 2", "Content 2", 0L)
		);
		Page<Recipe> recipePage = new PageImpl<>(recipeList);

		given(recipeRepository.findAll(any(Pageable.class))).willReturn(recipePage);

		ResponseEntity responseEntity = recipeService.getAllRecipe(0, "createdAt");

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
	}

	@Test
	@DisplayName("모든 게시물 조회 - 데이터 없음")
	void testGetAllRecipeNoData() {
		int page = 0;
		String sortBy = "createdAt";

		given(recipeRepository.findAll(any(Pageable.class))).willReturn(Page.empty());

		ResponseEntity responseEntity = recipeService.getAllRecipe(page, sortBy);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertTrue(responseEntity.getBody() instanceof String);
		assertEquals("먼저 작성하여 소식을 알려보세요!", responseEntity.getBody());
	}

	@Test
	@DisplayName("날짜 범위에 따른 게시물 조회")
	void testGetDateRecipe() {
		int page = 0;
		String startDate = "20230601";
		String endDate = "20230630";

		LocalDateTime startDateTime = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyyMMdd")).atTime(0, 0, 0);
		LocalDateTime endDateTime = LocalDate.parse(endDate, DateTimeFormatter.ofPattern("yyyyMMdd")).atTime(23, 59, 59);

		List<Recipe> recipeList = List.of(
			new Recipe(1L, user, "Recipe 1", "Content 1", 0L),
			new Recipe(2L, user, "Recipe 2", "Content 2", 0L)
		);
		Page<Recipe> recipePage = new PageImpl<>(recipeList);

		given(recipeRepository.findAllByCreatedAtBetween(any(Pageable.class), any(LocalDateTime.class), any(LocalDateTime.class)))
			.willReturn(recipePage);

		ResponseEntity responseEntity = recipeService.getDateRecipe(page, startDate, endDate);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
	}

	@Test
	@DisplayName("날짜 범위에 따른 게시물 조회 - 데이터 없음")
	void testGetDateRecipeNoData() {
		int page = 0;
		String startDate = "20230601";
		String endDate = "20230630";

		LocalDateTime startDateTime = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyyMMdd")).atTime(0, 0, 0);
		LocalDateTime endDateTime = LocalDate.parse(endDate, DateTimeFormatter.ofPattern("yyyyMMdd")).atTime(23, 59, 59);

		given(recipeRepository.findAllByCreatedAtBetween(any(Pageable.class), any(LocalDateTime.class), any(LocalDateTime.class)))
			.willReturn(Page.empty());

		ResponseEntity responseEntity = recipeService.getDateRecipe(page, startDate, endDate);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertTrue(responseEntity.getBody() instanceof String);
		assertEquals("먼저 작성하여 소식을 알려보세요!", responseEntity.getBody());
	}

}
