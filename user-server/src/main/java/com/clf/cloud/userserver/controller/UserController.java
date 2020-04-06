package com.clf.cloud.userserver.controller;

import com.clf.cloud.api.user.UserFeignApis;
import com.clf.cloud.api.vo.UsersVO;
import com.clf.cloud.common.enums.ErrorEnum;
import com.clf.cloud.common.enums.OperatorFriendRequestTypeEnum;
import com.clf.cloud.common.enums.SearchFriendsStatusEnum;
import com.clf.cloud.common.exception.CommonException;
import com.clf.cloud.common.utils.ClientUtils;
import com.clf.cloud.common.utils.MD5Utils;
import com.clf.cloud.common.vo.BaseResponseVO;
import com.clf.cloud.userserver.bo.ContactsBO;
import com.clf.cloud.userserver.bo.LoginBO;
import com.clf.cloud.userserver.bo.RegisterBO;
import com.clf.cloud.userserver.bo.UsersBO;
import com.clf.cloud.userserver.domain.ChatMsg;
import com.clf.cloud.userserver.domain.Users;
import com.clf.cloud.userserver.redis.RedisService;
import com.clf.cloud.userserver.redis.key.MsgCodeKey;
import com.clf.cloud.userserver.service.ContactService;
import com.clf.cloud.userserver.service.MsgService;
import com.clf.cloud.userserver.service.UserService;
import com.clf.cloud.userserver.utils.FastDFSClient;
import com.clf.cloud.userserver.utils.FileUtils;
import com.clf.cloud.userserver.vo.MyFriendsVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * @Author: clf
 * @Date: 2020-03-12
 * @Description: TODO
 */
@RestController
@RequestMapping("/users")
@RefreshScope
@Slf4j
public class UserController implements UserFeignApis {

    @Autowired
    private UserService userService;
    @Autowired
    private FastDFSClient fastDFSClient;
    @Autowired
    private MsgService msgService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private ContactService contactService;

    @GetMapping("/{userId}")
    @Override
    public BaseResponseVO<UsersVO> queryUserInfo(@PathVariable("userId") String userId) {
        return BaseResponseVO.success(userId);
    }

    @PostMapping("/login")
    public BaseResponseVO login(@RequestBody @Valid LoginBO loginBO) {
        //判断用户名密码不能为空
        if (StringUtils.isBlank(loginBO.getUsername())
                || StringUtils.isBlank(loginBO.getPassword())) {
            return BaseResponseVO.errorMsg("用户名或密码不能为空...");
        }
        Users userResult = null;
        if (userService.queryUsernameIsExist(loginBO.getUsername())) {
            //登录
            try {
                userResult = userService.queryUserForLogin(loginBO.getUsername(),
                        MD5Utils.encrypt(loginBO.getPassword()));
            } catch (Exception e) {
                throw new CommonException(ErrorEnum.MD5_ERROR);
            }
        }
        if (userResult == null) {
            return BaseResponseVO.errorMsg("用户名或密码不正确...");
        }
        UsersVO userVO = new UsersVO();
        BeanUtils.copyProperties(userResult, userVO);
        return BaseResponseVO.success(userVO);
    }

    @PostMapping("/register")
    public BaseResponseVO register(@Valid @RequestBody RegisterBO registerBO) {
        if (userService.queryUsernameIsExist(registerBO.getUsername())) {
            return BaseResponseVO.error(ErrorEnum.USERNAME_EXISTED);
        }
        if (userService.isMobileBind(registerBO.getMobile())) {
            return BaseResponseVO.error(ErrorEnum.MOBILE_EXISTED);
        }
        String verifyCode = redisService.get(
                MsgCodeKey.getByMobile,
                registerBO.getMobile(),
                String.class);
        if (StringUtils.isEmpty(verifyCode)) {
            return BaseResponseVO.error(ErrorEnum.VERIFY_CODE_EXPIRED);
        }
        if (!verifyCode.equals(registerBO.getVerifyCode())) {
            return BaseResponseVO.error(ErrorEnum.VERIFY_CODE_ERROR);
        }
        redisService.delete(MsgCodeKey.getByMobile, registerBO.getMobile());
        Users user = new Users();
        BeanUtils.copyProperties(registerBO, user);
        user = userService.saveUser(user);
        UsersVO userVO = new UsersVO();
        BeanUtils.copyProperties(user, userVO);
        return BaseResponseVO.success(userVO);
    }

    @PostMapping("/uploadFaceBase64")
    public BaseResponseVO uploadFaceBase64(@RequestBody UsersBO usersBO) throws Exception {
        //获取前端传过来的base64字符串,然后转换为文件对象再上传
        String base64Data = usersBO.getFaceData();
        MultipartFile faceFile = FileUtils.base64ToMultipart(base64Data);
        String url = fastDFSClient.uploadBase64(faceFile);

        //获取缩略图的url
        String thump = "_80x80.";
        String arr[] = url.split("\\.");
        String thumpImgUrl = arr[0] + thump + arr[1];

        //更新用户头像
        Users user = new Users();
        user.setId(usersBO.getUserId());
        user.setFaceImage(thumpImgUrl);
        user.setFaceImageBig(url);
        user = userService.updateUserInfo(user);
        UsersVO usersVO = new UsersVO();
        BeanUtils.copyProperties(user, usersVO);
        return BaseResponseVO.success(usersVO);
    }

    @PostMapping("/setNickname")
    public BaseResponseVO setNickname(@RequestBody UsersBO usersBO) {
        Users user = new Users();
        user.setId(usersBO.getUserId());
        user.setNickname(usersBO.getNickname());
        user = userService.updateUserInfo(user);
        UsersVO usersVO = new UsersVO();
        BeanUtils.copyProperties(user, usersVO);
        return BaseResponseVO.success(usersVO);
    }

    /**
     * 搜索好友接口,根据账号做匹配查找
     *
     * @param myUserId
     * @param friendUsername
     * @return
     */
    @PostMapping("/search")
    public BaseResponseVO searchUser(String myUserId, String friendUsername) {
        if (StringUtils.isEmpty(myUserId) || StringUtils.isEmpty(friendUsername)) {
            return BaseResponseVO.error(ErrorEnum.PARAM_CANT_NULL);
        }
        Integer status = userService.preSearchFriends(myUserId, friendUsername);
        if (SearchFriendsStatusEnum.SUCCESS.status == status) {
            Users user = userService.queryUserInfoByUsername(friendUsername);
            UsersVO userVO = new UsersVO();
            BeanUtils.copyProperties(user, userVO);
            return BaseResponseVO.success(userVO);
        } else {
            return BaseResponseVO.errorMsg(SearchFriendsStatusEnum.getMsgByKey(status));
        }
    }

    @PostMapping("/addFriendRequest")
    public BaseResponseVO addFriendRequest(String myUserId, String friendUsername) {
        if (StringUtils.isEmpty(myUserId) || StringUtils.isEmpty(friendUsername)) {
            return BaseResponseVO.error(ErrorEnum.PARAM_CANT_NULL);
        }
        Integer status = userService.preSearchFriends(myUserId, friendUsername);
        if (SearchFriendsStatusEnum.SUCCESS.status == status) {
            userService.sendFriendRequest(myUserId, friendUsername);
        } else {
            return BaseResponseVO.errorMsg(SearchFriendsStatusEnum.getMsgByKey(status));
        }
        return BaseResponseVO.success();
    }

    /**
     * 查询用户的好友申请
     *
     * @param userId
     * @return
     */
    @PostMapping("/queryFriendRequests")
    public BaseResponseVO queryFriendRequests(String userId) {
        if (StringUtils.isEmpty(userId)) {
            return BaseResponseVO.errorMsg("用户不存在");
        }
        return BaseResponseVO.success(userService.queryFriendRequestList(userId));
    }

    /**
     * 对好友请求进行处理
     *
     * @param acceptUserId
     * @param sendUserId
     * @param operType
     * @return
     */
    @PostMapping("/operFriendRequest")
    public BaseResponseVO operFriendRequest(String acceptUserId,
                                          String sendUserId,
                                          Integer operType) {
        if (StringUtils.isEmpty(acceptUserId) ||
                StringUtils.isEmpty(sendUserId) ||
                operType == null) {
            return BaseResponseVO.error(ErrorEnum.PARAM_CANT_NULL);
        }
        if (StringUtils.isEmpty(OperatorFriendRequestTypeEnum.getMsgByType(operType))) {
            return BaseResponseVO.errorMsg("操作类型不能为空");
        }

        if (OperatorFriendRequestTypeEnum.IGNORE.getType() == operType) {
            //1.忽略好友请求,删除请求记录
            userService.deleteFriendRequest(sendUserId, acceptUserId);
        } else if (OperatorFriendRequestTypeEnum.PASS.getType() == operType) {
            //2.通过请求,则增加好友关系,然后删除好友请求记录
            userService.passFriendRequest(sendUserId, acceptUserId);
        }
        //查询好友列表
        List<MyFriendsVO> myFriends = userService.queryFriends(acceptUserId);
        return BaseResponseVO.success(myFriends);
    }

    /**
     * 查询我的好友列表
     *
     * @param userId
     * @return
     */
    @PostMapping("/myFriends")
    public BaseResponseVO myFriends(String userId) {
        if (StringUtils.isEmpty(userId)) {
            return BaseResponseVO.errorMsg("用户id不能为空");
        }
        List<MyFriendsVO> myFriends = userService.queryFriends(userId);
        return BaseResponseVO.success(myFriends);
    }

    /**
     * 用户手机端获取未签收的消息列表
     *
     * @param acceptUserId
     * @return
     */
    @PostMapping("/getUnReadMsgList")
    public BaseResponseVO getUnReadMsgList(String acceptUserId) {
        if (StringUtils.isEmpty(acceptUserId)) {
            return BaseResponseVO.errorMsg("用户id不能为空");
        }
        //查询列表
        List<ChatMsg> unReadMsgList = userService.getUnReadMsgList(acceptUserId);
        return BaseResponseVO.success(unReadMsgList);
    }

    /**
     * 发送手机验证码
     *
     * @param mobile
     * @param request
     * @return
     */
    @PostMapping("/sendVerifyCode")
    public BaseResponseVO sendVerifyCode(String mobile, HttpServletRequest request) {
        if (!ClientUtils.isRightMobileNum(mobile)) {
            return BaseResponseVO.error(ErrorEnum.MOBILE_FORMAT_ERROR);
        }
        //判断该手机号是否已经注册过
        if (userService.isMobileBind(mobile)) {
            return BaseResponseVO.error(ErrorEnum.MOBILE_EXISTED);
        }
        String ipAddress = ClientUtils.getClientIpAddress(request);
        return msgService.sendMsg(ipAddress, mobile);
    }

    @PostMapping("/saveContacts")
    public BaseResponseVO saveContacts(@RequestBody ContactsBO contactsBO) {
        log.error(contactsBO.toString());
        contactService.saveContact(contactsBO);
        return BaseResponseVO.success();
    }
}
