package cn.temptation.util;

import cn.temptation.bean.Resource;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 转换工具类
 */
public class ConvertUtil {
    /**
     * 转换为Tree格式
     *
     * @param list
     * @param pid
     * @param params：按父节点编号字段、节点编号字段、节点名称字段顺序传入
     * @param <T>
     * @return
     */
    public static <T> List<Map<String, Object>> convert2Tree(List<T> list, Integer pid, String... params) {
        List<Map<String, Object>> result = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = null;
            T temp = (T) list.get(i);

            if (getFieldValueByFieldName(params[0], temp).equals(pid)) {
                map = new HashMap<>();
                map.put("id", getFieldValueByFieldName(params[1], temp));
                map.put("text", getFieldValueByFieldName(params[2], temp));
                map.put("children", convert2Tree(list, Integer.parseInt(getFieldValueByFieldName(params[1], temp).toString()), params));
            }

            if (map != null) {
                result.add(map);
            }
        }

        return result;
    }

    /**
     * 转换为TreeGrid格式
     * @param list
     * @param pid
     * @param params：按编号字段、名称字段、图标字段、URL字段顺序传入
     * @param <T>
     * @return
     */
    public static <T> List<Map<String, Object>> convert2TreeGrid(List<Resource> list, Integer pid, String... params) {
        List<Map<String, Object>> result = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = null;
            T temp = (T) list.get(i);

            if (getFieldValueByFieldName(params[0], temp).equals(pid)) {
                map = new HashMap<>();
                map.put(params[1], getFieldValueByFieldName(params[1], temp));
                map.put(params[2], getFieldValueByFieldName(params[2], temp));
                map.put(params[3], getFieldValueByFieldName(params[3], temp));
                map.put(params[4], getFieldValueByFieldName(params[4], temp));
                map.put("children", convert2TreeGrid(list, Integer.parseInt(getFieldValueByFieldName(params[1], temp).toString()), params));
            }

            if (map != null) {
                result.add(map);
            }
        }

        return result;
    }

    /**
     * 根据属性名获取属性值
     *
     * @param fieldName
     * @param object
     * @return
     */
    private static Object getFieldValueByFieldName(String fieldName, Object object) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            // 设置对象的访问权限，保证对private的属性的访问
            field.setAccessible(true);
            return field.get(object);
        } catch (Exception e) {

            return null;
        }
    }
}