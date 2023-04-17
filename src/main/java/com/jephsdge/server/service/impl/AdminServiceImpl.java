package com.jephsdge.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jephsdge.server.mapper.AdminMapper;
import com.jephsdge.server.pojo.Admin;
import com.jephsdge.server.service.IAdminService;
import com.jephsdge.server.utils.MD5Util;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jephsdge
 * @since 2023-04-06
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

    @Autowired
    private AdminMapper adminMapper;


    @Override
    public Admin login(String userName, String password) {
        String passwordMd5 = MD5Util.encode(password);
        return adminMapper.selectOne(
                new QueryWrapper<Admin>()
                        .eq("login_name",userName)
                        .eq("login_password",passwordMd5)
                        .eq("locked",false)
                        .eq("is_delete",false)
        );
    }

    @Override
    public Admin getAdminById(Integer adminId) {
        return adminMapper.selectById(adminId);
    }

    @Override
    public boolean updateName(Integer loginId, String loginName, String nickName) {
        Admin admin = new Admin();
        admin.setAdminId(loginId);
        admin.setLoginName(loginName);
        admin.setNickName(nickName);
        return adminMapper.updateById(admin) == 1;
    }

    @Override
    public boolean updatePassword(Integer loginId, String originalPassword, String newPassword) {
        Admin admin = adminMapper.selectById(loginId);
        String originalPasswordMd5 = MD5Util.encode(originalPassword);
        String newPasswordMd5 = MD5Util.encode(newPassword);
        if (admin.getLoginPassword().equals(originalPasswordMd5)){
            admin.setLoginPassword(newPasswordMd5);
            return adminMapper.updateById(admin) == 1;
        }
        return false;
    }


}
