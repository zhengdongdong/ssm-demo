package com.dd.service.impl;

import com.dd.dao.IUserDao;
import com.dd.dao.entity.UserEntity;
import com.dd.service.IUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by zdd on 2017/7/17.
 */
@Service("userService")
public class UserServiceImpl implements IUserService {
    @Resource
    private IUserDao userDao;

    public Integer createUser(UserEntity userEntity) {
        if (userDao == null || userEntity == null) return 0;
        return userDao.insert(userEntity);
    }

    public Integer updateUser(UserEntity userEntity) {
        if (userDao == null || userEntity == null) return 0;
        return userDao.update(userEntity);
    }

    public Integer deleteUser(Integer id) {
        if (userDao == null || id <= 0) return 0;
        return userDao.delete(id);
    }

    public UserEntity getUserById(Integer id) {
        if (userDao == null || id <= 0) return null;
        return userDao.selectById(id);
    }

    public List<UserEntity> getAllUser() {
        return userDao.selectAll();
    }

}
