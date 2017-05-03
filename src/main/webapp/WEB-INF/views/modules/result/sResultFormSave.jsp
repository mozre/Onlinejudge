<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>结果管理</title>
    <meta name="decorator" content="default"/>
    <script type="text/javascript">
        $(document).ready(function () {
            //$("#name").focus();
            $("#inputForm").validate({
                submitHandler: function (form) {
                    loading('正在跳转，请稍等...');
                    form.submit();
                },
                errorContainer: "#messageBox",
                errorPlacement: function (error, element) {
                    $("#messageBox").text("输入有误，请先更正。");
                    if (element.is(":checkbox") || element.is(":radio") || element.parent().is(".input-append")) {
                        error.appendTo(element.parent().parent());
                    } else {
                        error.insertAfter(element);
                    }
                }
            });
        });
    </script>
</head>
<body>
<ul class="nav nav-tabs">
    <li><a href="${ctx}/exam/exam/sExamList">我的题目</a></li>
    <li><a href="${ctx}/exam/exam/sExamForm?id=${result.eid}">题目要求</a></li>
    <li class="active"><a href="#">查看结果</a></li>
</ul>
<br/>
<form:form id="inputForm" modelAttribute="result" action="${ctx}/result/result/save" method="post"
           class="form-horizontal">
    <form:hidden path="id"/>
    <form:hidden path="uid"/>
    <form:hidden path="eid"/>
    <form:hidden path="compile"/>
    <form:hidden path="timeout"/>
    <form:hidden path="answer"/>
    <form:hidden path="remarks"/>
    <sys:message content="${message}"/>

    <div class="control-group">
        <label class="control-label">我的代码：</label>
        <div class="controls">
            <form:textarea path="code" htmlEscape="false" rows="10" cols="40" maxlength="10240" class="input-xxlarge "
                           readonly="true"/>
        </div>
    </div>

    <div class="control-group">
        <label class="control-label">编译：</label>
        <div class="controls">
                ${fns:getDictLabel(result.compile, 'compile', '')}
            <span class="help-inline"></span>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">超时：</label>
        <div class="controls">
                ${fns:getDictLabel(result.timeout, 'timeout', '')}
            <span class="help-inline"></span>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">结果：</label>
        <div class="controls">
                ${fns:getDictLabel(result.answer, 'answer', '')}
            <span class="help-inline"></span>
        </div>
    </div>

    <div class="control-group">
        <label class="control-label">错误信息：</label>
        <div class="controls">
            <form:textarea path="remarks" htmlEscape="false" rows="4" cols="40" maxlength="10240" class="input-xxlarge "
                           readonly="true"/>
        </div>
    </div>

    <div class="form-actions">
        <input id="btnSubmit" class="btn btn-primary" type="submit" value="保存结果"/>&nbsp;
    </div>
</form:form>
</body>
</html>