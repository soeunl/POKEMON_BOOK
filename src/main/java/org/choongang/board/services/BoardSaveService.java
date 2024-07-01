package org.choongang.board.services;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.choongang.board.controllers.RequestBoardData;
import org.choongang.board.entities.BoardData;
import org.choongang.board.mappers.BoardDataMapper;
import org.choongang.board.validators.BoardSaveValidator;
import org.choongang.global.config.annotations.Service;
import org.choongang.global.config.containers.BeanContainer;
import org.choongang.member.MemberUtil;
import org.modelmapper.ModelMapper;

@Service
@RequiredArgsConstructor
//게시글 작성, 수정
public class BoardSaveService {

    private final MemberUtil memberUtil;
    private final BoardDataMapper mapper;
    private final BoardSaveValidator validator; //권한 체크

    public void save(RequestBoardData form){
        validator.check(form);

        String mode = form.getMode();
        mode = mode == null || mode.isBlank()? "write" : mode;

        BoardData data = new ModelMapper().map(form, BoardData.class);

        if(mode.equals("update")){ //수정


        } else { // 등록
            //현재 로그인한 회원 번호
            if(memberUtil.isLogin()) {
                data.setMemberSeq(memberUtil.getMember().getUserNo());
            }

            //작성자 환경 정보
            HttpServletRequest request = BeanContainer.getInstance().getBean(HttpServletRequest.class);
            String ua = request.getHeader("User-Agent"); //사용자 브라우저 정보 -> getHeader 통해 가져옴
            String ip = request.getRemoteAddr(); //작성자 IP 정보
            data.setUa(ua);
            data.setIp(ip);
        }

    }


}