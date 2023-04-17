package com.jephsdge.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jephsdge.server.pojo.Admin;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jephsdge
 * @since 2023-04-06
 */
public interface IAdminService extends IService<Admin> {


    Admin login(String userName, String password);


    Admin getAdminById(Integer loginUserId);

    boolean updateName(Integer loginId, String loginName, String nickName);

    boolean updatePassword(Integer loginId, String originalPassword, String newPassword);
}
