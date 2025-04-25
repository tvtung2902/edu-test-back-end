package com.javaweb.edutest.repository;

import com.javaweb.edutest.model.GroupUser;
import com.javaweb.edutest.model.compositekey.GroupUserPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupUserRepository extends JpaRepository<GroupUser, GroupUserPK> {

}
