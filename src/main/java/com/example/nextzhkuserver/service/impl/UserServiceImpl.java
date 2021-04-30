package com.example.nextzhkuserver.service.impl;

import com.example.nextzhkuserver.entity.User;
import com.example.nextzhkuserver.mapper.UserMapper;
import com.example.nextzhkuserver.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author kaiter
 * @since 2021-04-23
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
