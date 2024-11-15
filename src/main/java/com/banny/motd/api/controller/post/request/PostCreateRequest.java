package com.banny.motd.api.controller.post.request;

import com.banny.motd.api.service.post.request.PostCreateServiceRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.List;

@Getter
public class PostCreateRequest {

    // todo: validator 추가
    private List<String> imageUrls;

    @NotBlank(message = "내용을 입력해주세요.")
    @Size(min = 1, max = 2_000, message = "내용은 1자 이상 2,000자 이하로 입력해주세요.")
    private String content;

    public PostCreateServiceRequest toServiceRequest() {
        return PostCreateServiceRequest.builder()
                .imageUrls(imageUrls)
                .content(content)
                .build();
    }

}
