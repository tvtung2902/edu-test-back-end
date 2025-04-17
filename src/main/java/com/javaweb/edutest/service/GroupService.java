package com.javaweb.edutest.service;

import com.javaweb.edutest.dto.request.GroupRequestDTO;
import com.javaweb.edutest.dto.response.GroupResponseDTO;
import com.javaweb.edutest.dto.response.GroupResponseDTOWithCount;
import com.javaweb.edutest.dto.response.PageResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

public interface GroupService {
    PageResponseDTO<GroupResponseDTOWithCount> getGroups(String searchName, int pageNo, int pageSize);
    GroupResponseDTO getGroup(long groupId);
    List<GroupResponseDTO> getGroupsOfUser(long userId);
    long addGroup(GroupRequestDTO groupRequestDTO, MultipartFile image) throws IOException;
    void addMembersToGroup(long groupId, Map<String, List<Long> > request);
    void addTestsToGroup(long groupId, Map<String, List<Long> > request);
    void updateGroup(long groupId, GroupRequestDTO groupRequestDTO);
    void updateMembersInGroup(long groupId, Map<String, List<Long> > request);
    void updateTestsInGroup(long groupId, Map<String, List<Long> > request);
    void deleteGroup(long groupId);
}
