package com.library.treerecyclerview.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by fula on 2018/7/20.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TreeItemClass {

    /**
     * 直接绑定itemclass
     *
     * @return
     */
    Class iClass() default Object.class;
}
