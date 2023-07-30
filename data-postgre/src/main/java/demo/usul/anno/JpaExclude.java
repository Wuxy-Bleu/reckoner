package demo.usul.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// only use for exclude some field to be print when using gson.tojson
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface JpaExclude {

}
