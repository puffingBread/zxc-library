package org.humor.zxc.library.commons.dao.dto.req;

import lombok.Data;

import java.util.Date;

/***
 *  Date: 2019/11/27
 *  Time: 16:51
 *  @author xuzz
 */
@Data
public class NoSqlLogAddQuery {

    private Integer operateType;

    private Long tableId;

    private String tableName;

    private String content;

    private String userId;

    private String userName;

    private String operateId;

    private Date operateTime;

}
