package org.choongang.global.router;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.choongang.global.config.containers.BeanContainer;

import java.io.IOException;
import java.util.List;

@WebServlet("/")
public class DispatcherServlet extends HttpServlet  {
// 서블릿 요청을 처리하는 역할을 수행

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        BeanContainer bc = BeanContainer.getInstance();
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        // service 메서드를 통해 ServletRequest 와 ServletResponse 객체를 받고, 받은 객체들을 실제 HTTP 요청/응답 객체인 HttpServletRequest 와 HttpServletResponse 객체로 형 변환함

        bc.addBean(HttpServletRequest.class.getName(), request);
        bc.addBean(HttpServletResponse.class.getName(), response);
        bc.addBean(HttpSession.class.getName(), request.getSession());
        // 빈(bean)으로 등록
        bc.loadBeans();
        // loadBeans 메서드를 호출하여 필요한 클래스 객체들을 로딩
        RouterService service = bc.getBean(RouterService.class);
        service.route(request, response);
        // RouterService 객체를 이용하여 실제 요청을 처리하는 컨트롤러를 찾아 실행
    }

    /**
     * css, js, image 파일 요청이 아닌지 체크
     *
     * @param request
     * @return
     */
    private boolean check(HttpServletRequest request) {
        String uri = request.getRequestURI().toLowerCase();
        List<String> excludeExtensions = List.of(".css", ".js", ".png", ".jpg", ".jpeg", ".gif"); // 요청 URI가 CSS, JS, 이미지 등의 정적 파일 요청인지 여부를 확인

        return excludeExtensions.stream().noneMatch(s -> uri.endsWith(s));
    }
}
