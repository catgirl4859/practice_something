package com.ohgiraffers.blog.bellaBlog.service;

import com.ohgiraffers.blog.bellaBlog.model.dto.BoardDTO;
import com.ohgiraffers.blog.bellaBlog.model.entity.BoardEntity;
import com.ohgiraffers.blog.bellaBlog.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BoardService {

    private final BoardRepository boardRepository;
    // Repository 는 기본적으로 Entity class 만 받아준다.

    @Autowired
    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public void save(BoardDTO boardDTO) {
//      Repository 의 save 메서드는 기본적으로 Entity 클래스만 받는다.

        BoardEntity boardEntity = BoardEntity.toSaveEntity(boardDTO);
        boardRepository.save(boardEntity);
    }

    public List<BoardDTO> findAll() {
        List<BoardEntity> boardEntityList = boardRepository.findAll();
        List<BoardDTO> boardDTOList = new ArrayList<>();

        for(BoardEntity boardEntity: boardEntityList){
            boardDTOList.add(BoardDTO.toBoardDTO(boardEntity));
        }
        return boardDTOList;
    }

    // JPA 가 제공해주는 method 들을 따라가면 자동으로 쿼리가 만들어진다.
    // 따라서 그게아닌 특수한 목적을 가진 쿼리들은 별도의 method 로 정의할 필요가 있다.
    // 이때, 이렇게 추가된 method 같은 경우에는 @Transactional 붙여줘야됨
    @Transactional
    public void updateHits(Long id) {
        boardRepository.updateHits(id);
    }

    public BoardDTO findById(Long id) {
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(id);
        if(optionalBoardEntity.isPresent()){
            BoardEntity boardEntity = optionalBoardEntity.get();
            BoardDTO boardDTO = BoardDTO.toBoardDTO(boardEntity);
            return boardDTO;
        }else{
            return null;
        }
    }

}


//public boolean likePost(Long userId){
//        try {
//            Post post = boardRepository.findPostById(userId);
//            if (post == null) {
//                throw new EntityNotFoundException("Post not found");
//            }
//            post.setLikes(post.getLikes() + 1);
//            boardRepository.save(post);
//            return true;
//        } catch (Exception e){
//            return false;
//        }
//    }

//@Transactional
//public int post(UserDTO userDTO) {
//    List<UserEntity> userEntities = userRepository.findAll();
//    // 도메인 로직
//    for (UserEntity blog: userEntities) {
//        if(blog.getBlogTitle().equals(userDTO.getBlogTitle())){
//            return 0;
//        }
//    }
//
//    UserEntity saveBlog = new UserEntity();
//    saveBlog.setBlogContent(userDTO.getBlogContent());
//    saveBlog.setBlogTitle(userDTO.getBlogTitle());
//    saveBlog.setCreateDate(new Date());
//    UserEntity result  = userRepository.save(saveBlog);
//
//    int resultValue = 0;
//
//    if(result != null){
//        resultValue = 1;
//    }
//
//    return resultValue;
//}