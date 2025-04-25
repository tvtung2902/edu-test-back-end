package com.javaweb.edutest.controller;

import com.javaweb.edutest.dto.request.TestGroupRequestDTO;
import com.javaweb.edutest.dto.request.GroupRequestDTO;
import com.javaweb.edutest.dto.request.UserAddToGroupRequestDTO;
import com.javaweb.edutest.dto.request.UserGroupRequestDTO;
import com.javaweb.edutest.dto.response.ResponseData;
import com.javaweb.edutest.enums.TestGroupStatus;
import com.javaweb.edutest.enums.UserGroupStatus;
import com.javaweb.edutest.service.GroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/groups")
@CrossOrigin(origins = "http://localhost:3000")
public class GroupController {

    private final GroupService groupService;

    @GetMapping
    public ResponseData<?> getGroups(
            @RequestParam(defaultValue = "", required = false, value = "name") String searchName,
            @RequestParam(defaultValue = "0", required = false, value = "page-no") int pageNo,
            @RequestParam(defaultValue = "2", required = false, value = "page-size") int pageSize
    ) {
        try {
            return new ResponseData<>(groupService.getGroups(searchName, pageNo, pageSize),
                    HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
        }
    }

    @GetMapping("/{groupId}/tests")
    public ResponseData<?> getGroupTests(@PathVariable long groupId,
                                         @RequestParam(defaultValue = "", required = false, value = "name") String searchName,
                                         @RequestParam(defaultValue = "0", required = false, value = "page-no") int pageNo,
                                         @RequestParam(defaultValue = "2", required = false, value = "page-size") int pageSize,
                                         @RequestParam(required = false, value = "status") TestGroupStatus status,
                                         @RequestParam(required = false, value = "unassigned") boolean unassigned
    ) {
        try {
            return new ResponseData<>(groupService.getTestsOfGroup(groupId, pageNo, pageSize, searchName, status), HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
        }
    }

    @GetMapping("/{groupId}/tests/unassigned")
    public ResponseData<?> getGroupTestsUnassigned(
            @PathVariable long groupId
    ) {
        try {
            return new ResponseData<>(groupService.getTestsOfGroupUnassigned(groupId), HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
        }
    }

    @GetMapping("/{groupId}")
    public ResponseData<?> getGroup(@PathVariable long groupId) {
        try {
            return new ResponseData<>(groupService.getGroup(groupId), HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
        } catch (Exception e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
        }
    }

    @GetMapping("/{groupId}/users")
    public ResponseData<?> getGroupUsers(@PathVariable long groupId,
                                         @RequestParam(defaultValue = "", required = false) String searchName,
                                         @RequestParam(defaultValue = "0", required = false, value = "page-no") int pageNo,
                                         @RequestParam(defaultValue = "2", required = false, value = "page-size") int pageSize,
                                         @RequestParam(defaultValue = "PENDING", required = false, value = "status") UserGroupStatus status) {
        try {
            return new ResponseData<>(groupService.getUsersOfGroup(groupId, searchName, pageNo, pageSize, status),
                    HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
        } catch (Exception e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseData<?> getGroupOfUser(@PathVariable long userId) {
        try {
            return new ResponseData<>(groupService.getGroupsOfUser(userId), HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
        } catch (Exception e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
        }
    }

    @PostMapping
    public ResponseData<?> addGroup(@RequestPart GroupRequestDTO groupRequestDTO,
                                    @RequestPart(value = "imageUrl", required = false) MultipartFile image
    ) {
        try {
            return new ResponseData<>(groupService.addGroup(groupRequestDTO, image), HttpStatus.CREATED.value(), HttpStatus.CREATED.getReasonPhrase());
        } catch (IOException e) {
            return new ResponseData<>(HttpStatus.CREATED.value(), "create group success, but upload image failed");
        } catch (Exception e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
        }
    }

    @PostMapping("{groupId}/users")
    public ResponseData<?> addMemberToGroup(
            @PathVariable long groupId,
            @RequestBody UserAddToGroupRequestDTO userAddToGroupRequestDTO) {
        try {
            groupService.addMembersToGroup(
                    groupId, userAddToGroupRequestDTO
            );
            return new ResponseData<>(HttpStatus.CREATED.value(), HttpStatus.CREATED.getReasonPhrase());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
        }
    }

    @PostMapping("{groupId}/tests")
    public ResponseData<?> addTestToGroup(@PathVariable long groupId, @RequestBody TestGroupRequestDTO testInGroupRequestDTO) {
        try {
            groupService.addTestsToGroup(groupId, testInGroupRequestDTO);
            return new ResponseData<>(HttpStatus.CREATED.value(), HttpStatus.CREATED.getReasonPhrase());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
        }
    }

    @PutMapping("{groupId}/users")
    public ResponseData<?> deleteMemberInGroup(@PathVariable long groupId, @RequestBody UserGroupRequestDTO userGroupRequestDTO) {
        try {
            groupService.deleteMembersInGroup(groupId, userGroupRequestDTO);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), HttpStatus.ACCEPTED.getReasonPhrase());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
        }
    }

    @PutMapping("{groupId}/tests")
    public ResponseData<?> deleteTestInGroup(@PathVariable long groupId, @RequestBody TestGroupRequestDTO testInGroupRequestDTO) {
        try {
            groupService.deleteTestsInGroup(groupId, testInGroupRequestDTO);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), HttpStatus.ACCEPTED.getReasonPhrase());
        } catch (Exception e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
        }
    }

    @PutMapping("/{groupId}")
    public ResponseData<?> updateGroup(@PathVariable long groupId,
                                       @RequestPart GroupRequestDTO groupRequestDTO,
                                       @RequestPart(value = "imageUrl", required = false) MultipartFile image
    ) {
        try {
            String imgUrl = groupService.updateGroup(groupId, groupRequestDTO, image);
            return new ResponseData<>(imgUrl, HttpStatus.ACCEPTED.value(), HttpStatus.ACCEPTED.getReasonPhrase());
        } catch (Exception e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
        }
    }

    @DeleteMapping("/{groupId}")
    public ResponseData<?> deleteGroup(@PathVariable long groupId) {
        try {
            groupService.deleteGroup(groupId);
            return new ResponseData<>(HttpStatus.NO_CONTENT.value(), HttpStatus.NO_CONTENT.getReasonPhrase());
        } catch (Exception e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
        }
    }
}