package com.ohgiraffers.crud_back.controller.controller;

import com.ohgiraffers.crud_back.model.entity.PostEntity;
import com.ohgiraffers.crud_back.service.PostService;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")  // 여기를 "/api"로 수정
public class PostController {

    @Value("${ftp.server}")
    private String FTP_SERVER;
    @Value("${ftp.port}")
    private int FTP_PORT;
    @Value("${ftp.username}")
    private String FTP_USER;
    @Value("${ftp.password}")
    private String FTP_PASSWORD;

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/post")
    public ResponseEntity<List<PostEntity>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @PostMapping("/post")
    public ResponseEntity<PostEntity> createPost(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("author") String author,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        PostEntity postEntity = new PostEntity();
        postEntity.setTitle(title);
        postEntity.setContent(content);
        postEntity.setAuthor(author);

        if (image != null && !image.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
            try {
                boolean uploaded = uploadFileToFTP(image, fileName);
                if (uploaded) {
                    postEntity.setImagePath(fileName);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }

        PostEntity createdPostEntity = postService.createPost(postEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPostEntity);
    }

    private boolean uploadFileToFTP(MultipartFile file, String fileName) throws IOException {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(FTP_SERVER, FTP_PORT);
            boolean login = ftpClient.login(FTP_USER, FTP_PASSWORD);
            if (!login) {
                throw new IOException("FTP 서버 로그인 실패");
            }
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode(); // 이 줄 추가
            boolean stored = ftpClient.storeFile(fileName, file.getInputStream());
            if (!stored) {
                throw new IOException("FTP 서버에 파일 업로드 실패");
            }
            return true;
        } catch (IOException e) {
            throw new IOException("FTP 업로드 중 오류 발생: " + e.getMessage(), e);
        } finally {
            if (ftpClient.isConnected()) {
                ftpClient.logout();
                ftpClient.disconnect();
            }
        }
    }

    @GetMapping("/post/{id}")
    public ResponseEntity<PostEntity> getPostById(@PathVariable Long id) {
        return postService.getPostById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


}