package com.casic.patrol.user.domain;

import javax.persistence.*;

/**用户信息
 * Created by lenovo on 2016/5/16.
 */
@Entity
@Table(name="CD_USER")
@SequenceGenerator(name = "CD_USERIS",sequenceName = "CD_USERIS",allocationSize = 1,initialValue = 1)
public class User {
    private Long id;
    private String userName;
    private String password;
    private String sex;
    private String phoneNumber;
    private Integer isValid;
    private Role role;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CD_USERIS")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "userName")
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "sex")
    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Column(name = "phoneNumber")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Column(name = "isValid")
    public Integer getIsValid() {
        return isValid;
    }

    public void setIsValid(Integer isValid) {
        this.isValid = isValid;
    }

    @ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST })
    @JoinColumn(name = "role")
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
