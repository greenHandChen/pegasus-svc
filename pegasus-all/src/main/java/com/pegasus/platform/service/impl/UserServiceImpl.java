package com.pegasus.platform.service.impl;

import com.pegasus.common.exception.CommonException;
import com.pegasus.platform.domain.Employee;
import com.pegasus.platform.domain.User;
import com.pegasus.platform.repository.UserRepository;
import com.pegasus.platform.service.IEmployeeService;
import com.pegasus.platform.service.IUserService;
import com.pegasus.platform.vo.UserVO;
import com.pegasus.security.dto.CuxUserDetails;
import com.pegasus.security.utils.SecurityUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

//import com.pegasus.platform.repository.UserRepository;

/**
 * Created by enHui.Chen on 2019/9/4.
 */
@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private IEmployeeService employeeService;
    @Autowired
    private EntityManager entityManager;

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);

    }

    @Override
    public UserVO getCurrentUser() {
        CuxUserDetails cuxUserDetails = SecurityUtil.getCurrentUser();
        Assert.notNull(cuxUserDetails, "当前用户未进行登录！");

        UserVO userVO = UserVO.builder()
                .loginName(cuxUserDetails.getUsername())
                .email(cuxUserDetails.getEmail())
                .phone(cuxUserDetails.getPhone())
                .roleId(cuxUserDetails.getRoleId())
                .roleIds(cuxUserDetails.getRoleIds())
                .isActive(cuxUserDetails.getActive())
                .build();

        Employee employee = employeeService.findByUserId(cuxUserDetails.getUserId());
        Assert.notNull(employee, "无法找到当前用户！");

        userVO.setEmployeeCode(employee.getEmployeeCode());
        userVO.setFullName(employee.getFullName());
        userVO.setTitle(employee.getTitle());

        return userVO;
    }

    @Override
    public List<User> findAccountAll() {
        List<User> users = userRepository.findAll();
        if (!CollectionUtils.isEmpty(users)) {
            users.forEach(user -> user.setPassword(null));
        }
        return users;
    }

    @Override
    public void createOrUpdateAccount(User user) {
        if (user != null && user.getId() == null) {
            user.setUserType("user");
            user.setPassword(SecurityUtil.BCryptEncode(user.getPassword()));
        } else {
            Optional<User> optionalUser = userRepository.findById(user.getId());
            optionalUser.ifPresent(pUser -> user.setPassword(pUser.getPassword()));
        }
        userRepository.save(user);
    }

    @Override
    public User findAccountByUserId(Long userId) {
        Optional user = userRepository.findById(userId);
        if (user.isPresent()) {
            // 持久态
            User pUser = (User) user.get();
            // 游离态
            User freeUser = new User();
            BeanUtils.copyProperties(pUser, freeUser);
            freeUser.setPassword(null);
            return freeUser;
        }
        return null;
    }

    @Override
    public void modifyPassword(Long userId, String oldPassword, String newPassword) {
        if (userId == null) {
            throw new CommonException("userId不能为空!");
        }
        if (StringUtils.isEmpty(oldPassword)) {
            throw new CommonException("原密码不能为空!");
        }
        if (StringUtils.isEmpty(newPassword)) {
            throw new CommonException("新密码不能为空!");
        }
        userRepository.findById(userId).ifPresent(user -> {
            if (!SecurityUtil.BCryptMatch(oldPassword, user.getPassword())) {
                throw new CommonException("原密码不正确!");
            }
            user.setPassword(SecurityUtil.BCryptEncode(newPassword));
            userRepository.save(user);
        });

    }
}
