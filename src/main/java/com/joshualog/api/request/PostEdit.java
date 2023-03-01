package com.joshualog.api.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PostEdit {
//    @NotBlank(message = "타이틀이 필요합니다.")
    private String title;
//    @NotBlank(message = "내용이 필요합니다.")
    private String content;
    @Builder
    public PostEdit(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
