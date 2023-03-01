package com.joshualog.api.controller;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joshualog.api.domain.Post;
import com.joshualog.api.repository.PostRepository;
import com.joshualog.api.request.PostCreate;
import com.joshualog.api.request.PostEdit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

//@WebMvcTest
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PostRepository postRepository;

    @Test
    @DisplayName("/posts 요청")
    void test() throws Exception {
        PostCreate postCreate = PostCreate.builder()
                .title("글 제목입니다.")
                .content("글 내용입니다 하하.")
                .build();
        String json = objectMapper.writeValueAsString(postCreate);

        // expected
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(content().string(""))
                .andDo(print());
    }
    @Test
    @DisplayName("/posts 요청시 title 값은 필수다.")
    void test2() throws Exception {
        PostCreate postCreate = PostCreate.builder()
                .content("글 내용입니다 하하.")
                .build();
        String json = objectMapper.writeValueAsString(postCreate);
        // expected
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.title").value("타이틀이 필요합니다."))
                .andDo(print());
    }
    @Test
    @DisplayName("/posts 요청시 DB에 값이 저장된다.")
    void test3() throws Exception {
        PostCreate postCreate = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();
        String json = objectMapper.writeValueAsString(postCreate);
        // when
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());
        // then
        assertEquals(1L,postRepository.count());
        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.", post.getTitle());
        assertEquals("내용입니다.", post.getContent());

    }
    @Test
    @DisplayName("글 1개 조회")
    void test4() throws Exception {
        // given
        Post post = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(post);

        // expected
        mockMvc.perform(get("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.title").value(post.getTitle()))
                .andExpect(jsonPath("$.content").value(post.getContent()))
                .andDo(print());
    }
    @Test
    @DisplayName("글 여러개 조회")
    void test5() throws Exception {
        // given
        List<Post> requestPosts = IntStream.range(0, 20)
                .mapToObj(i -> Post.builder()
                        .title("foo" + i)
                        .content("bar" + i)
                        .build())
                .collect(Collectors.toList());
        postRepository.saveAll(requestPosts);

        // expected
        mockMvc.perform(get("/posts?page=1&size=5")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(5)))
                .andExpect(jsonPath("$[0].title").value("foo19"))
                .andExpect(jsonPath("$[0].content").value("bar19"))
                .andDo(print());
    }
    @Test
    @DisplayName("페이지를 0으로 입력했을때 첫 페이지를 가져온다")
    void test6() throws Exception {
        // given
        List<Post> requestPosts = IntStream.range(0, 20)
                .mapToObj(i -> Post.builder()
                        .title("foo" + i)
                        .content("bar" + i)
                        .build())
                .collect(Collectors.toList());
        postRepository.saveAll(requestPosts);

        // expected
        mockMvc.perform(get("/posts?page=0&size=5")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(5)))
                .andExpect(jsonPath("$[0].title").value("foo19"))
                .andExpect(jsonPath("$[0].content").value("bar19"))
                .andDo(print());
    }
    @Test
    @DisplayName("글 수정")
    void test7() throws Exception {
        // given
        Post post = Post.builder()
                .title("신중혁")
                .content("짱짱맨")
                .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("변서율")
                .content("짱짱걸")
                .build();

        // expected
        mockMvc.perform(patch("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit)))
                .andExpect(status().isOk())
                .andDo(print());
    }
    @Test
    @DisplayName("글 삭제")
    void test8() throws Exception {
        // given
        Post post = Post.builder()
                .title("신중혁")
                .content("짱짱맨")
                .build();
        postRepository.save(post);

        // expected
        mockMvc.perform(delete("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
    @Test
    @DisplayName("존재하지 않는 글 조회")
    void test9() throws Exception {
        // expected
        mockMvc.perform(get("/posts/{postId}", 1L)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
    @Test
    @DisplayName("존재하지 않는 글 수정")
    void test10() throws Exception {
        // given
        PostEdit postEdit = PostEdit.builder()
                .title("변서율")
                .content("짱짱걸")
                .build();
        // expected
        mockMvc.perform(patch("/posts/{postId}", 1L)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit))
                )
                .andExpect(status().isNotFound())
                .andDo(print());
    }
    @Test
    @DisplayName("존재하지 않는 글 삭제")
    void test11() throws Exception {
        // expected
        mockMvc.perform(delete("/posts/{postId}", 1L)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
    @Test
    @DisplayName("제목에 바보가 들어갈때 예외 처리")
    void test12() throws Exception {
        PostCreate postCreate = PostCreate.builder()
                .title("나는 바보입니다.")
                .content("내용입니다.")
                .build();
        String json = objectMapper.writeValueAsString(postCreate);

        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}