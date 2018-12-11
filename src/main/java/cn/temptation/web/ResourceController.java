package cn.temptation.web;

import cn.temptation.bean.Resource;
import cn.temptation.dao.ResourceDao;

import cn.temptation.util.ConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/resource")
public class ResourceController {
    @Autowired
    private ResourceDao resourceDao;

    @RequestMapping("/view")
    public String view() {
        return "resource";
    }

    @RequestMapping("/list")
    @ResponseBody
    public List<Map<String, Object>> list() {
        List<Resource> list = resourceDao.findAll();
        return ConvertUtil.convert2TreeGrid(list, 0, "resourcepid", "resourceid", "resourcename", "resourceicon", "resourceurl");
    }

    @RequestMapping("/tree")
    @ResponseBody
    public List<Map<String, Object>> tree() {
        List<Resource> list = resourceDao.findAll();
        return ConvertUtil.convert2Tree(list, 0, "resourcepid", "resourceid", "resourcename");
    }
}