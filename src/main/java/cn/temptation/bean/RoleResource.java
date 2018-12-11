package cn.temptation.bean;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "sys_role_resource")
@Data
public class RoleResource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roleresourceid;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "roleid", foreignKey = @ForeignKey(name = "none"))
    private Role role;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "resourceid", foreignKey = @ForeignKey(name = "none"))
    private Resource resource;
}