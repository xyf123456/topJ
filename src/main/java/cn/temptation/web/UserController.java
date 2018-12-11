package cn.temptation.web;

import cn.temptation.bean.Resource;
import cn.temptation.bean.User;
import cn.temptation.dao.ResourceDao;
import cn.temptation.dao.UserDao;
import org.apache.logging.log4j.util.Strings;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.criteria.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class UserController {
    @Autowired
    private UserDao userDao;
    @Autowired
    private ResourceDao resourceDao;

    /**
     * 跳转【登录】页面
     *
     * @return
     */
    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * 登录处理
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "/dologin")
    @ResponseBody
    public Map<String, Object> dologin(User user) {
        Map<String, Object> resultMap = new HashMap<>();
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());

        try {
            // 登录认证
            subject.login(token);

            resultMap.put("flag", "ok");
        } catch (Exception e) {
            if (!Strings.isEmpty(e.getMessage())) {
                e.printStackTrace();
            }

            resultMap.put("flag", "ng");
        }

        return resultMap;
    }

    /**
     * 注销处理
     *
     * @return
     */
    @RequestMapping("/logout")
    public String logout() {
        // 1、获取Subject
        Subject subject = SecurityUtils.getSubject();

        // 2、执行注销
        try {
            subject.logout();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            return "redirect:login";
        }
    }

    /**
     * 跳转【系统首页】页面
     *
     * @return
     */
    @RequestMapping("/index")
    public String index(Model model) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        model.addAttribute("currentuser", user.getUsername());
        return "index";
    }

    @RequestMapping("/loadResource")
    @ResponseBody
    public List<Resource> loadResource(Integer resourcepid) {
        // 1、获取Subject
        User user = (User) SecurityUtils.getSubject().getPrincipal();

        List<Resource> list = resourceDao.findResourceByUserid(user.getUserid(), resourcepid);

        if (list.size() > 0) {
            for (Resource item : list) {
                List<Resource> children = resourceDao.findResourceByUserid(user.getUserid(), item.getResourceid());
                item.setChildren(children);
            }
        }

        return list;
    }

    @RequestMapping("/user/view")
    public String view() {
        return "user";
    }

    /**
     * 查询列表信息
     *
     * @param searchcondition 查询条件
     * @param searchcontent   查询内容
     * @param page            页数
     * @param rows            每页记录数
     * @return
     */
    @RequestMapping("/user/list")
    @ResponseBody
    public Map<String, Object> list(@RequestParam(value = "searchcondition", required = false) String searchcondition,
                                    @RequestParam(value = "searchcontent", required = false) String searchcontent,
                                    @RequestParam(value = "page", required = false) Integer page,
                                    @RequestParam(value = "rows", required = false) Integer rows) {
        // 创建查询规格对象
        Specification<User> specification = (Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            Predicate predicate = null;
            Path path = null;

            if (searchcondition != null && !"".equals(searchcondition)
                    && searchcontent != null && !"".equals(searchcontent)) {
                switch (searchcondition) {
                    case "username":      // 明细名称
                        path = root.get("username");
                        predicate = cb.like(path, "%" + searchcontent + "%");
                        break;
                    case "rolename":      // 类别名称
                        path = root.join("role", JoinType.INNER);
                        predicate = cb.like(path.get("rolename"), "%" + searchcontent + "%");
                        break;
                }
            }

            return predicate;
        };

        Pageable pageable = PageRequest.of(page - 1, rows, Sort.Direction.ASC, "userid");
        Page<User> pageModel = userDao.findAll(specification, pageable);

        // 获取rows
        List<User> list = pageModel.getContent();
        // 获取count
        Long count = pageModel.getTotalElements();

        Map<String, Object> resultMap = new HashMap();
        resultMap.put("total", count);
        resultMap.put("rows", list);
        resultMap.put("success", true);

        return resultMap;
    }

    /**
     * 新增处理 和 修改处理
     *
     * @param user
     * @return
     */
    @RequestMapping("/user/save")
    @ResponseBody
    public Map<String, Object> save(User user) {
        Map<String, Object> resultMap = new HashMap<>();

        try {
            userDao.save(user);
            resultMap.put("flag", "ok");
        } catch (Exception ex) {
            resultMap.put("flag", "ng");
        }

        return resultMap;
    }

    /**
     * 删除处理
     *
     * @param userid
     * @return
     */
    @RequestMapping("/user/delete")
    @ResponseBody
    public Map<String, Object> delete(@RequestParam("userid") String userid) {
        Map<String, Object> resultMap = new HashMap<>();
        userDao.deleteById(Integer.parseInt(userid));
        resultMap.put("success", true);
        return resultMap;
    }
}