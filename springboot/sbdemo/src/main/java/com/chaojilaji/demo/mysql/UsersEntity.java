package com.chaojilaji.demo.mysql;

import javax.persistence.*;
import java.util.Objects;

@Entity( name="users" )
public class UsersEntity {
    private int uid;
    private Long userphone;
    private String username;
    private String nickname;
    private Integer sex;


    @Id
    @Column(name = "uid")
    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    @Basic
    @Column(name = "userphone")
    public Long getUserphone() {
        return userphone;
    }

    public void setUserphone(Long userphone) {
        this.userphone = userphone;
    }

    @Basic
    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "nickname")
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Basic
    @Column(name = "sex")
    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsersEntity that = (UsersEntity) o;
        return uid == that.uid &&
                Objects.equals(userphone, that.userphone) &&
                Objects.equals(username, that.username) &&
                Objects.equals(nickname, that.nickname) &&
                Objects.equals(sex, that.sex);
    }

    @Override
    public int hashCode() {

        return Objects.hash(uid, userphone, username, nickname, sex);
    }
}
