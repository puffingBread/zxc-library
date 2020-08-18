package org.humor.zxc.library.commons.dao.translator;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Column;
import org.apache.commons.lang3.StringUtils;
import org.humor.zxc.library.commons.dao.dto.OperateLogDTO;
import org.humor.zxc.library.commons.dao.dto.req.SqlLogAddQuery;

import java.util.Collections;
import java.util.List;

/***
 *  Date: 2019/8/28
 *  Time: 11:56
 *  @author xuzz
 */
public class Translator {

    private TranslatorFactory translatorFactory;

    public Translator(TranslatorFactory translatorFactory) {
        this.translatorFactory = translatorFactory;
    }

    public List<OperateLogDTO> translate(SqlLogAddQuery addQuery) throws JSQLParserException {
        if (StringUtils.isBlank(addQuery.getOperateType()) || StringUtils.isBlank(addQuery.getSql())) {
            return Collections.emptyList();
        }

        TranslatorService translatorService = translatorFactory.getTranslatorService(addQuery.getOperateType());
        return translatorService.translate(addQuery);
    }

    static String buildContent(List<Column> columns, List<Expression> expressions) {
        StringBuilder contentBuilder = new StringBuilder("{");
        for (int index = 0; index < columns.size(); index++) {
            String name = com.baomidou.mybatisplus.core.toolkit.StringUtils.underlineToCamel(columns.get(index).getColumnName());
            String value = expressions.get(index).toString();
            contentBuilder.append("\"").append(name).append("\":")
                    .append(value.replaceAll("'", "\""));
            if (index < columns.size() - 1) {
                contentBuilder.append(", ");
            }
        }
        contentBuilder.append("}");
        return contentBuilder.toString();
    }
}
