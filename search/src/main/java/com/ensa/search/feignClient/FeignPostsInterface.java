package com.ensa.search.feignClient;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("POSTS/api/v1/posts")
public interface FeignPostsInterface{
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<String>> getPosts();
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<List<String>> createPost(@RequestBody String postDto);
    @RequestMapping(value = "/filter", method = RequestMethod.GET)
    ResponseEntity<List<String>> searchPosts(@RequestParam("query") String query);
}
