package com.clf.cloud.userserver.domain;

import java.io.Serializable;
import lombok.Data;

/**
 * my_friends
 * @author 
 */
@Data
public class MyFriends implements Serializable {
    private String id;

    private String myUserId;

    private String myFriendUserId;

    private static final long serialVersionUID = 1L;
}