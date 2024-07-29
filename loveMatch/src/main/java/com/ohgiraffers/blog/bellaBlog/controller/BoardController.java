package com.ohgiraffers.blog.bellaBlog.controller;


import com.ohgiraffers.blog.bellaBlog.model.dto.BoardDTO;
import com.ohgiraffers.blog.bellaBlog.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    @Autowired
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    } // 생성자 주입방식으로 의존성을 주입받게 된다.


    @GetMapping("/save")
    public String saveForm() {
        return "board/save";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute BoardDTO boardDTO) {
//        System.out.println(boardDTO);
        boardService.save(boardDTO);

        return "index";
    }

    @GetMapping("/") //DB 에서 data 가져와야해서 이때는 Model 객체 사용해야한다.
    public String findAll(Model model) {
        List<BoardDTO> boardDTOList = boardService.findAll();
        model.addAttribute("boardList", boardDTOList);
        return "board/list";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable Long id, Model model) {
        // 1. 해당게시글의 조회수를 하나 올림
        // 2. 게시글 데이터를 가져와서 detail.html 에 출력함
        boardService.updateHits(id);
        BoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("board", boardDTO);

        return "board/datail";
    }


}









