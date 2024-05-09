package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.Constants;
import com.example.common.enums.LevelEnum;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.User;
import com.example.entity.User;
import com.example.exception.CustomException;
import com.example.mapper.UserMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    public void add(User user) {
        // 业务逻辑
        //先查询用户是否已经存在，
        User dbUser = userMapper.selectByUsername(user.getUsername());
        // 验证业务逻辑：首先根据传入的用户名查询数据库中是否存在该用户名的用户信息
        if (ObjectUtil.isNotEmpty(dbUser)) {//如果查出不为空，说明有
            throw new CustomException(ResultCodeEnum.USER_EXIST_ERROR);
            // 抛出自定义异常，表示参数错误（用户名已存在），不可重复添加
        }
        if (ObjectUtil.isEmpty(user.getPassword())) {//如果密码为空
            user.setPassword(Constants.USER_DEFAULT_PASSWORD);//则赋值默认密码
        }
        if (ObjectUtil.isEmpty(user.getLevel())) {//如果身份没有选
            user.setLevel(LevelEnum.ORDINARY.level);//默认赋值创建一个 普通户 [枚举]
        }
        if (ObjectUtil.isEmpty(user.getName())) {//如果姓名没有
            user.setName(user.getUsername());//则默认赋值用户名
        }
        user.setRole(RoleEnum.USER.name());//用户新增的时候自动创建添加一个角色
        userMapper.insert(user);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        userMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        // 定义一个公共方法deleteBatch，用于根据传入的用户ID列表从数据库中批量删除对应的用户记录
        for (Integer id : ids) {
            userMapper.deleteById(id);
            // 对于集合中的每一个用户ID，调用UserMapper接口中的删除方法进行单个用户的删除操作
        }
    }

    /**
     * 修改
     */
    public void updateById(User user) {
        userMapper.updateById(user);
    }

    /**
     * 根据ID查询
     */
    public User selectById(Integer id) {
        User user = userMapper.selectById(id);
        // 生成token
        String tokenData = user.getId() + "-" + RoleEnum.USER.name();
        String token = TokenUtils.createToken(tokenData, user.getPassword());//tokenData、getPassword-->token
        user.setToken(token);
        return user;
    }

    /**
     * 查询所有
     */
    public List<User> selectAll(User user) {
        return userMapper.selectAll(user);
    }

    /**
     * 分页查询
     */
    public PageInfo<User> selectPage(User user, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<User> list = userMapper.selectAll(user);
        return PageInfo.of(list);
    }

    /**
     * 登录
     */
    public Account login(Account account) {
        Account dbUser = userMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbUser)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!account.getPassword().equals(dbUser.getPassword())) {
            throw new CustomException(ResultCodeEnum.USER_ACCOUNT_ERROR);
        }
        // 生成token
        String tokenData = dbUser.getId() + "-" + RoleEnum.USER.name();
        String token = TokenUtils.createToken(tokenData, dbUser.getPassword());//tokenData、getPassword-->token
        dbUser.setToken(token);
        return dbUser;
    }

    /**
     * 注册
     */
    public void register(Account account) {
        User user = new User();
        BeanUtils.copyProperties(account, user);
        add(user); /*注册要往数据库新增一条数据*/
    }

    /**
     * 修改密码
     */
    public void updatePassword(Account account) {
        User dbUser = userMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbUser)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!account.getPassword().equals(dbUser.getPassword())) {
            throw new CustomException(ResultCodeEnum.PARAM_PASSWORD_ERROR);
        }
        dbUser.setPassword(account.getNewPassword());
        userMapper.updateById(dbUser);
    }

    /**
     * 查询所有用户身份信息
     */
    public Set<String> queryAllLevel() {
        return userMapper.queryAllLevel();
    }

    /**
     * 统计用户类型比例
     */
    public int countUserByLevel(String level) {
        return userMapper.countUserByLevel(level);
    }
}
