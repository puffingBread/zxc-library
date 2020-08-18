package org.humor.zxc.library.commons.dao.dto;

import lombok.Data;
import org.humor.zxc.library.commons.dao.enums.OperateTypeEnum;

import java.util.Date;
import java.util.List;

/***
 *  Date: 2019/8/28
 *  Time: 15:12
 *  @author xuzz
 */
@Data
public class OperateLogDTO {

    private Long id;

    private String userId;

    private String userName;

    private OperateTypeEnum operateType;

    private String content;

    private String tableName;

    private Long tableId;

    private String operateId;

    private Date operateTime;

    private List<OperateLogDTO> relatedLogs;
}
