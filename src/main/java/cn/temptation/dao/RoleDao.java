package cn.temptation.dao;

import cn.temptation.bean.Role;
import cn.temptation.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoleDao extends JpaRepository<Role, Integer>, JpaSpecificationExecutor<Role> {
    @Query(value = "INSERT INTO sys_role_resource (roleid,resourceid) VALUES ( ?1 , ?2 )", nativeQuery = true)
    void saveRoleResource(Integer roleId, Integer resourceId);

    @Query(value = "DELETE FROM sys_role_resource WHERE roleid= ?1 ", nativeQuery = true)
    void deleteRoleResource(Integer roleId);

    @Query(value = "select userid from sys_user_role where roleid=?1 ", nativeQuery = true)
    List<Integer> findUserByRoleid(Integer roleid);

    @Query(value = "select * from sys_role where roleid = ?1 ", nativeQuery = true)
    Role findRoleByRoleid(Integer roleid);

    @Query(value = "select * from sys_role where rolename=?1", nativeQuery = true)
    Role findByRolename(String rolename);
}