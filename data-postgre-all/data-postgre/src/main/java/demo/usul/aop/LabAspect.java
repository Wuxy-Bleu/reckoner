package demo.usul.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class LabAspect {

    @Pointcut("execution(* demo.usul.controller.LabController.*(..))")
    private void pointCut(){}

    @Before("pointCut()")
    public void logs(JoinPoint jp){
        log.info("hit point cut");
    }
}
