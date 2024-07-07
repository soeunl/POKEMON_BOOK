package org.choongang.global.config;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * 사이트 설정 로드 및 조회
 *
 */
public class AppConfig { // 애플리케이션 설정 정보를 관리하는 역할을 수행함
    private final static ResourceBundle bundle;
    private final static Map<String, String> configs;
    static {
        // 환경 변수 mode에 따라 설정파일을 분리 예) prod이면 application-prod.properties로 읽어온다.
        String mode = System.getenv("mode");
        mode = mode == null || mode.isBlank() ? "":"-" + mode;

        bundle = ResourceBundle.getBundle("application" + mode);
        configs = new HashMap<>();
        Iterator<String> iter = bundle.getKeys().asIterator();
        while(iter.hasNext()) {
            String key = iter.next();
            String value = bundle.getString(key);
            configs.put(key, value);
        }
    }

    public static String get(String key) {
        return configs.get(key);
    }
}

//AppConfig 클래스는 시스템 환경 변수 "mode" 값을 이용하여 해당 모드에 맞는 리소스 번들 파일을 로딩함
//로드된 리소스 번들 파일의 내용을 키-값 형태로 configs 맵에 저장하여 애플리케이션 설정 정보를 관리함
