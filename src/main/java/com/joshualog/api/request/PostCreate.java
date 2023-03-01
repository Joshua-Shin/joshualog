package com.joshualog.api.request;

import com.joshualog.api.exception.InvalidException;
import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter @Getter
public class PostCreate {
    @NotBlank(message = "타이틀이 필요합니다.")
    private String title;
    @NotBlank(message = "내용이 필요합니다.")
    private String content;
    @Builder
    public PostCreate(String title, String content) {
        this.title = title;
        this.content = content;
    }
    public void validate() {
        if(title.contains("바보")) {
            throw new InvalidException("title", "제목에 바보가 포함될 수 없습니다.");
        }
    }
}
