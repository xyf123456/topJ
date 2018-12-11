package cn.temptation.bean;

import lombok.Data;

import javax.persistence.*;

/**
 * 【人员】实体
 */
@Entity
@Table(name = "sys_user")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, columnDefinition = "COMMENT '人员编号'")
    private Integer userid;

    @Column(columnDefinition = "COMMENT '帐号'")
    private String username;

    @Column(columnDefinition = "COMMENT '密码'")
    private String password;

    // 关联角色
    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "roleid", foreignKey = @ForeignKey(name = "none"))
    private Role role;
}