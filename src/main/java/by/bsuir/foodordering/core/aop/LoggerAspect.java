package by.bsuir.foodordering.core.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggerAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggerAspect.class);

    @Pointcut("@annotation(by.bsuir.foodordering.core.annotation.Timed)")
    public void logTimePointcut() {
    }

    @Around("logTimePointcut()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {

        long startTime = System.currentTimeMillis();

        Object proceed = joinPoint.proceed();

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        logger.info("Функция '{}' класса '{}' выполнена за {} мс",
                joinPoint.getSignature().getName(),
                joinPoint.getTarget().getClass().getSimpleName(),
                executionTime);

        return proceed;
    }

    @Pointcut("execution(* by.bsuir.foodordering.*.*.*(..))")
    public void methodsExecuted() {
    }

    @Around("methodsExecuted()")
    public Object logMethodEntry(ProceedingJoinPoint joinPoint) throws Throwable {

        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();

        logger.info(">>> Method Called: {}.{}()", className, methodName);

        return joinPoint.proceed();
    }
}