package com.pegasus.platform.repository;

import com.pegasus.platform.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


 /**
   * @Author: enHui.Chen
   * @Description: 
   * @Data 2019/11/8
   */
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor {
    User findByUsername(String username);
}
