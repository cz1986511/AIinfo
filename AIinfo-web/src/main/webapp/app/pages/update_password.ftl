<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <#include "/common/header.html"/>
    <meta name="description" content="">
    <meta name="author" content="">
    <title>AIinfo - 修改登陆信息</title>
</head>
<body>
    <div id="wrapper">
		<#if userType == 1>
		  <#include "navigation1.html"/>
		<#else>
		  <#include "navigation.html"/>
        </#if>
        <div id="page-wrapper">
            <div class="container-fluid">
                <div class="row">
                    <div class="col-lg-12">
                        <h1 class="page-header">修改登陆密码</h1>
                    </div>
                    <form id="updatePassword" method="post">
                      <div class="form-group">
						<label for="oldPassword">旧密码</label>
						<input type="password" class="form-control" id="oldPassword" placeholder="Old Password">
					  </div>
					  <div class="form-group">
						<label for="newPassword">新密码</label>
						<input type="password" class="form-control" id="newPassword" placeholder="New Password">
					  </div>
					  <button type="button" id="btnUpdateP" class="btn btn-success">提交</button>
                    </form>
                </div>
				<div class="row">
                    <div class="col-lg-12">
                        <h1 class="page-header">修改登陆手机号</h1>
                    </div>
                    <form id="updateTelphone" method="post">
                      <div class="form-group">
						<label for="oldTelphone">旧手机号</label>
						<input type="text" class="form-control" id="oldTelphone" placeholder="Old Telphone">
					  </div>
					  <div class="form-group">
						<label for="newTelphone">新手机号</label>
						<input type="text" class="form-control" id="newTelphone" placeholder="New Telphone">
					  </div>
					  <button type="button" id="btnUpdateT" class="btn btn-success">提交</button>
                    </form>
                </div>
                <!-- /.row -->
            </div>
            <!-- /.container-fluid -->
        </div>
        <!-- /#page-wrapper -->
    </div>
    <!-- /#wrapper -->
    <script type="text/javascript">
      $("#btnUpdateP").click(function(){
	       var oldPassword = $("#oldPassword").val();
		   var newPassword = $("#newPassword").val();
	       $.ajax({
	           type: "POST",
               url: "password_update.html",
               data: {oldPassword:oldPassword, newPassword:newPassword},
               dataType: "json",
               success: function(data){
                 if(data.status == true){
                     alert("修改成功！");
					 window.location.reload();
                 } else {
                     alert(data.msg);
                 }
               }
	       });
	    });
		
		$("#btnUpdateT").click(function(){
	       var oldTelphone = $("#oldTelphone").val();
		   var newTelphone = $("#newTelphone").val();
		   if(!oldTelphone){
		       alert("原手机号不能为空!");
			   return false;
		   }
		   if(!newTelphone){
		       alert("新手机号不能为空!");
			   return false;
		   }
	       $.ajax({
	           type: "POST",
               url: "telphone_update.html",
               data: {oldTelphone:oldTelphone, newTelphone:newTelphone},
               dataType: "json",
               success: function(data){
                 if(data.status == true){
                     alert("修改成功！");
					 window.location.reload();
                 } else {
                     alert(data.msg);
                 }
               }
	       });
	    });
    </script>
    <#include "/common/footer.html" />
</body>
</html>