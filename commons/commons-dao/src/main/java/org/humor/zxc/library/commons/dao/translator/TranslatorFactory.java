package org.humor.zxc.library.commons.dao.translator;

import java.util.Collections;

/***
 *  Date: 2019/8/28
 *  Time: 11:57
 *  @author xuzz
 */
public class TranslatorFactory {

    private TranslatorService insertTranslator;

    private TranslatorService updateTranslator;

    public TranslatorFactory(TranslatorService insertTranslator, TranslatorService updateTranslator) {
        this.insertTranslator = insertTranslator;
        this.updateTranslator = updateTranslator;
    }

    public TranslatorService getTranslatorService(String operateType) {
        switch (operateType) {
            case "INSERT":
                return insertTranslator;
            case "UPDATE":
            case "REMOVE":
                return updateTranslator;
            default:
                return addQuery -> Collections.emptyList();
        }
    }
}
