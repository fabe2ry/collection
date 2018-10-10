package com.fabe2ry.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by xiaoxq on 2018/9/28.
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelSheetSetting {

    /**
     * sheet名
     * @return
     */
    String sheetName();

    /**
     * sheet的headre下标，默认0
     * @return
     */
    int headerRowIndex() default 0;

}
