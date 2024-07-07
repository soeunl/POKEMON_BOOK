package org.choongang.global.advices;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.choongang.global.config.annotations.ControllerAdvice;
import org.choongang.global.config.annotations.ModelAttribute;
import org.choongang.global.exceptions.*;
import org.choongang.member.MemberUtil;
import org.choongang.member.entities.Member;
import org.choongang.pokemon.entities.PokemonDetail;

import java.util.List;

@RequiredArgsConstructor // Lombok에서 제공하는 어노테이션으로, final 또는 @NonNull 으로 선언된 필드들을 매개변수로 갖는 생성자를 자동으로 생성해줌
@ControllerAdvice("org.choongang") // 전역적인 예외 처리와 공통적인 컨트롤러 로직을 관리하는 데 사용
public class CommonControllerAdvice {

    private final MemberUtil memberUtil;

    @ModelAttribute // 사용자로부터 받은 입력 데이터를 객체 형태로 만들어 컨트롤러에서 손쉽게 활용할 수 있도록 해주는 역할
    public boolean isLogin() {
        return memberUtil.isLogin();
    }

    @ModelAttribute
    public boolean isAdmin() {
        return memberUtil.isAdmin();
    }

    @ModelAttribute
    public Member loggedMember() {
        return memberUtil.getMember();
    }

    @ModelAttribute
    public PokemonDetail myProfile() {
        return memberUtil.getMyProfile();
    }

    @ModelAttribute
    public List<Long> myPokemonSeqs() {
        return memberUtil.getMyPokemonSeqs();
    }

    /**
     * 공통 에러 페이지 처리
     *
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(Exception.class) //  Exception 및 그 하위 클래스의 예외를 처리한다고 지정
    public String errorHandler(Exception e, HttpServletRequest request, HttpServletResponse response) {

        e.printStackTrace();

        if (e instanceof CommonException commonException) {
            int status = commonException.getStatus();
            response.setStatus(status); // 응답 상태코드 설정

            StringBuffer sb = new StringBuffer(1000);
            // 조건 중 하나라도 충족되면 스크립트가 StringBuffer에 작성됨

            if (e instanceof AlertException) {
                sb.append(String.format("alert('%s');", e.getMessage()));
            } // 자바스크립트로 경고 메시지를 띄움

            if (e instanceof AlertBackException alertBackException) {
                String target = alertBackException.getTarget();
                sb.append(String.format("%s.history.back();", target));
            } // 사용자를 이전 히스토리로 이동

            if (e instanceof AlertRedirectException alertRedirectException) {
                String target = alertRedirectException.getTarget();
                String url = alertRedirectException.getRedirectUrl();

                sb.append(String.format("%s.location.replace('%s');", target, url)); // 사용자를 리디렉션함
            }

            if (!sb.isEmpty()) {
                request.setAttribute("script", sb.toString());
                return "commons/execute_script";
                // 스크립트가 작성된 경우 request.setAttribute("script", sb.toString())를 사용하여 작성된 스크립트를 "script"라는 이름의 요청 속성에 설정함
            }
        } else {
            // CommonException으로 정의한 예외가 아닌 경우 - 응답 코드 500
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        return "errors/error";
    }
}