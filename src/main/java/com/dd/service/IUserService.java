package com.dd.service;

import com.dd.dao.entity.UserEntity;

import java.util.List;

/**
 * Created by zdd on 2017/7/17.
 */
public interface IUserService {

    Integer createUser(UserEntity userEntity);

    Integer updateUser(UserEntity userEntity);

    Integer deleteUser(Integer id);

    UserEntity getUserById(Integer id);

    List<UserEntity> getAllUser();
}
