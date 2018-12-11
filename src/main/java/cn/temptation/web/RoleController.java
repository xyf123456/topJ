package cn.temptation.web;

import cn.temptation.bean.Role;
import cn.temptation.dao.ResourceDao;
import cn.temptation.dao.RoleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.criteria.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private ResourceDao resourceDao;

    @RequestMapping("/view")
    public String view() {
        return "role";
    }

    /**
     * 查询列表信息
     *
     * @param searchcontent 查询内容
     * @param page          页数
     * @param rows          每页记录数
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public Map<String, Object> list(@RequestParam(value = "searchcontent", required = false) String searchcontent,
                                    @RequestParam(value = "page", required = false) Integer page,
                                    @RequestParam(value = "rows", required = false) Integer rows) {
        // 创建查询规格对象
        Specification<Role> specification = (Root<Role> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            Predicate predicate = null;
            Path path = null;

            if (searchcontent != null && !"".equals(searchcontent)) {
                path = root.get("rolename");
                predicate = cb.like(path, "%" + searchcontent + "%");
            }

            return predicate;
        };

        Pageable pageable = PageRequest.of(page - 1, rows, Sort.Direction.ASC, "roleid");
        Page<Role> pageModel = roleDao.findAll(specification, pageable);

        // 获取rows
        List<Role> list = pageModel.getContent();
        // 获取count
        Long count = pageModel.getTotalElements();

        Map<String, Object> resultMap = new HashMap();
        resultMap.put("total", count);
        resultMap.put("rows", list);
        resultMap.put("success", true);

        return resultMap;
    }

    /**
     * 查询类别信息
     *
     * @return
     */
    @RequestMapping("/findAllRole")
    @ResponseBody
    public Map<String, Object> findAllRole() {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("success", true);
        resultMap.put("list", roleDao.findAll());
        return resultMap;
    }

    @RequestMapping("/resource/match")
    @ResponseBody
    public List<Integer> match(@RequestParam(value = "roleid", required = false) String roleid) {
        List<Integer> list = resourceDao.findByRoleid(Integer.parseInt(roleid));
        return list;
    }

    @RequestMapping("/relResources")
    @ResponseBody
    public Map<String, Object> relResources(@RequestParam(value = "resourceids", required = false) String resourceids,
                                            @RequestParam(value = "roleid", required = false) String roleid) {
        Map<String, Object> resultMap = new HashMap<>();

        try {
            roleDao.deleteRoleResource(Integer.parseInt(roleid));
            for (String resourceid : resourceids.split(",")) {
                roleDao.saveRoleResource(Integer.parseInt(roleid), Integer.parseInt(resourceid));
            }
            resultMap.put("success", true);
        } catch (Exception e) {
            resultMap.put("success", false);
            e.printStackTrace();
        }

        return resultMap;
    }

    // 角色新增处理 和 修改处理
    @RequestMapping("/save")
    @ResponseBody
    public Map<String, Object> save(Role role) {
        Map<String, Object> resultMap = new HashMap<>();

        if (roleDao.findByRolename(role.getRolename()) == null) {
            try {
                roleDao.save(role);
                resultMap.put("flag", "ok");
            } catch (Exception ex) {
                resultMap.put("flag", "ng");
            }
        } else {
            resultMap.put("flag", "exist");
        }

        return resultMap;
    }

    // 角色删除
    @RequestMapping("/delete")
    @ResponseBody
    public Map<String, Object> delete(@RequestParam("roleid") Integer roleid) {
        Map<String, Object> resultMap = new HashMap<>();

        try {
            List<Integer> list = roleDao.findUserByRoleid(roleid);
            if (list.size() > 0) {
                resultMap.put("success", "该角色有关联的用户，无法删除");
            } else {
                Role role = roleDao.findRoleByRoleid(roleid);
                roleDao.delete(role);
                resultMap.put("success", "true");
            }
        } catch (Exception e) {
            resultMap.put("success", "false");
            e.printStackTrace();
        }

        return resultMap;
    }
}