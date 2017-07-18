package com.dd.controller;

import com.alibaba.fastjson.JSONObject;
import com.dd.dao.entity.UserEntity;
import com.dd.model.TestJsonModel;
import com.dd.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by zdd on 2017/7/17.
 */
@Controller
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Resource(name = "userService")
    IUserService service;

    // region  RequestMapping 分配路由
    // * 通配符
    @RequestMapping("*/test")
    public String test() {
        return "success";
    }

    // 请求参数必须有 val1且等于1, 必须有val2(值无所谓), val3 必须不能存在
    @RequestMapping(value = "/testMappingParams", params = {"val1=1", "val2", "!val3"})
    public String testMappingParams() {
        return "success";
    }

    // 请求方式只能为get
    @RequestMapping(value = "/testMappingMethod", method = {RequestMethod.GET})
    public String testMappingMethod() {
        return "success";
    }

    // 请求头只有host为localhost才能访问
    @RequestMapping(value = "/testMappingHeader", headers = {"host=localhost:8080"})
    public String testMappingHeader() {
        return "success";
    }

    // 只接受类型为 application/json 的请求, 可用 headers 代替
    @RequestMapping(value = "/testMappingConsumes", consumes = "application/json")
    public String testMappingConsumes(@RequestHeader("Content-Type") String type) { // 会匹配 */*
        logger.info("type : " + type);
        return "success";
    }

    // 仅处理request请求中Accept头中包含了"application/json"的请求，同时暗示了返回的内容类型为application/json
    @RequestMapping(value = "/testMappingProduces", produces = "application/json") // 会匹配 */*
    public String testMappingProduces(@RequestHeader("Content-Type") String type) {
        logger.info("type : " + type);
        return "success";
    }
    // endregion

    // region 传参
    @RequestMapping("/showAll")
    public String showUser() {
        List<UserEntity> users = service.getAllUser();
        if (users != null) {
            logger.info("users count : " + users.size());
            return "success";
        }
        logger.error("load all user error");
        return "fail";
    }

    // 使用 URI 模板传参 -- 注意:若@PathVariable("id") 这样写, 参数可随意起, 否则不能
    @RequestMapping("/show/{id}")
    public String showUser(@PathVariable Integer id) {
        if (id == null) id = 0;
        UserEntity user = service.getUserById(id);
        if (user != null) {
            logger.info("id-" + id + " user: " + user.toString());
            return "success";
        }
        logger.error("load user error id = " + id);
        return "fail";
    }

    // 使用 URI 模板传参
    @RequestMapping("/add/{username}/{password}")
    public String add(@PathVariable("username") String username, @PathVariable("password") String password) {
        UserEntity entity = new UserEntity();
        entity.setUsername(username);
        entity.setPassword(password);
        int result = service.createUser(entity);
        if (result > 0) {
            logger.info("insert " + username + ", " + password + " success");
            return "success";
        }
        logger.error("insert error " + username + ", " + password);
        return "fail";
    }

    // 正则
    @RequestMapping(value = "/testRegExp/{val:[a-z]+}.{num:[\\d]+}")
    public String reg(@PathVariable String val, @PathVariable String num) {
        logger.info("val : " + val + " ,  num : " + num);
        return "success";
    }

    // HttpServletRequest 获取传参
    @RequestMapping("/showByHttpServletRequest")
    public String show(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        UserEntity user = service.getUserById(id);
        if (user != null) {
            logger.info("id-" + id + " user: " + user.toString());
            return "success";
        }
        logger.error("load user error id = " + id);
        return "fail";
    }

    // @RequestParam 获取传参
    @RequestMapping("/showByRequestParam")
    public String show(@RequestParam(value = "id", required = true) Integer id) {
        if (id == null) id = 0;
        UserEntity user = service.getUserById(id);
        if (user != null) {
            logger.info("id-" + id + " user: " + user.toString());
            return "success";
        }
        logger.error("load user error id = " + id);
        return "fail";
    }

    // @CookieValue 从cookie读取数据
    @RequestMapping("/showByCookie")
    public String showByCookie(@CookieValue("JSESSIONID") String cookie) {
        logger.info("cookie : " + cookie);
        return "success";
    }

    // @RequestHeader 从请求头中获取数据
    @RequestMapping("/showByRequestHeader")
    public String showByRequestHeader(@RequestHeader("Accept") String accept) {
        logger.info("accept : " + accept);
        return "success";
    }
    // endregion

    // region 传入/传出对象
    // RequestBody 接收Json字符串, 可获取对象
    // ResponseBody 返回json对象
    @RequestMapping(value = "/testRequestBody", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object testRequestBody(@RequestBody TestJsonModel model) {
        // 若出现415错误, spring.xml 中添加 <mvc:annotation-driven />
        if (model != null) logger.info(model.toString());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", "success");
        return jsonObject;
    }
    // endregion
}
