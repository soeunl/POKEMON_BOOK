package org.choongang.member.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.choongang.global.config.annotations.Controller;
import org.choongang.global.config.annotations.GetMapping;
import org.choongang.global.config.annotations.PostMapping;
import org.choongang.global.config.annotations.RequestMapping;
import org.choongang.member.services.JoinService;
import org.choongang.member.services.LoginService;
import org.choongang.member.services.ModifyService;
import org.choongang.member.services.MypageService;

import java.util.List;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final JoinService joinService;
    private final LoginService loginService;
    private final MypageService mypageService;
    private final ModifyService modifyService;

    // 회원 가입 양식
    @GetMapping("/join")
    public String join(HttpServletRequest request) {

        request.setAttribute("addCss", List.of("join"));
        return "member/join";
    }

    // 회원 가입 처리
    @PostMapping("/join")
    public String joinPs(RequestJoin form, HttpServletRequest request) {

        joinService.process(form);

        String url = request.getContextPath() + "/member/login";
        String script = String.format("parent.location.replace('%s');", url);

        request.setAttribute("script", script);

        return "commons/execute_script";
    }

    // 로그인 양식
    @GetMapping("/login")
    public String login(HttpServletRequest request) {

        request.setAttribute("addCss", List.of("login"));
        return "member/login";
    }

    // 로그인 처리
    @PostMapping("/login")
    public String loginPs(RequestLogin form, HttpServletRequest request) {

        loginService.process(form);

        String redirectUrl = form.getRedirectUrl();
        redirectUrl = redirectUrl == null || redirectUrl.isBlank() ? "/" : redirectUrl;

        String script = String.format("parent.location.replace('%s');", request.getContextPath() + redirectUrl);

        request.setAttribute("script", script);


        return "commons/execute_script";
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 세션 비우기 : 로그 아웃

        return "redirect:/"; // 페이지 이동 response.sendRedirect(...)
    }

    //마이페이지
    @GetMapping("/mypage")
    public String mypage(RequestMypage form, HttpServletRequest request) {
        request.setAttribute("form", form);
        request.setAttribute("addCss", List.of("mypage"));

        return "member/mypage";
    }

    // 회원정보 수정 양식
    @GetMapping("/modify")
    public String modify(HttpServletRequest request) {

        request.setAttribute("addCss", List.of("modify"));
        return "member/modify";
    }

    // 회원정보 수정 처리
    @PostMapping("/modify")
    public String modifyPs(RequestModify form, HttpServletRequest request) {

        modifyService.process(form);

        String url = request.getContextPath() + "/member/mypage";
        //정보 수정이 완료되면 마이페이지 화면으로 돌아가서 수정 정보 확인할 수 있게 함
        String script = String.format("parent.location.replace('%s');", url);

        request.setAttribute("script", script);

        return "commons/execute_script";
    }


    //마이보드 작성글 조회, 오류 발생 시 지워주세요..
    @GetMapping("/myboard")
    public String myboard(RequestMyboard form, HttpServletRequest request) {
        request.setAttribute("form", form);
        request.setAttribute("addCss", List.of("myboard"));

        return "member/myboard";
    }
}
