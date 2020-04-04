package com.clf.cloud.userserver.domain;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * chat_msg
 * @author 
 */
@Data
public class ChatMsg implements Serializable {
    private String id;

    private String sendUserId;

    private String acceptUserId;

    private String msg;

    /**
     * 签收的状态(未读,已读)
     */
    private Integer signFlag;

    private Date createTime;

    private static final long serialVersionUID = 1L;
}