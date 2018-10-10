package com.fabe2ry.annotation;


import com.fabe2ry.type.ExcelColumnType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by xiaoxq on 2018/9/28.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelColumnSetting {

    /**
     * 该列列名
     * @return
     */
    String columnName();

    /**
     * 该列需要的数据类型
     */
    ExcelColumnType columnType();

    /**
     * 该列下标，默认-1，没有设置
     * @return
     */
    int columnIndex() default -1;

    /**
     * 该列是否可以为空
     * @return
     */
    boolean allowEmpty() default false;
//    TODO:允许为空的话，要设置默认值

    /**
     * 该列导出的顺序
     * @return
     */
    int exportOrder() default 99;

}
