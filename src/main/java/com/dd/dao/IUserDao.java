package com.dd.dao;

import com.dd.dao.entity.UserEntity;

import java.util.List;

/**
 * Created by zdd on 2017/7/17.
 */
public interface IUserDao {
    /**
     * 插入用户记录
     *
     * @param userEntity
     * @return
     */
    Integer insert(UserEntity userEntity);

    /**
     * 删除用户记录
     *
     * @param id
     * @return
     */
    Integer delete(Integer id);

    /**
     * 更新用户记录
     *
     * @param userEntity
     * @return
     */
    Integer update(UserEntity userEntity);

    /**
     * 根据用户id查找用户
     *
     * @param id
     * @return
     */
    UserEntity selectById(Integer id);

    /**
     * 查找所有用户
     *
     * @return
     */
    List<UserEntity> selectAll();
}
