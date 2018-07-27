package com.cn.tfl.aspectjlib.anotation;

import android.content.Context;
import android.widget.Toast;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Created by Happiness on 2017/3/16.
 */
@Aspect
public class TestAspect {

    @Pointcut("execution(@TestAspectJ private * *..*.*(..)) && @annotation(name)")
    public void testAspect(TestAspectJ name) {
    }

    @Before("testAspect(TestAspectJ)")
    public void testTestAspect(JoinPoint joinPoint) {
        Toast.makeText((Context) joinPoint.getTarget(),"AOP TEST SUCCESS",Toast.LENGTH_SHORT).show();

    }

}
