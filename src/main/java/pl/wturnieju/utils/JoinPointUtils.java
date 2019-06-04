package pl.wturnieju.utils;

import java.lang.annotation.Annotation;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.NONE)
public class JoinPointUtils {

    public static <T extends Annotation> T getAnnotation(JoinPoint joinPoint, Class<T> annotation) {
        return getMethodSignature(joinPoint).getMethod().getAnnotation(annotation);
    }

    public static MethodSignature getMethodSignature(JoinPoint joinPoint) {
        return (MethodSignature) joinPoint.getSignature();
    }

    public static Object getParameterValue(JoinPoint joinPoint, Class<? extends Annotation> annotation) {
        var index = getParameterIndex(joinPoint, annotation);
        if (index == null) {
            return null;
        }

        return joinPoint.getArgs()[index];
    }

    public static Integer getParameterIndex(JoinPoint joinPoint, Class<? extends Annotation> annotation) {
        var parameters = getMethodSignature(joinPoint).getMethod().getParameters();
        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].getAnnotation(annotation) != null) {
                return i;
            }
        }
        return null;
    }
}
