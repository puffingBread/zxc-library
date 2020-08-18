package org.humor.zxc.library.commons.dao.dto.req;

import lombok.Data;

import java.util.Date;

/***
 *  Date: 2019/10/22
 *  Time: 15:54
 *  @author xuzz
 */
@Data
public class SqlLogAddQuery {

    private String operateType;

    private String sql;

    private String userId;

    private String userName;

    private String tableName;

    private Long tableId;

    private String operateId;

    private Date operateTime;
}
