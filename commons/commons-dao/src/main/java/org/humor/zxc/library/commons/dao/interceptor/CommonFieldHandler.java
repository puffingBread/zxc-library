package org.humor.zxc.library.commons.dao.interceptor;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;

/**
 * 公共属性自动填充
 * Date: 2021/12/13
 * Time: 4:29 PM
 *
 * @author xuzz
 */
public class CommonFieldHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        Date currentDate = new Date();
        this.setFieldValByName("createTime", currentDate, metaObject);
        this.setFieldValByName("updateTime", currentDate, metaObject);
        this.setFieldValByName("deleted", 0, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime", new Date(), metaObject);
    }
}
