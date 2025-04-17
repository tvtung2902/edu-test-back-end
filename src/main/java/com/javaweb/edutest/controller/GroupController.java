package com.javaweb.edutest.controller;

import com.javaweb.edutest.dto.request.GroupRequestDTO;
import com.javaweb.edutest.dto.response.ResponseData;
import com.javaweb.edutest.service.GroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

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
                                    @RequestPart(value = "imageUrl") MultipartFile image
                                    ) {
        try {
            return new ResponseData<>(groupService.addGroup(groupRequestDTO, image), HttpStatus.CREATED.value(), HttpStatus.CREATED.getReasonPhrase());
        }
        catch (IOException e){
            return new ResponseData<>(HttpStatus.CREATED.value(), "create group success, but upload image failed");
        }
        catch (Exception e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
        }
    }

    @PostMapping("{groupId}/member")
    public ResponseData<?> addMemberToGroup(@PathVariable long groupId, @RequestBody Map<String, List<Long>> request){
        try {
            return new ResponseData<>(HttpStatus.CREATED.value(), HttpStatus.CREATED.getReasonPhrase());
        } catch (Exception e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
        }
    }

    @PostMapping("{groupId}/test")
    public ResponseData<?> addTestToGroup(@PathVariable long groupId, @RequestBody Map<String, List<Long>> request) {
        try {
            return new ResponseData<>(HttpStatus.CREATED.value(), HttpStatus.CREATED.getReasonPhrase());
        } catch (Exception e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
        }
    }

    @DeleteMapping("{groupId}/member")
    public ResponseData<?> deleteMemberInGroup(@PathVariable long groupId, @RequestBody Map<String, List<Long>> request){
        try {
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), HttpStatus.ACCEPTED.getReasonPhrase());
        } catch (Exception e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
        }
    }

    @PutMapping("{groupId}/test")
    public ResponseData<?> deleteTestInGroup(@PathVariable long groupId, @RequestBody Map<String, List<Long>> request) {
        try {
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), HttpStatus.ACCEPTED.getReasonPhrase());
        } catch (Exception e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
        }
    }

    @PutMapping("/{groupId}")
    public ResponseData<?> updateGroup(@PathVariable long groupId, @RequestBody GroupRequestDTO groupRequestDTO) {
        try {
            groupService.updateGroup(groupId, groupRequestDTO);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), HttpStatus.ACCEPTED.getReasonPhrase());
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