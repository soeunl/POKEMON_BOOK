package org.choongang.global.config.containers.mybatis;

import org.choongang.global.config.MapperProxyHandler;
import org.choongang.global.config.annotations.mybatis.MapperScan;

import java.lang.reflect.Proxy;
import java.util.Arrays;

@MapperScan({
        "org.choongang.member.mappers",
        "org.choongang.pokemon.mappers",
        "org.choongang.board.mappers",
        "org.choongang.file.mappers"
})
public class MapperProvider {

    public static MapperProvider instance;

    private MapperProvider() {}

    public static MapperProvider getInstance() {
        if (instance == null) {
            instance = new MapperProvider();
        }
        return instance;
    }

    // 입력받은 클래스가 Mapper 인터페이스인지 확인하고, @MapperScan 어노테이션을 통해 설정된 패키지 경로와 일치하는지 확인하고, 조건이 일치하면 동적 프록시를 이용하여 Mapper 인터페이스를 구현하는 객체를 생성함

    @SuppressWarnings("unchecked")
    public <T> T getMapper(Class clz) { //  주어진 클래스 정보 (clz)를 이용하여 Mapper 인터페이스 객체를 생성하는 역할
        if (!clz.isInterface()) {
            return null;
        }

        MapperScan mapperScan = getClass().getAnnotation(MapperScan.class);
        boolean isMapper = Arrays.stream(mapperScan.value()).anyMatch(s -> s.startsWith(clz.getPackageName()));

        if (isMapper) {

            // 입력받은 클래스 정보 (clz)를 기반으로 JDK의 동적 프록시 클래스를 생성
            return (T) Proxy.newProxyInstance(
                    clz.getClassLoader(),
                    new Class[] { clz },
                    new MapperProxyHandler(clz)
            );
        }

        return null;
    }
}