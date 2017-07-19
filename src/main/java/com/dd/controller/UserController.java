package com.dd.controller;

import com.alibaba.fastjson.JSONObject;
import com.dd.dao.entity.UserEntity;
import com.dd.model.TestJsonModel;
import com.dd.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

/**
 * Created by zdd on 2017/7/17.
 */
@Controller
@SessionAttributes(value = {"classInitStr"})
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Resource(name = "userService")
    IUserService service;

    // region 测试
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
    // endregion

    // region  RequestMapping 分配路由, 限制请求
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
    public String testMappingConsumes(@RequestHeader("Accept") String type) { // 会匹配 */*
        logger.info("type : " + type);
        return "success";
    }

    // 仅处理request请求中Accept头中包含了"application/json"的请求，同时暗示了返回的内容类型为application/json
    @RequestMapping(value = "/testMappingProduces", produces = "application/json") // 会匹配 */*
    public String testMappingProduces(@RequestHeader("Accept") String type) {
        logger.info("type : " + type);
        return "success";
    }
    // endregion

    // region @RequestMapping 所有可用参数展示(实际都能从HttpServletRequest中获取到)
    // 可用参数 : HttpServletRequest, HttpServletResponse, HttpSession
    // 注意: 在使用HttpSession 对象的时候，如果此时HttpSession 对象还没有建立起来的话就会有问题
    @RequestMapping("/testParams/1")
    public void testParams(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        try {
            PrintWriter writer = response.getWriter();
            writer.println("HttpServletResponse success");
            if (request != null)
                writer.println("HttpServletRequest get value aa=" + request.getParameter("aa"));
            if (session != null) {
                writer.println("HttpSession is not null");
                if (session.getAttributeNames().hasMoreElements()) {
                    writer.println("HttpSession first element :" + session.getAttributeNames().nextElement());
                }
            } else {
                writer.println("HttpSession is null");
            }
            writer.println("HttpServletResponse end");
        } catch (IOException e) {
            logger.error("testParams response error");
        }
    }

    // 可用参数 : WebRequest 使用该对象可以访问到存放在HttpServletRequest 和HttpSession 中的属性值
    @RequestMapping("/testParams/2")
    public void testParams(HttpServletResponse response, WebRequest webRequest) {
        try {
            PrintWriter writer = response.getWriter();
            if (webRequest != null) {
                writer.println("WebRequest is not null");
                writer.println("WebRequest get val aa=" + webRequest.getParameter("aa"));
                writer.println("WebRequest get header accept:" + webRequest.getHeader("accept"));
            } else {
                writer.println("WebRequest is null");
            }
        } catch (IOException e) {
            logger.error("testParams response error");
        }
    }

    // 可用参数 : MultipartFile 实际上和 HttpServletRequest 中获取的是一样的, 要求 post 且 类型为 multipart/form-data
    @RequestMapping("/testParams/3")
    public void testParams(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "file", required = true) MultipartFile file) {
        try {
            PrintWriter writer = response.getWriter();
            writer.println("HttpServletRequest is MultipartHttpServletRequest : " + (request instanceof MultipartHttpServletRequest));
            if (request instanceof MultipartHttpServletRequest) {
                MultipartHttpServletRequest req = (MultipartHttpServletRequest) request;
                writer.println("MultipartFile is request get :" + (req.getFile("file") == file));
            }
        } catch (IOException e) {
            logger.error("testParams response error");
        }
    }
    // endregion

    // region @RequestMapping 所有返回值类型
    // ModelAndView
    @RequestMapping("/return/modelAndView")
    public ModelAndView returnModelAndView(ModelAndView modelAndView) {
        // modelAndView.setView(view);
        modelAndView.setViewName("redirectStr");
        modelAndView.addObject("username", "zdd");
        return modelAndView;
    }

    // 视图名称
    @RequestMapping("/return/viewName")
    public String returnViewName() {
        return "success";
    }

    // void 一般由 response 返回, 如果没有写的话，那么Spring 将会利用RequestToViewNameTranslator 来返回一个对应的视图名称。如果视图中需要模型的话，处理方法与返回字符串的情况相同。 在这里是跳转到 /user/reutrn/void.jsp
    @RequestMapping("/return/model")
    public void returnView(HttpServletResponse response) {
        try {
            PrintWriter writer = response.getWriter();
            writer.println("ok");
        } catch (Exception e) {
        }
    }

    // 返回 Model 因为没有视图返回, 会默认访问/user/model.jsp
    @RequestMapping("/model")
    public Model returnModel(Model model) {
        TestJsonModel jsonModel = new TestJsonModel();
        jsonModel.setUsername("zdd");
        jsonModel.setPassword("password");
        model.addAttribute("model", jsonModel);
        return model;
    }

    // @ResponseBody注解时, 处理器方法的任何返回类型都会通过 HttpMessageConverters 转换之后写到HttpServletResponse 中
    @RequestMapping(value = "/return/json", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object returnJson(@RequestBody TestJsonModel model) {
        if (model != null) logger.info(model.toString());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", "success");
        return jsonObject;
    }
    // endregion

    // region 传入/传出Json对象
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

    // region 传参
    // region URI 传参
    // 使用 URI 模板传参, 大小写敏感
    // 注意:若@PathVariable("id") 这样写, 参数可随意起, 否则不能
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
    // endregion

    // region URI 正则传参
    // 正则
    @RequestMapping(value = "/testRegExp/{val:[a-z]+}.{num:[\\d]+}")
    public String reg(@PathVariable String val, @PathVariable String num) {
        logger.info("val : " + val + " ,  num : " + num);
        return "success";
    }
    // endregion

    // region HttpServletRequest 传参
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
    // endregion

    // region @RequestParam 传参
    // @RequestParam 获取传参, 大小写敏感
    // @RequestParam("id") 写入value, 若没有写入value, 将获取和参数名相同的名称的值
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
    // endregion

    // region @CookieValue 获取数据
    // @CookieValue 从cookie读取数据
    // @CookieValue("JSESSIONID") 写入cookie名, 若没有写入cookie名, 将获取和参数名相同的cookie
    @RequestMapping("/showByCookie")
    public String showByCookie(@CookieValue("JSESSIONID") String cookie) {
        logger.info("cookie : " + cookie);
        return "success";
    }
    // endregion

    // region @RequestHeader 获取数据
    // @RequestHeader 从请求头中获取数据, 大小写不敏感, 如Host 和 host都是指Host信息
    // @RequestHeader("Accept") 写入header名, 若没有写入header名, 将获取和参数名相同的header值
    @RequestMapping("/showByRequestHeader")
    public String showByRequestHeader(@RequestHeader("Accept") String accept) {
        logger.info("accept : " + accept);
        return "success";
    }
    // endregion
    // endregion

    // region 重定向传值
    @RequestMapping("/redirect/str")
    public String redirectStr(Model model) {
        model.addAttribute("username", "zdd");
        return "redirectStr";
    }

    @RequestMapping("/redirect/model")
    public String redirectModel(Model model) {
        TestJsonModel m = new TestJsonModel();
        m.setUsername("zdd");
        m.setPassword("password");
        model.addAttribute("userModel", m);
        return "redirectModel";
    }
    // endregion

    // region 模型/Controller共享数据

    // @SessionAttribute 标注在 Controller, 使用 Session 共享数据(注意, 这里测试时需要访问两次, 第一次session存入数据, 第二次才能获取到数据)

    // @ModelAttribute 标注在方法上, 模型间共享, 方法将在处理器方法执行之前执行，然后把返回的对象存放在模型属性中
    @ModelAttribute("initStr")
    public String getInitStr() {
        return "hello world";
    }

    @ModelAttribute("initInteger")
    public int getInitInteger() {
        return 99;
    }

    // @ModelAttribute("initInteger") 若未指定 name, 则按参数名获取
    @RequestMapping("/share/modelAttribute")
    public void shareModelAttribute(HttpServletRequest request, HttpServletResponse response, Map<String, Object> map, @ModelAttribute("initStr") String str, @ModelAttribute("initInteger") int integer) {
        try {
            // 通过map向session存入数据, 只有被 SessionAttribute 标注的name 才能写入session
            map.put("classInitStr", "hello world");
            // 不会被写入session
            map.put("classInitInt", 222);
            PrintWriter writer = response.getWriter();
            writer.println("get share str : " + str + " , integer : " + integer);
            HttpSession session = request.getSession();
            Enumeration<String> enume = session.getAttributeNames();
            while (enume.hasMoreElements()) {
                String name = enume.nextElement();
                writer.println("session name is : " + name + " ,  value : " + session.getAttribute(name));
            }
        } catch (Exception e) {

        }
    }
    // endregion
}
