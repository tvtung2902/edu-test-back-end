package com.javaweb.edutest.repository;

import com.javaweb.edutest.dto.response.GroupResponseDTOWithCount;
import com.javaweb.edutest.model.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> findByOwner_Id(long ownerId);

    int countByNameContainingIgnoreCase(String searchName);

    @Query("""
        SELECT g.id AS id,
        g.name AS name,
        g.description AS description,
        g.code AS code,
        g.image AS image,
        g.createdAt as createdAt,
        SIZE(g.tests) AS testsCount,
        SIZE(g.groupUsers) AS memberCount
        FROM Group g
        WHERE LOWER(g.name) LIKE LOWER(CONCAT('%', :searchName, '%'))
        ORDER BY g.id DESC""")
    Page<GroupResponseDTOWithCount> findGroups(String searchName, Pageable pageable);

    @Query("""
        SELECT g.id AS id,
        g.name AS name,
        g.description AS description,
        g.code AS code,
        g.image AS image,
        g.createdAt as createdAt,
        SIZE(g.groupUsers) AS memberCount,
        SIZE(g.tests) AS testsCount
        FROM Group g
        WHERE g.id = id
        """)
    Optional<GroupResponseDTOWithCount> findGroupById(@Param("id") long id);
}