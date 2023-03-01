package com.joshualog.api.domain;

import com.joshualog.api.request.PostEdit;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Lob
    private String content;
    @Builder
    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }
    public void edit(PostEdit postEdit) {
        title = postEdit.getTitle() == null ? title : postEdit.getTitle();
        content = postEdit.getContent() == null ? content : postEdit.getContent();
    }
}
