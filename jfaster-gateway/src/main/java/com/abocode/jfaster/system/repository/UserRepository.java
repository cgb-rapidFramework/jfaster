package com.abocode.jfaster.system.repository;
import com.abocode.jfaster.system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Description:
 *
 * @author: guanxf
 * @date: 2020/5/18
 */
public interface UserRepository extends JpaRepository<User, String> {

}
