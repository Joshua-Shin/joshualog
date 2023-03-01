package com.joshualog.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.joshualog.api.domain.Post;
import com.joshualog.api.exception.PostNotFound;
import com.joshualog.api.repository.PostRepository;
import com.joshualog.api.request.PostCreate;
import com.joshualog.api.request.PostEdit;
import com.joshualog.api.request.PostSearch;
import com.joshualog.api.response.PostResponse;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class PostServiceTest {
    @Autowired
    private PostService postService;
    @Autowired
    private PostRepository postRepository;
    @Test
    @DisplayName("글 작성")
    void test1() throws Exception {
        // given
        PostCreate postCreate = PostCreate.builder()
                .title("foo")
                .content("bar")
                .build();

        // when
        postService.write(postCreate);

        // then
        assertEquals(1L, postRepository.count());
        Post post = postRepository.findAll().get(0);
        assertEquals("foo", post.getTitle());
        assertEquals("bar", post.getContent());
    }
    @Test
    @DisplayName("글 1개 조회")
    void test2() throws Exception {
        // given
        Post requestPost = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(requestPost);

        // when
        PostResponse response = postService.get(requestPost.getId());

        // then
        assertNotNull(response);
        assertEquals("foo", response.getTitle());
        assertEquals("bar", response.getContent());
    }
    @Test
    @DisplayName("제목을 10글자만 반환")
    void test3() throws Exception {
        // given
        Post requestPost = Post.builder()
                .title("123456789012345")
                .content("bar")
                .build();
        postRepository.save(requestPost);

        // when
        PostResponse response = postService.get(requestPost.getId());

        // then
        assertNotNull(response);
        assertEquals("1234567890", response.getTitle());
        assertEquals("bar", response.getContent());
    }
    @Test
    @DisplayName("글 제목 수정")
    void test4() throws Exception {
        // given
        List<Post> requestPosts = IntStream.range(0, 20)
                .mapToObj(i -> Post.builder()
                        .title("foo" + i)
                        .content("bar" + i)
                        .build())
                .collect(Collectors.toList());
        postRepository.saveAll(requestPosts);

        PostSearch postSearch = PostSearch.builder()
                .page(1)
                .build();

        // when
        List<PostResponse> posts = postService.getList(postSearch);

        // then
        assertEquals(10L, posts.size());
        assertEquals("foo19", posts.get(0).getTitle());
    }
    @Test
    @DisplayName("글 수정")
    void test5() throws Exception {
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

        // when
        postService.edit(post.getId(), postEdit);

        // then
        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id= " + post.getId()));

        assertEquals(changedPost.getTitle(), "변서율");
        assertEquals(changedPost.getContent(), "짱짱걸");
    }
    @Test
    @DisplayName("내용은 전달안할 시 글 제목만 수정")
    void test6() throws Exception {
        // given
        Post post = Post.builder()
                .title("신중혁")
                .content("짱짱맨")
                .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("변서율")
                .build();

        // when
        postService.edit(post.getId(), postEdit);

        // then
        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id= " + post.getId()));

        assertEquals(changedPost.getTitle(), "변서율");
        assertEquals(changedPost.getContent(), "짱짱맨");
    }
    @Test
    @DisplayName("글 삭제")
    void test7() throws Exception {
        // given
        Post post = Post.builder()
                .title("신중혁")
                .content("짱짱맨")
                .build();
        postRepository.save(post);
        // when
        postService.delete(post.getId());
        // then
        assertEquals(0, postRepository.count());
    }
    @Test
    @DisplayName("글 조회 - 글이 존재하지 않을 시 예외 처리")
    void test8() throws Exception {
        // given
        Post requestPost = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(requestPost);

        // expected
        assertThrows(PostNotFound.class, () -> postService.get(requestPost.getId() + 1L));
    }
    @Test
    @DisplayName("글 삭제 - 글이 존재하지 않을 시 예외 처리")
    void test9() throws Exception {
        // given
        Post post = Post.builder()
                .title("신중혁")
                .content("짱짱맨")
                .build();
        postRepository.save(post);

        // expected
        assertThrows(PostNotFound.class, () -> postService.delete(post.getId() + 1L));
    }
    @Test
    @DisplayName("글 수정 - 글이 존재 하지 않을 시 예외 처리")
    void test10() throws Exception {
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
        assertThrows(PostNotFound.class, () -> postService.edit(post.getId() + 1L, postEdit));
    }
}