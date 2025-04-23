package com.javaweb.edutest.repository;

import com.javaweb.edutest.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("SELECT COUNT(gu.user) FROM GroupUser gu WHERE LOWER(gu.user.name) LIKE LOWER(CONCAT('%', :name, '%')) AND gu.group.id = :groupId")
    long countUsersInGroupByName(@Param("name") String name, @Param("groupId") long groupId);

    @Query("SELECT gu.user FROM GroupUser gu WHERE LOWER(gu.user.name) LIKE LOWER(CONCAT('%', :name, '%')) AND gu.group.id = :groupId")
    Page<User> findByNameAndGroupId(@Param("name") String name, @Param("groupId") Long groupId,
                                    Pageable pageable);
}