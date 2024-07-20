package com.lawProject.SSL.domain.chatmessage.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto implements Serializable {
    private static final long serialVersionUID = 2082503192322391880L;
    @NotNull
    private String roomId;
    @NotNull
    private Long senderId;
    @NotBlank
    private String content;

    private String senderType;

    public void setContent(String content) {
        this.content = content;
    }
}