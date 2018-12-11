package cn.temptation.bean;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * 【资源】实体
 */
@Entity
@Table(name = "sys_resource")
@Data
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer resourceid;
    @Column
    private Integer resourcepid;
    @Column
    private String resourcename;
    @Column
    private String resourceicon;
    @Column
    private String resourceurl;

    @OneToMany(cascade = {CascadeType.ALL})
    @Transient
    private List<Resource> children;
}