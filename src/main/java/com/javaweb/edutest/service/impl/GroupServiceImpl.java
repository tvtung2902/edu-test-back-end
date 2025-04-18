package com.javaweb.edutest.service.impl;

import com.javaweb.edutest.dto.request.GroupRequestDTO;
import com.javaweb.edutest.dto.response.GroupResponseDTO;
import com.javaweb.edutest.dto.response.GroupResponseDTOWithCount;
import com.javaweb.edutest.dto.response.PageResponseDTO;
import com.javaweb.edutest.exception.ResourceNotFoundException;
import com.javaweb.edutest.mapper.GroupMapper;
import com.javaweb.edutest.model.Group;
import com.javaweb.edutest.model.Test;
import com.javaweb.edutest.model.User;
import com.javaweb.edutest.repository.GroupRepository;
import com.javaweb.edutest.repository.TestRepository;
import com.javaweb.edutest.repository.UserRepository;
import com.javaweb.edutest.service.CloudinaryService;
import com.javaweb.edutest.service.GroupService;
import com.javaweb.edutest.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class GroupServiceImpl implements GroupService {

    private final GroupMapper groupMapper;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final TestRepository testRepository;
    private final CloudinaryService cloudinaryService;

    @Override
    public PageResponseDTO<GroupResponseDTOWithCount> getGroups(String name, int pageNo, int pageSize) {
        int totalRecords = groupRepository.countByNameContainingIgnoreCase(name);
        Pageable pageable = PaginationUtil.createPageable(pageNo, pageSize, totalRecords);
        Page<GroupResponseDTOWithCount> groupResponseDTOs = groupRepository.findGroups(name, pageable);
        return PaginationUtil.toPageResponse(groupResponseDTOs);
    }

    @Override
    public GroupResponseDTO getGroup(long groupId) {
        return groupMapper.toGroupResponseDTO(findGroupById(groupId));
    }

    @Override
    public List<GroupResponseDTO> getGroupsOfUser(long userId) {
        return groupMapper.toGroupResponseDTOs(groupRepository.findByOwner_Id(userId));
    }

    @Override
    public long addGroup(GroupRequestDTO groupRequestDTO, MultipartFile image) throws IOException {
        Group newGroup = groupMapper.toGroup(groupRequestDTO);
        try {
            String imageUrl = cloudinaryService.uploadFile(image);
            newGroup.setImage(imageUrl);
        } finally {
            newGroup = groupRepository.save(newGroup);
        }
        return newGroup.getId();
    }

    @Override
    public void addMembersToGroup(long groupId,  Map<String, List<Long>> request) {
        List<Long> membersIds = request.get("memberIds");
        Group currentGroup = findGroupById(groupId);
        var memberInGroups = currentGroup.getMembers();
        var users = new HashSet<User>();
        membersIds.forEach(membersId -> users.add(findUserById(membersId)));
        currentGroup.getMembers().addAll(users);
        memberInGroups.addAll(users);
    }

    @Override
    public void addTestsToGroup(long groupId, Map<String, List<Long>> request) {
        List<Long> testsIds = request.get("testIds");
        Group currentGroup = findGroupById(groupId);
        var testsInGroups = currentGroup.getTests();
        var tests = new HashSet<Test>();
        testsIds.forEach(testId -> tests.add(findTestById(testId)));
        currentGroup.getTests().addAll(tests);
        testsInGroups.addAll(tests);
    }

    @Override
    public String updateGroup(long groupId, GroupRequestDTO groupRequestDTO, MultipartFile image) throws IOException {
        Group currentGroup = findGroupById(groupId);
        try{
            if(groupRequestDTO.isChangedImg()){
                cloudinaryService.deleteFile(currentGroup.getImage());
                String imageUrl = cloudinaryService.uploadFile(image);
                currentGroup.setImage(imageUrl);
            }
        } finally {
            groupMapper.updateGroup(currentGroup, groupRequestDTO);
            groupRepository.save(currentGroup);
        }
        return currentGroup.getImage();
    }

    @Override
    public void updateMembersInGroup(long groupId, Map<String, List<Long>> request) {
        List<Long> membersIds = request.get("memberIds");
        Group currentGroup = findGroupById(groupId);
        var membersInGroups = currentGroup.getMembers();
        var users = new HashSet<User>();
        membersIds.forEach(membersId -> users.add(findUserById(membersId)));
        currentGroup.setMembers(users);
        membersInGroups.addAll(users);
    }

    @Override
    public void updateTestsInGroup(long groupId, Map<String, List<Long>> request) {
        List<Long> testsIds = request.get("testIds");
        Group currentGroup = findGroupById(groupId);
        var testsInGroups = currentGroup.getTests();
        var tests = new HashSet<Test>();
        testsIds.forEach(testsId -> tests.add(findTestById(testsId)));
        currentGroup.setTests(tests);
        testsInGroups.addAll(tests);
    }

    @Override
    public void deleteGroup(long groupId) throws IOException {
        Group group = findGroupById(groupId);
        try {
            String imageUrl = group.getImage();
            cloudinaryService.deleteFile(imageUrl);
        } finally {
            groupRepository.deleteById(groupId);
        }
    }

    private Group findGroupById(long groupId) {
        return groupRepository.findById(groupId).orElseThrow(
                () -> new ResourceNotFoundException("group not found with id: " + groupId)
        );
    }

    private User findUserById(long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("group not found with id: " + userId)
        );
    }

    private Test findTestById(long testId) {
        return testRepository.findById(testId).orElseThrow(
                () -> new ResourceNotFoundException("test not found with id: " + testId)
        );
    }
}