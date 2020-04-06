package com.clf.cloud.userserver.service.impl;

import com.clf.cloud.api.netty.ChatMsgNio;
import com.clf.cloud.common.enums.MsgSignFlagEnum;
import com.clf.cloud.common.enums.SearchFriendsStatusEnum;
import com.clf.cloud.common.utils.MD5Utils;
import com.clf.cloud.userserver.dao.ChatMsgDao;
import com.clf.cloud.userserver.dao.FriendsRequestDao;
import com.clf.cloud.userserver.dao.MyFriendsDao;
import com.clf.cloud.userserver.dao.UsersDao;
import com.clf.cloud.userserver.domain.ChatMsg;
import com.clf.cloud.userserver.domain.FriendsRequest;
import com.clf.cloud.userserver.domain.MyFriends;
import com.clf.cloud.userserver.domain.Users;
import com.clf.cloud.userserver.service.UserService;
import com.clf.cloud.userserver.utils.FastDFSClient;
import com.clf.cloud.userserver.utils.FileUtils;
import com.clf.cloud.userserver.utils.QRCodeUtils;
import com.clf.cloud.userserver.vo.FriendRequestVO;
import com.clf.cloud.userserver.vo.MyFriendsVO;
import lombok.extern.slf4j.Slf4j;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @Author: clf
 * @Date: 2020-03-12
 * @Description: TODO
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UsersDao usersDao;
    @Autowired
    private Sid sid;
    @Autowired
    private QRCodeUtils qrCodeUtils;
    @Autowired
    private FastDFSClient fastDFSClient;
    @Autowired
    private MyFriendsDao myFriendsDao;
    @Autowired
    private FriendsRequestDao friendsRequestDao;
    @Autowired
    private ChatMsgDao chatMsgDao;
    @Value("${tmpFilePath}")
    private String tmpFilePath;
    @Value("${defaultImage}")
    private String defaultImage;
    @Value("${defaultImageBig}")
    private String defaultImageBig;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean queryUsernameIsExist(String username) {
        Users result = usersDao.selectOne(username);
        return result != null ? true : false;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUserForLogin(String username, String password) {
        Users users = usersDao.selectOne(username);
        if(users != null) {
            if (users.getPassword().equals(password)) {
                return users;
            } else {
                return null;
            }
        }
        return null;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users saveUser(Users user) {
        //注册
        user.setId(sid.nextShort());
        user.setNickname(user.getUsername());
        //设置默认头像
        //TODO
        user.setFaceImage(defaultImage);
        user.setFaceImageBig(defaultImageBig);
        //为每个用户生成一个唯一的二维码

        String qrCodePath = tmpFilePath + user.getId() + "qrcode.png";
        // miniwechat_qrcode:[username]
        qrCodeUtils.createQRCode(qrCodePath, "miniwechat_qrcode:" + user.getUsername());
        MultipartFile qrCodeFile = FileUtils.fileToMultipart(qrCodePath);
        String qrCodeUrl = "";
        try {
            qrCodeUrl = fastDFSClient.uploadQRCode(qrCodeFile);
        } catch (IOException e) {
            //todo
        }
        user.setQrcode(qrCodeUrl);
        try {
            user.setPassword(MD5Utils.encrypt(user.getPassword()));
        } catch (Exception e) {
            //todo
        }
        usersDao.insert(user);
        return user;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users updateUserInfo(Users user) {
        usersDao.updateByPrimaryKey(user);
        return queryUserById(user.getId());
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Integer preSearchFriends(String myUserId, String friendUsername) {
        //1.用户不存在,返回[用户不存在]
        Users user = queryUserInfoByUsername(friendUsername);
        if(user == null) {
            return SearchFriendsStatusEnum.USER_NOT_EXIST.status;
        }
        //2.搜索用户是自己,返回[不能添加自己]
        if(user.getId().equals(myUserId)) {
            return SearchFriendsStatusEnum.NOT_YOURSELF.status;
        }
        //3.搜索账号已经是好友,返回[该用户已经是你的好友]
        MyFriends myFriends = myFriendsDao.selectByMyUserIdAndFriendId(myUserId, user.getId());
        if(myFriends != null) {
            return SearchFriendsStatusEnum.ALREADY_FRIENDS.status;
        }
        return SearchFriendsStatusEnum.SUCCESS.status;
    }

    @Override
    public Users queryUserInfoByUsername(String username) {
        return usersDao.selectOne(username);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void sendFriendRequest(String myUserId, String friendUsername) {
        //根据用户名获取朋友的信息
        Users friend = queryUserInfoByUsername(friendUsername);
        if(friend == null) {
            //TODO,抛异常处理
            log.error("friend is null");
        }

        //1.查询发送好友请求记录表
        FriendsRequest friendRequest = friendsRequestDao.
                selectBySendUserIdAndAcceptUserId(myUserId, friend.getId());
        if(friendRequest == null) {
            //2.即该用户还不是你的好友,才进行添加操作
            String requestId = sid.nextShort();
            FriendsRequest request = new FriendsRequest();
            request.setId(requestId);
            request.setSendUserId(myUserId);
            request.setAcceptUserId(friend.getId());
            request.setRequestDateTime(new Date());
            friendsRequestDao.insert(request);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<FriendRequestVO> queryFriendRequestList(String acceptUserId) {
        return usersDao.queryFriendRequestList(acceptUserId);
    }

    @Override
    public void deleteFriendRequest(String sendUserId, String acceptUserId) {
        friendsRequestDao.deleteBySendUserIdAndAcceptUserId(sendUserId, acceptUserId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void passFriendRequest(String sendUserId, String acceptUserId) {
        if(myFriendsDao.selectByMyUserIdAndFriendId(acceptUserId, sendUserId) != null) {
            //判断是否已经是好友,避免重复请求进行重复添加
            return;
        }
        //1.保存好友
        saveFriends(sendUserId, acceptUserId);
        //2.逆向保存好友
        saveFriends(acceptUserId, sendUserId);
        //3.删除好友请求记录
        deleteFriendRequest(sendUserId, acceptUserId);

        //TODO,使用消息队列, netty服务从对应的消息队列中得到需要更新的消息再主动推送至用户进行更新
//        Channel channel = UserChannelRel.get(sendUserId);
//        if(channel != null) {
//            //使用websocket主动推送消息到请求发起者, 更新他的通讯录列表为最新
//            DataContent dataContent = new DataContent();
//            dataContent.setAction(MsgActionEnum.PULL_FRIEND.type);
//            channel.writeAndFlush(
//                    new TextWebSocketFrame(
//                            JsonUtils.objectToJson(dataContent)));
//        }
    }

    @Transactional(propagation =  Propagation.SUPPORTS)
    @Override
    public List<MyFriendsVO> queryFriends(String userId) {
        return usersDao.queryMyFriends(userId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public String saveMsg(ChatMsgNio chatMsgNio) {
        ChatMsg msg = new ChatMsg();
        msg.setId(sid.nextShort());
        msg.setSendUserId(chatMsgNio.getSenderId());
        msg.setAcceptUserId(chatMsgNio.getReceiverId());
        msg.setMsg(chatMsgNio.getMsg());
        msg.setCreateTime(new Date());
        msg.setSignFlag(MsgSignFlagEnum.unsign.type);
        chatMsgDao.insert(msg);
        return msg.getId();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateMsgSigned(List<String> msgIdList) {
        chatMsgDao.batchUpdateMsgSigned(msgIdList);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ChatMsg> getUnReadMsgList(String acceptUserId) {
        return chatMsgDao.selectByAcceptUserIdAndSignType(acceptUserId, MsgSignFlagEnum.unsign.type);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean isMobileBind(String mobile) {
        Users user = usersDao.selectByMobile(mobile);
        return user != null;
    }

    @Override
    public Users selectByUserId(String userId) {
        return usersDao.selectByPrimaryKey(userId);
    }

    @Override
    public Users selectByMobile(String phoneNumber) {
        return usersDao.selectByMobile(phoneNumber);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveFriends(String sendUserId, String acceptUserId) {
        MyFriends myFriends = new MyFriends();
        myFriends.setId(sid.nextShort());
        myFriends.setMyUserId(sendUserId);
        myFriends.setMyFriendUserId(acceptUserId);
        myFriendsDao.insert(myFriends);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public Users queryUserById(String userId) {
        return usersDao.selectByPrimaryKey(userId);
    }
}
