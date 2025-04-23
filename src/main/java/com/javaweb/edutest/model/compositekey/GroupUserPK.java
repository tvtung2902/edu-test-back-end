package com.javaweb.edutest.model.compositekey;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupUserPK {
    private long userId;
    private long groupId;
}