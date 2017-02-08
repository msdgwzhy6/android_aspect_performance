package zane.com.performance_aop.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import zane.com.performance_aop.internal.DebugLog;
import zane.com.performance_aop.internal.TimeWatch;

/**
 * 计算方法耗时
 * Created by zhouzhan on 8/2/17.
 */
@Aspect
public class TraceAspect {

    @Pointcut("execution(* android.app.Activity.onCreate(..))")
    public void pointcutOnActivityCreate() {

    }

    @Around("pointcutOnActivityCreate()")
    public Object adviceOnActivityCreate(final ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();

        final TimeWatch timeWatch = new TimeWatch();
        timeWatch.start();
        Object result = joinPoint.proceed();
        timeWatch.stop();

        DebugLog.log(className, buildLogMessage(methodName, timeWatch.getTotalTimeMillis()));

        return result;
    }

    private static String buildLogMessage(String methodName, long methodDuration) {
        StringBuilder message = new StringBuilder();
        message.append("Gintonic --> ");
        message.append(methodName);
        message.append(" --> ");
        message.append("[");
        message.append(methodDuration);
        message.append("ms");
        message.append("]");

        return message.toString();
    }
}
