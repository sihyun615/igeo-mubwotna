package com.sparta.igeomubwotna.mvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.igeomubwotna.config.SecurityConfig;
import com.sparta.igeomubwotna.controller.CommentController;
import com.sparta.igeomubwotna.controller.RecipeController;
import com.sparta.igeomubwotna.dto.CommentRequestDto;
import com.sparta.igeomubwotna.dto.CommentResponseDto;
import com.sparta.igeomubwotna.dto.RecipeRequestDto;
import com.sparta.igeomubwotna.dto.RecipeResponseDto;
import com.sparta.igeomubwotna.entity.Recipe;
import com.sparta.igeomubwotna.entity.User;
import com.sparta.igeomubwotna.security.UserDetailsImpl;
import com.sparta.igeomubwotna.service.CommentService;
import com.sparta.igeomubwotna.service.RecipeService;

@WebMvcTest(  //Controller 테스트 가능
	controllers = {RecipeController.class, CommentController.class},  //테스트할 Controller 지정
	excludeFilters = {  //제외할 것
		@ComponentScan.Filter(
			type = FilterType.ASSIGNABLE_TYPE,
			classes = SecurityConfig.class
		)
	}
) //Security 같이 사용 시 이런 식으로 설정하면 됨

@MockBean(JpaMetamodelMappingContext.class)
class RecipeCommentMvcTest {
	private MockMvc mvc;

	private Principal mockPrincipal;

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean  //RecipeController에서 사용되므로 주입해주기 위한 Bean
	RecipeService recipeService;

	@MockBean  //CommentController에서 사용되므로 주입해주기 위한 Bean
	CommentService commentService;

	@BeforeEach
	public void setup() {
		mvc = MockMvcBuilders.webAppContextSetup(context)
			.apply(springSecurity(new MockSpringSecurityFilter()))  //기존 security filter 취소했으니 새로 만들어준 필터 넣어줌
			.build();
	}

	private User mockUserSetup() {
		// Mock 테스트 유져 생성
		String userId = "123syihyun123";
		String password = "Qkrtlgus11!";
		String name = "sihyun";
		String email = "111lch_n9@df.com";
		String description = "Hi";

		User testUser = new User(userId, password, name, email, description);
		UserDetailsImpl testUserDetails = new UserDetailsImpl(testUser);  //직접 생성
		mockPrincipal = new UsernamePasswordAuthenticationToken(testUserDetails, "", testUserDetails.getAuthorities());    //직접 생성

		return testUser;
	}


	@Test
	@DisplayName("게시물 등록")
	void testSaveRecipe() throws Exception {
		// given
		User user = mockUserSetup();  //회원필요
		String title = "김치볶음밥";
		String content = "~~~~하면 맛있는 김치볶음밥 완성!";
		RecipeRequestDto requestDto = new RecipeRequestDto(title, content);
		RecipeResponseDto responseDto = new RecipeResponseDto(new Recipe(requestDto, user));

		given(recipeService.saveRecipe(any(RecipeRequestDto.class), any(User.class))).willReturn(ResponseEntity.status(HttpStatus.CREATED).body(responseDto));

		String postInfo = objectMapper.writeValueAsString(requestDto);  //클래스를 JSON타입의 String으로 변환

		// when - then
		mvc.perform(post("/api/recipe/")
				.content(postInfo)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.principal(mockPrincipal)
			)
			.andExpect(status().isCreated())  //예측
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))  // 응답 컨텐츠 타입이 JSON인지 확인
			.andExpect(jsonPath("$.title").value(responseDto.getTitle())) // RecipeResponseDto의 필드인 title 값 확인
			.andExpect(jsonPath("$.content").value(responseDto.getContent())) // RecipeResponseDto의 필드인 content 값 확인
			.andExpect(jsonPath("$.userId").value(responseDto.getUserId())) // RecipeResponseDto의 필드인 content 값 확인
			.andDo(print());
	}

	@Test
	@DisplayName("게시물 수정")
	void testEditRecipe() throws Exception {
		// given
		User user = mockUserSetup();  //회원필요
		Long recipeId = 1L;
		String title = "더 맛있는 김치볶음밥";
		String content = "~~~~더하면 더 맛있는 김치볶음밥 완성!!!";
		RecipeRequestDto requestDto = new RecipeRequestDto(title, content);
		RecipeResponseDto responseDto = new RecipeResponseDto(new Recipe(requestDto, user));

		given(recipeService.editRecipe(eq(recipeId), any(RecipeRequestDto.class), any(User.class))).willReturn(ResponseEntity.status(HttpStatus.OK).body(responseDto));

		String postInfo = objectMapper.writeValueAsString(requestDto);  //클래스를 JSON타입의 String으로 변환

		// when - then
		mvc.perform(patch("/api/recipe/{recipeId}", recipeId)
				.content(postInfo)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.principal(mockPrincipal)
			)
			.andExpect(status().isOk())  //예측
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))  // 응답 컨텐츠 타입이 JSON인지 확인
			.andExpect(jsonPath("$.title").value(responseDto.getTitle())) // RecipeResponseDto의 필드인 title 값 확인
			.andExpect(jsonPath("$.content").value(responseDto.getContent())) // RecipeResponseDto의 필드인 content 값 확인
			.andExpect(jsonPath("$.userId").value(responseDto.getUserId())) // RecipeResponseDto의 필드인 content 값 확인
			.andDo(print());
	}

	@Test
	@DisplayName("게시물 삭제")
	void testDeleteRecipe() throws Exception {
		// given
		User user = mockUserSetup();  //회원필요
		Long recipeId = 1L;

		given(recipeService.deleteRecipe(eq(recipeId), any(User.class))).willReturn(ResponseEntity.status(HttpStatus.OK).body(recipeId + " 번 삭제 완료"));

		// when - then
		mvc.perform(delete("/api/recipe/{recipeId}", recipeId)
				.principal(mockPrincipal)
			)
			.andExpect(status().isOk())  //예측
			.andExpect(content().string(recipeId + " 번 삭제 완료"))
			.andDo(print());
	}

	@Test
	@DisplayName("게시물 조회")
	void testGetRecipe() throws Exception {
		// given
		User user = mockUserSetup();  //회원필요
		Long recipeId = 1L;
		String title = "김치볶음밥";
		String content = "~~~~하면 맛있는 김치볶음밥 완성!";
		Recipe recipe = new Recipe(recipeId, user, title, content, 0L);
		RecipeResponseDto responseDto = new RecipeResponseDto(recipe);

		given(recipeService.getRecipe(eq(recipeId))).willReturn(ResponseEntity.status(HttpStatus.OK).body(responseDto));

		// when - then
		mvc.perform(get("/api/recipe/{recipeId}", recipeId)
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isOk())  //예측
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))  // 응답 컨텐츠 타입이 JSON인지 확인
			.andExpect(jsonPath("$.title").value(responseDto.getTitle())) // RecipeResponseDto의 필드인 title 값 확인
			.andExpect(jsonPath("$.content").value(responseDto.getContent())) // RecipeResponseDto의 필드인 content 값 확인
			.andExpect(jsonPath("$.userId").value(responseDto.getUserId())) // RecipeResponseDto의 필드인 content 값 확인
			.andDo(print());
	}


	@Test
	@DisplayName("레시피 전체 조회")
	void testGetAllRecipe() throws Exception {
		// given
		User user = mockUserSetup();
		int page = 1;
		String sortBy = "createdAt";
		Recipe recipe1 = new Recipe(1L, user, "김치볶음밥", "맛있는 김치볶음밥 만드는 법", 0L);
		Recipe recipe2 = new Recipe(2L, user, "된장찌개", "맛있는 된장찌개 만드는 법",0L);

		List<Recipe> recipes = Arrays.asList(recipe1, recipe2);
		Page<Recipe> recipePage = new PageImpl<>(recipes, PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.DESC, sortBy)), recipes.size());

		given(recipeService.getAllRecipe(anyInt(), anyString())).willReturn(ResponseEntity.status(HttpStatus.OK).body(recipePage.map(RecipeResponseDto::new)));

		// when - then
		mvc.perform(get("/api/recipe/")
				.param("page", String.valueOf(page))
				.param("sortBy", sortBy)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isOk())  // 예상 상태 코드
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.content[0].title").value("김치볶음밥"))  // 첫 번째 레시피의 title 값 확인
			.andExpect(jsonPath("$.content[1].title").value("된장찌개"))  // 두 번째 레시피의 title 값 확인
			.andExpect(jsonPath("$.content[0].content").value("맛있는 김치볶음밥 만드는 법"))  // 첫 번째 레시피의 content 값 확인
			.andExpect(jsonPath("$.content[1].content").value("맛있는 된장찌개 만드는 법"))  // 두 번째 레시피의 content 값 확인
			.andExpect(jsonPath("$.content[0].userId").value(user.getUserId()))  // 첫 번째 레시피의 userId 값 확인
			.andDo(print());
	}


	@Test
	@DisplayName("날짜 범위에 따른 레시피 조회")
	void testGetDateRecipe() throws Exception {
		// given
		User user = mockUserSetup();
		int page = 1;
		String startDate = "2024-06-16";
		String endDate = "2024-06-20";

		Recipe recipe1 = new Recipe(1L, user, "김치볶음밥", "맛있는 김치볶음밥 만드는 법", 0L);
		Recipe recipe2 = new Recipe(2L, user, "된장찌개", "맛있는 된장찌개 만드는 법",0L);

		List<Recipe> recipes = Arrays.asList(recipe1, recipe2);
		Page<Recipe> recipePage = new PageImpl<>(recipes, PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.DESC, "createdAt")), recipes.size());

		given(recipeService.getDateRecipe(anyInt(), anyString(), anyString())).willReturn(ResponseEntity.status(HttpStatus.OK).body(recipePage.map(RecipeResponseDto::new)));

		// when - then
		mvc.perform(MockMvcRequestBuilders.get("/api/recipe/date/")
				.param("page", String.valueOf(page))
				.param("startdate", startDate)
				.param("enddate", endDate)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isOk())  // 예상 상태 코드
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.content[0].title").value("김치볶음밥"))  // 첫 번째 레시피의 title 값 확인
			.andExpect(jsonPath("$.content[1].title").value("된장찌개"))  // 두 번째 레시피의 title 값 확인
			.andExpect(jsonPath("$.content[0].content").value("맛있는 김치볶음밥 만드는 법"))  // 첫 번째 레시피의 content 값 확인
			.andExpect(jsonPath("$.content[1].content").value("맛있는 된장찌개 만드는 법"))  // 두 번째 레시피의 content 값 확인
			.andExpect(jsonPath("$.content[0].userId").value(user.getUserId()))  // 첫 번째 레시피의 userId 값 확인
			.andDo(print());
	}

	@Test
	@DisplayName("댓글 등록")
	void testCreateComment() throws Exception {
		// given
		User user = mockUserSetup();  //회원필요
		Long recipeId = 1L;

		String content = "김치볶음밥 맛있겠다~~";
		CommentRequestDto requestDto = new CommentRequestDto(content);

		given(commentService.createComment(any(CommentRequestDto.class), eq(recipeId), any(User.class))).willReturn(ResponseEntity.status(HttpStatus.OK).body("comment가 등록되었습니다."));

		String postInfo = objectMapper.writeValueAsString(requestDto);  //클래스를 JSON타입의 String으로 변환

		// when - then
		mvc.perform(post("/api/recipe/{recipeId}/comment", recipeId)
				.content(postInfo)
				.contentType(MediaType.APPLICATION_JSON)
				.principal(mockPrincipal)
			)
			.andExpect(status().isOk())  //예측
			.andExpect(content().string("comment가 등록되었습니다."))
			.andDo(print());

	}


	@Test
	@DisplayName("댓글 수정")
	void testUpdateComment() throws Exception {
		// given
		this.mockUserSetup();  //회원필요
		Long recipeId = 1L;
		Long commentId = 1L;
		String content = "이 김치볶음밥 별로...";
		CommentRequestDto requestDto = new CommentRequestDto(content);

		given(commentService.updateComment(eq(recipeId), eq(commentId), any(CommentRequestDto.class), any(User.class))).willReturn(ResponseEntity.status(HttpStatus.OK).body("comment가 수정되었습니다."));

		String postInfo = objectMapper.writeValueAsString(requestDto);  //클래스를 JSON타입의 String으로 변환

		// when - then
		mvc.perform(patch("/api/recipe/{recipeId}/comment/{commentId}", recipeId, commentId)
				.content(postInfo)
				.contentType(MediaType.APPLICATION_JSON)
				.principal(mockPrincipal)
			)
			.andExpect(status().isOk())  //예측
			.andExpect(content().string("comment가 수정되었습니다."))  // 응답 내용 확인
			.andDo(print());
	}

	@Test
	@DisplayName("댓글 삭제")
	void testDeleteComment() throws Exception {
		// given
		User user = mockUserSetup();  //회원필요
		Long recipeId = 1L;
		Long commentId = 1L;

		doNothing().when(commentService).deleteComment(eq(recipeId), eq(commentId), any(User.class));

		// when - then
		mvc.perform(delete("/api/recipe/{recipeId}/comment/{commentId}", recipeId, commentId)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.principal(mockPrincipal)
			)
			.andExpect(status().isOk())  //예측
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))  // 응답 컨텐츠 타입이 JSON인지 확인
			.andExpect(jsonPath("$.statusCode").value(HttpStatus.OK.value()))  // Response 객체의 statusCode 필드 값이 200인지 확인
			.andExpect(jsonPath("$.message").value("댓글이 삭제되었습니다"))  // Response 객체의 message 필드 값 확인
			.andDo(print());

	}


	@Test
	@DisplayName("댓글 조회")
	void testGetComment() throws Exception {
		// given
		User user = mockUserSetup();  //회원필요
		Long recipeId = 1L;
		CommentResponseDto responseDto = new CommentResponseDto(Long.valueOf(1L), "맛있겠다~~", user.getUserId(), LocalDateTime.now(), Long.valueOf(0L));
		List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
		commentResponseDtoList.add(responseDto);

		given(commentService.getComment(anyLong())).willReturn(commentResponseDtoList);

		// when - then
		mvc.perform(get("/api/recipe/{recipeId}/comment", recipeId)
				.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isOk())  // 예상 상태 코드
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))  // 응답 컨텐츠 타입이 JSON인지 확인
			.andExpect(jsonPath("$[0].id").value(responseDto.getId())) // 첫 번째 댓글의 id 확인
			.andExpect(jsonPath("$[0].content").value(responseDto.getContent())) // 첫 번째 댓글의 content 확인
			.andExpect(jsonPath("$[0].userId").value(user.getUserId())) // 첫 번째 댓글의 userId 확인
			.andExpect(jsonPath("$[0].likeCount").value(responseDto.getLikeCount())) // 첫 번째 댓글의 likeCount 확인
			.andDo(print());

	}

}