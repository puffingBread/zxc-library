package org.humor.zxc.library.commons.dao.service;


import org.humor.zxc.library.commons.dao.dto.req.NoSqlLogAddQuery;
import org.humor.zxc.library.commons.dao.dto.req.SqlLogAddQuery;

/***
 *  Date: 2019/12/5
 *  Time: 17:19
 *  @author xuzz
 */
public interface SendLogService {

    void sendSqlLog(SqlLogAddQuery addQuery);

    void sendNoSqlLog(NoSqlLogAddQuery addQuery);
}
