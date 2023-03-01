package com.joshualog.api.repository;

import static com.joshualog.api.domain.QPost.post;

import com.joshualog.api.domain.Post;
import com.joshualog.api.request.PostSearch;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public List<Post> getList(PostSearch postSearch) {
        return jpaQueryFactory.selectFrom(post)
                .limit(postSearch.getSize())
                .offset(postSearch.getOffset())
                .orderBy(post.id.desc())
                .fetch();
    }
}
