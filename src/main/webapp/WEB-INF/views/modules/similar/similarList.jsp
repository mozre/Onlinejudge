<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>查重管理</title>
    <meta name="decorator" content="default"/>
    <script type="text/javascript">
        $(document).ready(function () {

        });
        function page(n, s) {
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            $("#searchForm").submit();
            return false;
        }
    </script>
</head>
<body>
<ul class="nav nav-tabs">
    <li><a href="${ctx}/exam/exam/tExamList">题目列表</a></li>
    <li class="active"><a href="${ctx}/similar/similar/">查重列表</a></li>
</ul>
<form:form id="searchForm" modelAttribute="similar" action="${ctx}/similar/similar/" method="post"
           class="breadcrumb form-search">
    <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
    <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
    <%--<ul class="ul-form">--%>
        <%--<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>--%>
        <%--<li class="clearfix"></li>--%>
    <%--</ul>--%>
</form:form>
<sys:message content="${message}"/>
<table id="contentTable" class="table table-striped table-bordered table-condensed">
    <thead>
    <tr>
        <th>学生1姓名</th>
        <th>学生2姓名</th>
        <th>相似率</th>
        <th>更新时间</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${page.list}" var="similar">
        <tr>
            <td>
                    ${similar.uid1}
            </td>
            <td>
                    ${similar.uid2}
            </td>
            <td>
                    ${similar.similarRate}
            </td>
            <td>
                <fmt:formatDate value="${similar.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
    </c:forEach>
    </tbody>
</table>
<div class="pagination">${page}</div>
</body>
</html>