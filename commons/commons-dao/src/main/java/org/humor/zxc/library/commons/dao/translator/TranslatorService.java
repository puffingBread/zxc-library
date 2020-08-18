package org.humor.zxc.library.commons.dao.translator;


import net.sf.jsqlparser.JSQLParserException;
import org.humor.zxc.library.commons.dao.dto.OperateLogDTO;
import org.humor.zxc.library.commons.dao.dto.req.SqlLogAddQuery;

import java.util.List;


/**
 * @author xuzz
 */
public interface TranslatorService {

    List<OperateLogDTO> translate(SqlLogAddQuery addQuery) throws JSQLParserException;
}
