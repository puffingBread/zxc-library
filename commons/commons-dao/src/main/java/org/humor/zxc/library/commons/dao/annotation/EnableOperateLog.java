package org.humor.zxc.library.commons.dao.annotation;

import org.humor.zxc.library.commons.dao.config.InterceptorConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * TODO ADD DESCRIPTION
 * Date: 2020/8/16
 * Time: 10:18 下午
 *
 * @author xuzz
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(InterceptorConfiguration.class)
@Documented
public @interface EnableOperateLog {
    
}
