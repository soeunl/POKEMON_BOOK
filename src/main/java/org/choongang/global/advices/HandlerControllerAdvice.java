package org.choongang.global.advices;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.choongang.global.Interceptor;
import org.choongang.global.config.annotations.*;
import org.choongang.global.config.containers.BeanContainer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HandlerControllerAdvice {

    private final HttpServletRequest request;

    public boolean handle(Object controller) {
        Class clazz = controller.getClass();
        String pkName = clazz.getPackageName(); // 클래스 패키지 이름을 pkName 변수에 저장

        boolean isRest = Arrays.stream(clazz.getAnnotations()).anyMatch(a -> a instanceof RestController); // clazz 클래스에 @RestController 어노테이션이 있는지 확인하고 결과값은 isRest 변수에 저장
        List<Object> advices = getControllerAdvices(isRest); // isRest 값에 따라 적합한 컨트롤러 어드바이스 목록 가져오기
        List<Object> matchedAdvices = new ArrayList<>(); // 일치하는 컨트롤러 어드바이스를 저장할 matchedAdvices를 생성
        for (Object advice : advices) {
            Annotation[] annotations = advice.getClass().getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof ControllerAdvice anno) {
                    boolean isMatched = Arrays.stream(anno.value()).anyMatch(pkName::startsWith);
                    if (isMatched) {
                        matchedAdvices.add(advice);

                    } // controller 객체가 어떤 컨트롤러 클래스인지 파악하고, 해당 컨트롤러 클래스의 패키지 이름과 일치하는 @ControllerAdvice 어노테이션을 가진 객체(컨트롤러 어드바이스)를 찾는 과정을 수행..?
                }
            }
        }

        boolean isContinue = true;
        // 매칭된 어드바이스가 있다면
        if (!matchedAdvices.isEmpty()) {
            for (Object matchedAdvice : matchedAdvices) {
                // 인터셉터 체크
                if (matchedAdvice instanceof Interceptor interceptor) {
                    if(!interceptor.preHandle()) {
                        isContinue = false;
                    }
                }

                Method[] methods = matchedAdvice.getClass().getDeclaredMethods();
                for (Method method : methods) {
                    for (Annotation anno : method.getDeclaredAnnotations()) {
                        // 공통 유지할 속성 처리 S
                        if (anno instanceof ModelAttribute ma) {
                            try {
                                String name = ma.value().isBlank() ? method.getName() : ma.value().trim();
                                Object value = method.invoke(matchedAdvice);
                                request.setAttribute(name, value);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        // 공통 유지할 속성 처리 E
                    } // endfor
                } // endfor
            }
        }

        return isContinue;
    }

    public List<Object> getControllerAdvices(boolean isRest) { // BeanContainer에서 모든 빈 객체를 가져와서 @ControllerAdvice 또는 @RestControllerAdvice 애노테이션이 설정된 것만 필터링하여 리스트 형태로 반환.isRest 값에 따라 일반 컨트롤러와 REST API 컨트롤러에 적합한 컨트롤러 어드바이스를 구분하여 반환

        return BeanContainer.getInstance()
                .getBeans()
                .values()
                .stream()
                .filter(b -> Arrays.stream(b.getClass().getAnnotations()).anyMatch(a -> (!isRest && a instanceof ControllerAdvice) || (isRest && a instanceof RestControllerAdvice)))
                .toList();
    }
}