package org.humor.zxc.library.commons.dao.translator;

import com.google.common.collect.ImmutableList;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.MultiExpressionList;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.insert.Insert;
import org.humor.zxc.library.commons.dao.dto.OperateLogDTO;
import org.humor.zxc.library.commons.dao.dto.req.SqlLogAddQuery;
import org.humor.zxc.library.commons.dao.enums.OperateTypeEnum;
import org.humor.zxc.library.commons.util.utils.BeanUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/***
 *  Date: 2019/8/28
 *  Time: 12:01
 *  @author xuzz
 */
public class InsertTranslatorImpl implements TranslatorService {

    @Override
    public List<OperateLogDTO> translate(SqlLogAddQuery addQuery) throws JSQLParserException {
        String type = addQuery.getOperateType();
        String sql = addQuery.getSql();

        Statement statement = CCJSqlParserUtil.parse(sql);
        Insert insert = (Insert) statement;
        List<ExpressionList> expressions;
        if (insert.getItemsList() instanceof MultiExpressionList) {
            expressions = ((MultiExpressionList) insert.getItemsList()).getExprList();
        } else {
            expressions = ImmutableList.of((ExpressionList) insert.getItemsList());
        }

        List<OperateLogDTO> list = new ArrayList<>();
        for (ExpressionList expression : expressions) {
            OperateLogDTO operateLog = BeanUtils.copyProperties(addQuery, OperateLogDTO::new);
            operateLog.setOperateType(OperateTypeEnum.valueOf(type));
            operateLog.setContent(Translator.buildContent(insert.getColumns(), expression.getExpressions()));
            operateLog.setTableId(addQuery.getTableId() == null ? 0L : addQuery.getTableId());
            operateLog.setOperateTime(new Date());
            list.add(operateLog);
        }

        return list;
    }

}
