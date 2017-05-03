<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>结果管理</title>
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
    <li ><a href="${ctx}/exam/exam/tExamList">题目列表</a></li>
    <li class="active"><a href="#">结果列表</a></li>
    <%--<li><a href="${ctx}/result/result/form">结果添加</a></li>--%>
</ul>
<form:form id="searchForm" modelAttribute="result" action="${ctx}/result/result/" method="post"
           class="breadcrumb form-search">
    <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
    <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
    <ul class="ul-form">
        <li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查重"/></li>
        <li class="clearfix"></li>
    </ul>
</form:form>
<sys:message content="${message}"/>
<table id="contentTable" class="table table-striped table-bordered table-condensed">
    <thead>
    <tr>
        <th>姓名</th>
        <th>编译</th>
        <th>超时</th>
        <th>结果</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${page.list}" var="result">
        <tr>
            <td><a href="${ctx}/result/result/tGetResult?id=${result.id}">
                    ${result.uid}
            </a></td>
            <td>
                    ${fns:getDictLabel(result.compile, 'compile', '')}
            </td>
            <td>
                    ${fns:getDictLabel(result.timeout, 'timeout', '')}
            </td>
            <td>
                    ${fns:getDictLabel(result.answer, 'answer', '')}
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<div class="pagination">${page}</div>
</body>
</html>