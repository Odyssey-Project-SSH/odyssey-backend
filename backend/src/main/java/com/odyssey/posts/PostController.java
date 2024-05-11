package com.odyssey.posts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/user/{userId}")
    public List<Post> getPostsByUserId(@PathVariable("userId") Integer userId) {
        return postService.getPostsByUserId(userId);
    }

    @GetMapping("/{postId}")
    public Post getPlan(@PathVariable("postId") Integer postId) {
        return postService.getPost(postId);
    }

    @PostMapping
    public void registerPost(@RequestBody PostRegistrationRequest request) {
        postService.addPost(request);
    }

    @DeleteMapping("/{postId}")
    public void deletePost(@PathVariable("postId") Integer postId) {
        postService.deletePost(postId);
    }

    @DeleteMapping("/user/{userId}")
    public void deletePostsByUserId(@PathVariable("userId") Integer userId) {
        postService.deletePostsByUserId(userId);
    }

    @PutMapping("/{postId}")
    public void updatePost(@PathVariable("postId") Integer postId, @RequestBody PostUpdateRequest request) {
        postService.updatePost(postId, request);
    }

}