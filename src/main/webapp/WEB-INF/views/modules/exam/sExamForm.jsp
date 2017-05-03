<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>做题</title>
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
    <li class="active"><a href="${ctx}/exam/exam/sExamForm?id=${exam.id}">题目要求</a></li>
</ul>
<br/>
<form:form id="inputForm" modelAttribute="exam" action="${ctx}/result/result/createResult" method="post" class="form-horizontal">
    <form:hidden path="id"/>
    <sys:message content="${message}"/>
    <div class="control-group">
        <label class="control-label">题目名称：</label>
        <div class="controls">
                ${exam.ename}
            <span class="help-inline"></span>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">题目内容：</label>
        <div class="controls">
                ${exam.detail}
            <span class="help-inline"></span>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">题目语言：</label>
        <div class="controls">
                ${fns:getDictLabel(exam.lan, 'language', '')}
            <span class="help-inline"></span>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">输入参数：</label>
        <div class="controls">
                ${exam.inArgsType}
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">输出参数：</label>
        <div class="controls">
                ${exam.outArgsType}
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">提交截止时间：</label>
        <div class="controls">
                ${exam.deadline}
            <span class="help-inline"></span>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">备注信息：</label>
        <div class="controls">
                ${exam.remarks}
            <span class="help-inline"></span>
        </div>
    </div>

    <div class="form-actions">
        <input id="btnSubmit" class="btn btn-primary" type="submit" value="查看成绩"/>&nbsp;
        <%--<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>--%>
    </div>
</form:form>


</body>
</html>