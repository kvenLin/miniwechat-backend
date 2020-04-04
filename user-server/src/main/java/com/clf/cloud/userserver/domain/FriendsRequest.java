package com.clf.cloud.userserver.domain;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * friends_request
 * @author 
 */
@Data
public class FriendsRequest implements Serializable {
    private String id;

    private String sendUserId;

    private String acceptUserId;

    private Date requestDateTime;

    private static final long serialVersionUID = 1L;
}