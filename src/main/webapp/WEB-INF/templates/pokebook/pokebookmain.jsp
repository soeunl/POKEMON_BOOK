<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<fmt:setBundle basename="messages.pokebook" />
<fmt:message var="pageTitle" key="도감_메인페이지"/>
<c:url var="pokebook" value="/pokebookmain" />
<c:url var="pokebooksubUrl" value="/pokebook/pokebooksub" />

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/pokebookmain.css">
<layout:main title="${pageTitle}">

    <div class="bookMain">
        <c:forEach var="i" begin="1" end="100">
            <div class="pokemonBox">
                    <a href="${pokebooksubUrl}">
                    <img src="https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${i}.png" class="pokemon">
                </a>
            </div>
        </c:forEach>
    </div>
</layout:main>
