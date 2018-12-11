package cn.temptation.dao;

import cn.temptation.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface UserDao extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {
    @Query(value = "select * from sys_user where username=?1", nativeQuery = true)
    User findByUsername(String username);
}