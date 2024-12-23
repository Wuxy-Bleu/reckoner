package demo.usul.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 会给method第一个参数create bf key, add ids
 * method param1 必须是collection, 并且collection中每一个element必须有id字段
 * method必须是reactive的，也就是返回类型必须是Mono or Flux
 * key name 默认前缀+方法名+参数名
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BloomFilterReactive {

    double errRate() default 0.01;

    long capacity() default 100;

    // id getter的SpEL表达式
    String idGetter() default "getId()";

    String key() default "";
}
