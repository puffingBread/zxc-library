package org.humor.zxc.library.commons.dao.dto;

import lombok.Data;

import java.io.Serializable;

/***
 *  Date: 2019/12/5
 *  Time: 17:23
 *  @author xuzz
 */
@Data
public class LogSendDTO<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer logType;
    private T content;
}
