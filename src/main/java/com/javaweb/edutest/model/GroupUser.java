package com.javaweb.edutest.model;

import com.javaweb.edutest.enums.UserGroupStatus;
import com.javaweb.edutest.model.compositekey.GroupUserPK;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "group_user")
public class GroupUser extends AbstractEntity {
    @EmbeddedId
    private GroupUserPK id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("groupId")
    @JoinColumn(name = "group_id")
    private Group group;

    @Enumerated(EnumType.STRING)
    private UserGroupStatus userGroupStatus;
}
