package org.humor.zxc.library.commons.dao.dto.req;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.humor.zxc.library.commons.util.dto.PageRequest;

import java.util.Date;
import java.util.List;

/***
 *  Date: 2019/8/30
 *  Time: 14:14
 *  @author xuzz
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OperateLogQuery extends PageRequest {

    private Long id;

    private String userId;

    private Integer operateType;

    private Long tableId;

    private String tableName;

    private Date startTime;

    private Date endTime;

    private List<String> operateIds;

}
