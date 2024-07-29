package com.ohgiraffers.blog.bellaBlog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {



    @GetMapping("/")
    public String index(){
        return "index";
    }

}


//private final UserService userService;
//
//@Autowired
//public UserController(UserService userService) {
//    this.userService = userService;
//}
//
//@GetMapping
//public String indexJaesuck(){
//    return "jaesuck/page";
//}
//
//@PostMapping
//public ModelAndView postBlog(UserDTO userDTO, ModelAndView mv){
//
//    if(userDTO.getBlogTitle() == null || userDTO.getBlogTitle().equals("")){
//        mv.setViewName("redirect:jaesuck");
//    }
//    if(userDTO.getBlogContent() == null || userDTO.getBlogContent().equals("")){
//        mv.setViewName("redirect:jaesuck");
//    }
//    int result = userService.post(userDTO);
//    if(result <= 0){
//        mv.setViewName("error/page");
//    }else{
//        mv.setViewName("jaesuck/page");
//    }
//    return mv;
//}