package com.joshualog.api.repository;

import com.joshualog.api.domain.Post;
import com.joshualog.api.request.PostSearch;
import java.util.List;

public interface PostRepositoryCustom {
    List<Post> getList(PostSearch postSearch);
}
