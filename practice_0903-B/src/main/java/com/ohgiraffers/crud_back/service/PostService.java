package com.ohgiraffers.crud_back.service;

import com.ohgiraffers.crud_back.model.dto.PostDTO;
import com.ohgiraffers.crud_back.model.entity.PostEntity;
import com.ohgiraffers.crud_back.repository.PostRepository;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;

    @Value("${ftp.server}")
    private String server;

    @Value("${ftp.port}")
    private int port;

    @Value("${ftp.username}")
    private String user;

    @Value("${ftp.password}")
    private String pass;

    @Value("${file.server.url}")
    private String fileServerUrl;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    // PostDTO 받아서 게시글등록
    public PostEntity createPost(PostDTO postDTO){
        PostEntity postEntity = toPostEntity(postDTO);
        return postRepository.save(postEntity);
    }
    private PostEntity toPostEntity(PostDTO postDTO) {
        PostEntity postEntity = new PostEntity.Builder()
                .title(postDTO.getTitle())
                .content(postDTO.getContent())
                .author(postDTO.getAuthor())
                .imagePath(postDTO.getImagePath())
                .build();
        return postEntity;
    }

    // 모든 게시글을 조회하고 각 게시글에 이미지 URL 추가 = 전체 목록
    public List<PostDTO> getAllPosts() {
        return postRepository.findAll().stream()
                .map(this::enhancePostWithImageUrl)
                .collect(Collectors.toList());
    }

//  ID로 게시글을 조회하고 이미지 URL 추가하는 메서드 = 상세 조회
    public Optional<PostDTO> getPostById(Long id) {
        return postRepository.findById(id)
                .map(this::enhancePostWithImageUrl);
    }

    private PostDTO enhancePostWithImageUrl(PostEntity post) {

        if (post.getImagePath() != null && !post.getImagePath().isEmpty()) {
            String imageUrl = String.format("/api/images/%s", post.getImagePath());
            post.setImagePath(imageUrl);
        }

        PostDTO dto = convertToDTO(post);
        return dto;
    }

    private PostDTO convertToDTO(PostEntity entity) {
        return new PostDTO.Builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .author(entity.getAuthor())
                .imagePath(entity.getImagePath())  // 수정: imagePath 사용
                .build();
    }

}