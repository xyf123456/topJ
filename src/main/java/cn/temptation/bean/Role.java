package cn.temptation.bean;

import lombok.Data;

import javax.persistence.*;

/**
 * 【角色】实体
 */
@Entity
@Table(name = "sys_role")
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roleid;
    @Column
    private String rolename;
}