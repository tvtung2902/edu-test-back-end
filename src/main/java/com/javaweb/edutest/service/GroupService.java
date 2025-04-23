package com.javaweb.edutest.service;

import com.javaweb.edutest.dto.request.TestGroupRequestDTO;
import com.javaweb.edutest.dto.request.GroupRequestDTO;
import com.javaweb.edutest.dto.request.UserAddToGroupRequestDTO;
import com.javaweb.edutest.dto.request.UserGroupRequestDTO;
import com.javaweb.edutest.dto.response.*;
import com.javaweb.edutest.enums.TestGroupStatus;
import com.javaweb.edutest.enums.UserGroupStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

public interface GroupService {
    PageResponseDTO<GroupResponseDTOWithCount> getGroups(String searchName, int pageNo, int pageSize);
    GroupResponseDTOWithCount getGroup(long groupId);
    List<GroupResponseDTO> getGroupsOfUser(long userId);
    PageResponseDTO<TestGroupResponseDTO> getTestsOfGroup(long groupId, int pageNo, int pageSize,
                                                          String searchName, TestGroupStatus status);
    PageResponseDTO<UserResponseDTO> getUsersOfGroup(long groupId, String search, int pageNo, int pageSize, UserGroupStatus status);
    long addGroup(GroupRequestDTO groupRequestDTO, MultipartFile image) throws IOException;
    void addMembersToGroup(long groupId, UserAddToGroupRequestDTO request);
    void addTestsToGroup(long groupId, TestGroupRequestDTO testInGroupRequestDTO);
    String updateGroup(long groupId, GroupRequestDTO groupRequestDTO, MultipartFile image) throws IOException;
    void deleteMembersInGroup(long groupId, UserGroupRequestDTO request);
    void deleteTestsInGroup(long groupId, TestGroupRequestDTO deleteTestInGroupRequestDTO);
    void deleteGroup(long groupId) throws IOException;
}
