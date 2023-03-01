package com.joshualog.api.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joshualog.api.domain.Post;
import com.joshualog.api.repository.PostRepository;
import com.joshualog.api.request.PostCreate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.shinlog.com", uriPort = 443)
@AutoConfigureMockMvc
@ExtendWith(RestDocumentationExtension.class)
public class PostControllerDocTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @DisplayName("글 단건 조회")
    void test1() throws Exception {
        // given
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();
        postRepository.save(post);

        // expected
        this.mockMvc.perform(get("/posts/{postId}", 1L)
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("post-inquiry",
                        pathParameters(
                                parameterWithName("postId").description("게시판ID")),
                        responseFields(
                                fieldWithPath("id").description("게시글ID"),
                                fieldWithPath("title").description("제목"),
                                fieldWithPath("content").description("내용"))
                        ));
    }

    @Test
    @DisplayName("글 작성")
    void test2() throws Exception {
        PostCreate postCreate = PostCreate.builder()
                .title("제목")
                .content("내용")
                .build();
        String json = objectMapper.writeValueAsString(postCreate);

        // expected
        this.mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("post-create", requestFields(
                                fieldWithPath("title").description("제목"),
                                fieldWithPath("content").description("내용").optional())
                ));
    }
}
