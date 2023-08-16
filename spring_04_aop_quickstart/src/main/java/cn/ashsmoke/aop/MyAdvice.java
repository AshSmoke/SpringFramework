package cn.ashsmoke.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Aspect
public class MyAdvice {
    @Pointcut("execution(void cn.ashsmoke.dao.BookDao.update())")
    private void pt(){}
    @Before("pt()")
    public void method(){
        System.out.println(System.currentTimeMillis());
    }
    @Pointcut("execution(String cn.ashsmoke.dao.impl.BookDaoImpl.sendId(*))")
    private void sendIdPt(){}

    @Before("sendIdPt()")
    public void before(JoinPoint jp){
        Object[] args = jp.getArgs();
        System.out.println(Arrays.toString(args));
    }
    @Around("sendIdPt()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable{
        Object[] args = pjp.getArgs();
        System.out.println(Arrays.toString(args));
        Object ret = pjp.proceed();
        return ret;
    }
    @AfterReturning(value = "sendIdPt()",returning = "ret")
    public void afterReturning(Object ret){
        System.out.println("afterReturning advice ..."+ret);
    }
    @AfterThrowing(value = "sendIdPt()",throwing = "th")
    public void afterThrowing(Throwable th){
        System.out.println("afterThrowing advice ..."+th);
    }
}
