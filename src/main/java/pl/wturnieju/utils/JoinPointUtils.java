package pl.wturnieju.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Optional;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.NONE)
public class JoinPointUtils {

    public static <T extends Annotation> T getAnnotation(JoinPoint joinPoint, Class<T> annotation) {
        return getMethod(joinPoint).getAnnotation(annotation);
    }

    private static Method getMethod(JoinPoint joinPoint) {
        return getMethodSignature(joinPoint).getMethod();
    }

    public static MethodSignature getMethodSignature(JoinPoint joinPoint) {
        return (MethodSignature) joinPoint.getSignature();
    }

    public static Object getParameterValue(JoinPoint joinPoint, Class<? extends Annotation> annotation) {
        return findParameterIndex(joinPoint, annotation)
                .map(index -> joinPoint.getArgs()[index])
                .orElse(null);
    }

    public static Optional<Integer> findParameterIndex(JoinPoint joinPoint, Class<? extends Annotation> annotation) {
        var parameters = getMethod(joinPoint).getParameters();
        for (int i = 0; i < parameters.length; i++) {
            if (hasAnnotation(parameters[i], annotation)) {
                return Optional.of(i);
            }
        }
        return Optional.empty();
    }

    private static boolean hasAnnotation(Parameter parameter, Class<? extends Annotation> annotation) {
        return parameter.getAnnotation(annotation) != null;
    }
}
