package org.humor.zxc.library.commons.dao.service.impl;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.humor.zxc.library.commons.dao.dto.LogSendDTO;
import org.humor.zxc.library.commons.dao.dto.req.NoSqlLogAddQuery;
import org.humor.zxc.library.commons.dao.dto.req.SqlLogAddQuery;
import org.humor.zxc.library.commons.dao.enums.LogTypeEnum;
import org.humor.zxc.library.commons.dao.service.SendLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;

/***
 *  Date: 2019/12/5
 *  Time: 16:35
 *  @author xuzz
 */
@Component
@Slf4j
public class SendLogServiceImpl implements SendLogService {

    @Autowired
    private KafkaTemplate<String, LogSendDTO<?>> kafkaTemplate;

    @Value("${bgm.kafka.topic.operate-log}")
    private String topic;

    @Override
    public void sendSqlLog(SqlLogAddQuery addQuery) {
        if (Objects.isNull(addQuery)) {
            return;
        }

//        addQuery.setUserId(Optional.ofNullable(AuthUtil.provider().getUserId()).orElse(""));
//        addQuery.setUserName(Optional.ofNullable(AuthUtil.provider().getUserName()).orElse(""));
//        addQuery.setOperateId(AuthUtil.provider().getTraceId());
        addQuery.setOperateTime(new Date());

        LogSendDTO<SqlLogAddQuery> logSendDTO = new LogSendDTO<>();
        logSendDTO.setLogType(LogTypeEnum.SQL.getValue());
        logSendDTO.setContent(addQuery);
        log.info("send sql log, logSendDTO=" + JSON.toJSONString(logSendDTO));
        kafkaTemplate.send(topic, logSendDTO);
    }

    @Override
    public void sendNoSqlLog(NoSqlLogAddQuery addQuery) {
        if (Objects.isNull(addQuery)) {
            return;
        }

//        addQuery.setUserId(AuthUtil.provider().getUserId());
//        addQuery.setUserName(AuthUtil.provider().getUserName());
//        addQuery.setOperateId(AuthUtil.provider().getTraceId());
        addQuery.setOperateTime(new Date());

        LogSendDTO<NoSqlLogAddQuery> logSendDTO = new LogSendDTO<>();
        logSendDTO.setLogType(LogTypeEnum.NO_SQL.getValue());
        logSendDTO.setContent(addQuery);
        log.info("send noSql log, logSendDTO=" + JSON.toJSONString(logSendDTO));

        kafkaTemplate.send(topic, logSendDTO);
    }
}
