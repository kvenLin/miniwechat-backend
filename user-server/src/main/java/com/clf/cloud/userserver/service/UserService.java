package com.clf.cloud.userserver.service;

import com.clf.cloud.api.netty.ChatMsgNio;
import com.clf.cloud.userserver.domain.ChatMsg;
import com.clf.cloud.userserver.domain.Users;
import com.clf.cloud.userserver.vo.FriendRequestVO;
import com.clf.cloud.userserver.vo.MyFriendsVO;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserService {
    /**
     * 判断用户名是否存在
     * @param username
     * @return
     */
    boolean queryUsernameIsExist(String username);

    /**
     * 查询
     * @param username
     * @param password
     * @return
     */
    Users queryUserForLogin(String username, String password);

    /**
     * 用户注册
     * @param user
     * @return
     */
    Users saveUser(Users user);

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    Users updateUserInfo(Users user);

    /**
     * 搜索好友前置条件
     * @param myUserId
     * @param friendUsername
     * @return
     */
    Integer preSearchFriends(String myUserId, String friendUsername);

    /**
     * 根据用户名查询用户
     * @param username
     * @return
     */
    Users queryUserInfoByUsername(String username);

    /**
     * 添加好友请求记录保存到数据库
     * @param myUserId
     * @param friendUsername
     */
    void sendFriendRequest(String myUserId, String friendUsername);

    /**
     * 查询好友请求
     * @param acceptUserId
     * @return
     */
    List<FriendRequestVO> queryFriendRequestList(String acceptUserId);

    /**
     * 删除好友请求记录
     * @param sendUserId
     * @param acceptUserId
     */
    void deleteFriendRequest(String sendUserId, String acceptUserId);

    /**
     * 通过好友请求
     * @param sendUserId
     * @param acceptUserId
     */
    void passFriendRequest(String sendUserId, String acceptUserId);

    /**
     * 查询好友列表
     * @param userId
     * @return
     */
    List<MyFriendsVO> queryFriends(String userId);

    /**
     * 获取未签收的消息列表
     * @param acceptUserId
     * @return
     */
    List<ChatMsg> getUnReadMsgList(String acceptUserId);

    /**
     * 判断手机号是否已经注册
     * @param mobile
     * @return
     */
    boolean isMobileBind(String mobile);

    /**
     * 根据userId判断是否存在该用户
     * @param userId
     * @return
     */
    Users selectByUserId(String userId);

    /**
     * 根据绑定的手机号搜索用户
     * @param phoneNumber
     * @return
     */
    Users selectByMobile(String phoneNumber);
}
