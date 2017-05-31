<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>用户签到</title>
    <#include "/common/header.html"/>
</head>
<body>
    <div id="wrapper">
        <#if userType == 1>
		  <#include "navigation1.html"/>
		<#else>
		  <#include "navigation.html"/>
        </#if>
        <!-- Page Content -->
        <div id="page-wrapper">
            <div class="container-fluid">
                <div class="row">
                    <div class="col-lg-12">
                        <h1 class="page-header">每日读书签到</h1>
                    </div>
                    <div class="form-group">
                        <input class="form-control" placeholder="输入签到内容" id="signContent" name="signContent" type="text" autofocus>
		     		</div>
                    <input class="btn btn-lg btn-success btn-block" type="submit" id="btnLoad" name="btnLoad" value="签到"></input>	    
					<div class="col-lg-12">
                        <h1 class="page-header">今日签到统计</h1>
                    </div>
					<table class="table">
						<tr>
						  <th>姓名</th>
						  <th>签到内容</th>
						  <th>签到时间</th>
						</tr>
						<#if signList??>
						<#list signList as sign>
						  <tr>
							<td>${sign.userName}</td>
							<td>${sign.signInfo}"</td>
							<td>${(sign.gmtModify?string("yyyy-MM-dd HH:mm:ss"))!}</td>
						  </tr>
						</#list>
					  </#if>
					</table>
					<div class="col-lg-12">
                        <h1 class="page-header">最近30日签到</h1>
                    </div>
                </div>
                <!-- /.row -->
            </div>
            <!-- /.container-fluid -->
        </div>
        <!-- /#page-wrapper -->
    </div>
    <!-- /#wrapper -->
    <script type="text/javascript">
        $('#btnLoad').click(function(){
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