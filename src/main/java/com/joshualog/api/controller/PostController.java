package com.joshualog.api.controller;

import com.joshualog.api.request.PostCreate;
import com.joshualog.api.request.PostEdit;
import com.joshualog.api.request.PostSearch;
import com.joshualog.api.response.PostResponse;
import com.joshualog.api.service.PostService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 글 작성
    @PostMapping("/posts")
    public void post(@RequestBody @Valid PostCreate request) {
        request.validate();
        postService.write(request);
    }
    // 글 단건 조회
    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable Long postId) {
        return postService.get(postId);
    }
    // 글 목록 조회
    @GetMapping("/posts")
    public List<PostResponse> getList(@ModelAttribute PostSearch postSearch) {
        return postService.getList(postSearch);
    }
    // 글 수정
    @PatchMapping("/posts/{postId}")
    public void patch(@PathVariable Long postId, @RequestBody PostEdit postEdit) {
        postService.edit(postId, postEdit);
    }
    // 글 삭제
    @DeleteMapping("/posts/{postId}")
    public void delete(@PathVariable Long postId) {
        postService.delete(postId);
    }
}
