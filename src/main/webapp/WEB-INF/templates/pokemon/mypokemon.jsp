<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<c:url var="actionUrl" value="/pokemon/mypokemon" />
<c:url var="popupUrl" value="/pokemon/gacha" />
<layout:main title="시원초이">
    <section class="layout-width">
<div class="mypokemon-buttons">
</div>
         <form name="frmSave" method="POST" action="${actionUrl}" target="ifrmProcess" autocomplete="off">
            <dl>
                <dd>
                    <c:if test="${myProfile != null}">
                       <div class='profile'>
                            <img src="${myProfile.frontImage}" alt="${myProfile.nameKr}">
                            <div>${myProfile.nameKr}</div>
                       </div>
                    </c:if>
                    <button type='button' id="generate-profile-image" onclick="commonLib.popup.open('${popupUrl}', 650, 650);">
                    랜덤 포켓몬 뽑기
                    </button>
                </dd>
            </dl>
        </form>
         <jsp:include page="_my_pokemon.jsp" />
    </section>
</layout:main>