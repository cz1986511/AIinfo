<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <#include "/common/header.html"/>
    <meta name="description" content="">
    <meta name="author" content="">
    <title>读书签到</title>
</head>
<body>
    <div id="wrapper">
		<#include "navigation.html"/>
            <div class="container-fluid">
                    <h2 class="page-header">每日读书签到</h2>
                    <div class="form-group">
                        <input class="form-control" placeholder="输入签到内容" id="signContent" name="signContent" type="text" autofocus>
		     		</div>
                    <input class="btn btn-success" type="submit" id="btnSign" name="btnSign" value="签到"></input>	    
					<div class="">
                        <h3 class="page-header">今日签到统计</h3>
                    </div>
					<#if signList??>
					  <#list signList as sign>
						<div class="alert alert-info" role="alert">
							${sign.userName}:${sign.signInfo}--${(sign.gmtModify?string("yyyy-MM-dd HH:mm:ss"))!}
						</div>
					  </#list>
					</#if>
					<div class="">
                        <h3 class="page-header">最近30日签到</h3>
                    </div>
            </div>
    </div>
    <!-- /#wrapper -->
    <script type="text/javascript">
        $('#btnSign').click(function(){
	       var signInfo = $('#signContent').val();
		   if (!signInfo) {
		     alert("签到内容不能为空");
			 return false;
		   }
	       $.ajax({
	           type: "POST",
               url: "sign.action",
               data: {signInfo:signInfo},
               dataType: "json",
               success: function(data){
                 if(data.status == true){
                     location.reload(true);
                     alert("签到成功！");
                 } else {
                     location.reload(true);
                     alert(data.msg);
                 }
               }
	       });
	    });
    </script>
    <#include "/common/footer.html" />
</body>
</html>