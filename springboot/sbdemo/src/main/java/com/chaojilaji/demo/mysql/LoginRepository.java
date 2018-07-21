package com.chaojilaji.demo.mysql;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginRepository extends JpaRepository<UsersEntity,Integer> {
    @Query("select t from UsersEntity t where t.userphone = ?1")
    UsersEntity findByUserphoneAndUsername(int userphone);
}
