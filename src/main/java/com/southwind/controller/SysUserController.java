package com.southwind.controller;


import com.southwind.entity.SysUser;
import com.southwind.service.SysUserService;
import com.southwind.util.MD5Util;
import com.southwind.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author admin
 * @since 2022-08-08
 */
@Controller
@RequestMapping("/user")
public class SysUserController {

    @Autowired
    private SysUserService userService;

    @GetMapping("/list")
    public ModelAndView list(
            @RequestParam(value = "key",required = false) String key,
            @RequestParam(value = "page",required = false) Integer page
    ){
        if(page == null) page = 1;
        ModelAndView modelAndView = new ModelAndView();
        PageUtil pageUtil = this.userService.queryUser(key, page);
        modelAndView.addObject("list",pageUtil.getData());
        modelAndView.addObject("page", page);
        List<Integer> pages = new ArrayList<>();
        for (int i = 1; i <= pageUtil.getPages(); i++) {
            pages.add(i);
        }
        modelAndView.addObject("pages", pages);
        modelAndView.addObject("pageCount", pageUtil.getPages());
        modelAndView.addObject("total", pageUtil.getTotal());
        modelAndView.addObject("start", (page-1)*PageUtil.SIZE + 1);
        Integer end = page*PageUtil.SIZE;
        if(end > pageUtil.getTotal()) {
            modelAndView.addObject("end", pageUtil.getTotal());
        } else {
            modelAndView.addObject("end", end);
        }
        modelAndView.addObject("key", key);
        modelAndView.setViewName("user_list");
        return modelAndView;
    }

    @PostMapping("/save")
    public String save(SysUser user){
        //密码加密
        user.setPassword(MD5Util.getSaltMD5(user.getPassword()));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User admin = (User) authentication.getPrincipal();
        user.setCreateUser(admin.getUsername());
        this.userService.save(user);
        return "redirect:/user/list";
    }

    @GetMapping("/delete/{id}")
    @ResponseBody
    public String delete(@PathVariable("id") Integer id){
        boolean remove = this.userService.removeById(id);
        if(remove) return "success";
        return "fail";
    }

    @GetMapping("/findById/{id}")
    public ModelAndView findById(@PathVariable("id") Integer id){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", this.userService.getById(id));
        modelAndView.setViewName("user_update");
        return modelAndView;
    }

    @PostMapping("/update")
    public String update(SysUser user){
        //密码加密
        user.setPassword(MD5Util.getSaltMD5(user.getPassword()));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User admin = (User) authentication.getPrincipal();
        user.setUpdateUser(admin.getUsername());
        this.userService.updateById(user);
        return "redirect:/user/list";
    }

}

