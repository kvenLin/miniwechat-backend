package com.clf.cloud.userserver.service.impl;

import com.clf.cloud.common.utils.ClientUtils;
import com.clf.cloud.userserver.bo.ContactBO;
import com.clf.cloud.userserver.bo.ContactsBO;
import com.clf.cloud.userserver.dao.ContactUserDao;
import com.clf.cloud.userserver.dao.UserContactRelDao;
import com.clf.cloud.userserver.domain.ContactUser;
import com.clf.cloud.userserver.domain.UserContactRel;
import com.clf.cloud.userserver.domain.Users;
import com.clf.cloud.userserver.service.ContactService;
import com.clf.cloud.userserver.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Pattern;

/**
 * @Author: clf
 * @Date: 2020-02-19
 * @Description: TODO
 */
@Component
@Slf4j
public class ContactServiceImpl implements ContactService {
    @Autowired
    private UserService userService;
    @Autowired
    private ContactUserDao contactUserDao;
    @Autowired
    private UserContactRelDao userContactRelDao;

    @Override
    public void saveContact(ContactsBO contactsBO) {
        //用于校验是否是真实名字
        String realnameReg = "/^[\\u4e00-\\u9fa5]{2,4}$/";
        Users me = userService.selectByUserId(contactsBO.getUserId());
        if(me == null) {
            log.error("用户id不存在: {}", contactsBO.getUserId());
            return;
        }
        List<ContactBO> contactList = contactsBO.getContactList();
        if(contactList == null || contactList.size() == 0 ) {
            log.error("用户通讯录获取失败或通讯录为空");
        }
        for (ContactBO contactBO : contactList) {

            //判断该手机号用户是否存在,存在设置联系人的userId
            String userId = "";
            String phone = ClientUtils.formatPhone(contactBO.getPhoneNumber());
            if(userService.isMobileBind(phone)) {
                Users user = userService.selectByMobile(phone);
                userId = user.getId();
            }
            //联系人是否保存过
            ContactUser contactUser = contactUserDao.selectByPhone(phone);
            if(contactUser != null) {
                contactUser.setUserId(userId);
                //更新操作
                //对真实用户名的联系人进行更新或存储
                if(Pattern.matches(realnameReg, contactBO.getUsername())) {
                    contactUser.setRealname(contactBO.getUsername());
                }
                contactUserDao.updateByPrimaryKeySelective(contactUser);
            } else {
                contactUser = new ContactUser();
                contactUser.setUserId(userId);
                contactUser.setPhone(phone);
                contactUser.setRealname(contactBO.getUsername());
                //插入操作
                contactUserDao.insert(contactUser);
            }

            //最后创建与该用户之间的关系
            UserContactRel rel = userContactRelDao.selectByUserIdAndPhone(me.getId(), phone);
            if(rel == null) {
                rel = new UserContactRel();
                rel.setUserId(me.getId());
                rel.setRelationNick(contactBO.getUsername());
                rel.setPhone(phone);
                userContactRelDao.insertSelective(rel);
            } else {
                rel.setRelationNick(contactBO.getUsername());
                userContactRelDao.updateByPrimaryKeySelective(rel);
            }

        }

    }
}
