package cn.temptation.dao;

import cn.temptation.bean.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ResourceDao extends JpaRepository<Resource, Integer>, JpaSpecificationExecutor<Resource> {
    // 按资源类型模糊查询
    @Query(value = "SELECT * FROM sys_resource WHERE resourcepid = ?1", nativeQuery = true)
    List<Resource> findByResourcepid(Integer resourcepid);

    @Query(value = "SELECT rs.resourceurl FROM sys_role_resource AS rr " +
            "INNER JOIN sys_resource AS rs ON rr.resourceid = rs.resourceid " +
            "INNER JOIN sys_role AS r ON rr.roleid = r.roleid " +
            "INNER JOIN sys_user AS u ON u.roleid = r.roleid " +
            "WHERE u.userid = :userid ", nativeQuery = true)
    List<String> findUrlByUserid(@Param("userid") Integer userid);

    @Query(value = "SELECT rs.* FROM sys_role_resource AS rr " +
            "INNER JOIN sys_resource AS rs ON rr.resourceid = rs.resourceid " +
            "INNER JOIN sys_role AS r ON rr.roleid = r.roleid " +
            "INNER JOIN sys_user AS u ON u.roleid = r.roleid " +
            "WHERE u.userid = :userid AND rs.resourcepid = :resourcepid", nativeQuery = true)
    List<Resource> findResourceByUserid(@Param("userid") Integer userid, @Param("resourcepid") Integer resourcepid);

    @Query(value = "SELECT rr.`resourceid` FROM sys_role_resource AS rr\n" +
            "INNER JOIN sys_resource AS rs ON rr.`resourceid` = rs.`resourceid` AND rr.`roleid` = :roleid", nativeQuery = true)
    List<Integer> findByRoleid(@Param("roleid") Integer roleid);
}