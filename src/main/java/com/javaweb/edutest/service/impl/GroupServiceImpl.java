package com.javaweb.edutest.service.impl;

import com.javaweb.edutest.dto.request.TestGroupRequestDTO;
import com.javaweb.edutest.dto.request.GroupRequestDTO;
import com.javaweb.edutest.dto.request.UserAddToGroupRequestDTO;
import com.javaweb.edutest.dto.request.UserGroupRequestDTO;
import com.javaweb.edutest.dto.response.*;
import com.javaweb.edutest.enums.TestGroupStatus;
import com.javaweb.edutest.enums.UserGroupStatus;
import com.javaweb.edutest.exception.ResourceNotFoundException;
import com.javaweb.edutest.mapper.GroupMapper;
import com.javaweb.edutest.mapper.UserMapper;
import com.javaweb.edutest.model.Group;
import com.javaweb.edutest.model.GroupUser;
import com.javaweb.edutest.model.Test;
import com.javaweb.edutest.model.User;
import com.javaweb.edutest.model.compositekey.GroupUserPK;
import com.javaweb.edutest.repository.GroupRepository;
import com.javaweb.edutest.repository.SearchTestsRepository;
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
    private final SearchTestsRepository searchTestsRepository;
    private final UserMapper userMapper;

    @Override
    public PageResponseDTO<GroupResponseDTOWithCount> getGroups(String name, int pageNo, int pageSize) {
        int totalRecords = groupRepository.countByNameContainingIgnoreCase(name);
        Pageable pageable = PaginationUtil.createPageable(pageNo, pageSize, totalRecords);
        Page<GroupResponseDTOWithCount> groupResponseDTOs = groupRepository.findGroups(name, pageable);
        return PaginationUtil.toPageResponse(groupResponseDTOs);
    }

    @Override
    public GroupResponseDTOWithCount getGroup(long groupId) {
        return groupRepository.findGroupById(groupId).orElseThrow(
                () -> new ResourceNotFoundException("not found group with id: " + groupId)
        );
    }

    @Override
    public List<GroupResponseDTO> getGroupsOfUser(long userId) {
        return groupMapper.toGroupResponseDTOs(groupRepository.findByOwner_Id(userId));
    }

    @Override
    public PageResponseDTO<TestGroupResponseDTO> getTestsOfGroup(
            long groupId, int pageNo, int pageSize, String searchName, TestGroupStatus status) {
        return searchTestsRepository.getTestsOfGroup(groupId, pageNo, pageSize, searchName, status);
    }

    @Override
    public PageResponseDTO<UserResponseDTO> getUsersOfGroup(long groupId, String search, int pageNo,
                                                            int pageSize, UserGroupStatus status) {

        long totalRecord = userRepository.countUsersInGroupByName(search, groupId);
        Pageable pageable = PaginationUtil.createPageable(pageNo, pageSize, totalRecord);
        Page<User> users = userRepository.findByNameAndGroupId(search, groupId, pageable);
        Page<UserResponseDTO> userResponseDTOs = users.map(userMapper::mapToUserDto);
        return PaginationUtil.toPageResponse(userResponseDTOs);
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
    public void addMembersToGroup(long groupId, UserAddToGroupRequestDTO request) {
        List<String> emails = request.getEmails();
        Group currentGroup = findGroupById(groupId);

        for (String email : emails) {
            User user = userRepository.findByEmail(email).orElseThrow(
                    () -> new ResourceNotFoundException("User not found with email: " + email)
            );

            GroupUserPK groupUserPK = new GroupUserPK(user.getId(), groupId);

            boolean userExistsInGroup = currentGroup.getGroupUsers().stream()
                    .anyMatch(gu -> gu.getId().equals(groupUserPK));

            if (userExistsInGroup) {
                continue;
            }

            GroupUser newGroupUser = new GroupUser();
            newGroupUser.setId(groupUserPK);
            newGroupUser.setUser(user);
            newGroupUser.setGroup(currentGroup);
            newGroupUser.setUserGroupStatus(UserGroupStatus.JOINED);

            currentGroup.getGroupUsers().add(newGroupUser);
        }
    }


    @Override
    public void addTestsToGroup(long groupId, TestGroupRequestDTO testInGroupRequestDTO) {
        List<Long> testsIds = testInGroupRequestDTO.getTestIds();
        Group currentGroup = findGroupById(groupId);
        for (Long testId : testsIds) {
            Test test = findTestById(testId);
            currentGroup.getTests().add(test);
        }
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
    public void deleteMembersInGroup(long groupId, UserGroupRequestDTO request) {
        List<Long> userIds = request.getUsers();
        Group currentGroup = findGroupById(groupId);

        for (Long userId : userIds) {
            GroupUserPK groupUserPK = new GroupUserPK(userId, groupId);

            GroupUser groupUser = currentGroup.getGroupUsers().stream()
                    .filter(gu -> gu.getId().equals(groupUserPK))
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("User not found in this group"));

            currentGroup.getGroupUsers().remove(groupUser);
        }
    }

    @Override
    public void deleteTestsInGroup(long groupId, TestGroupRequestDTO deleteTestInGroupRequestDTO) {
        List<Long> testsIds = deleteTestInGroupRequestDTO.getTestIds();
        Group currentGroup = findGroupById(groupId);
        for (Long testId : testsIds) {
            Test test = findTestById(testId);
            currentGroup.getTests().remove(test);
        }
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