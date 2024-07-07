package org.choongang.global.config;

import org.apache.ibatis.session.SqlSession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class MapperProxyHandler implements InvocationHandler {

    private final Class clz;
    public MapperProxyHandler(Class clz) {
        this.clz = clz;
    }
    private Object obj;
    private SqlSession session = DBConn.getSession();
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws // 동적 프록시 객체가 메서드를 호출할 때 실제 실행되는 로직을 담고 있음
            Throwable { 

        session.clearCache(); // 매 요청 처리 전에 MyBatis 세션의 캐시를 초기화

        // 매 요청 1번만 객체 갱신!
        if (obj == null) { // 필드 변수 obj가 null인 경우 (처음 호출 시)에만 실행
            obj = session.getMapper(clz);
        }
        Object result = null;
        try { // 예외가 발생하면 새로운 세션과 Mapper 객체를 다시 생성하여 작업을 재시도함
            result = method.invoke(obj, args);
        } catch (Exception e) {
            session = DBConn.getSession();
            obj = session.getMapper(clz);
            result = method.invoke(obj, args);
        }
        return result;
    }
}