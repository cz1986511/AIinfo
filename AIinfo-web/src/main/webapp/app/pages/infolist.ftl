<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>机器列表页</title>
    <#include "/common/header.html"/>
</head>
<body>
    <div id="wrapper">
	  <#include "navigation.html"/>
      <div id="page-wrapper">
        <div class="container-fluid">
          <div class="row">
            <div class="col-lg-12">
			  <h2>机器列表</h2>
			</div>
			  <form name="schedule" action="scheduleAction.html" method="get">
			  <table border=1>
			    <tr>
			      <th width=50>ID</th>
			      <th width=100>机器名</th>
			      <th width=100>IP</th>
			      <th width=100>端口</th>
			      <th width=60>状态</th>
			      <th width=400>描述</th>
			      <th width=400>操作</th>
			    </tr>
			    <#if jobList??>
			    <#list jobList as info>
			      <tr>
			        <td><input id="id" name="id" class="input-id" value="${info.id}" /></td>
			        <td><input id="name" name="name" value="${info.name}" /></td>
			        <td><input id="ip" name="ip" value="${info.ip}" /></td>
			        <td><input id="port" name="port" value="${info.port}" /></td>
			        <td id="status" name="status" value="${info.status}">${info.status}</td>
			        <td id="desc" name="infoDesc" value="${info.infoDesc}">${info.desc}</td>
			        <td>
			          <a href="/AIinfo/switch_list.html?ip=${info.ip}&name=${info.name}&port=${info.port}">详细列表</a>
			          <input class="btn-delete" type="button" value="删除"></input>
			        </td>
			      </tr>
			    </#list>
			  </#if>
			  </table>
			  <input type="hidden" name="actionType" value="3"></input>
			  </form>
		  </div>
	    </div>
	  </div>
	</div>
	<script type="text/javascript">
	  $(document).on("click",".btn-delete",function(){
	       var bothoers=$(this).parent().parent();
	       var id=bothoers.find(".input-id").val();
	       $.ajax({
	           type: "GET",
               url: "deleteinfo.html",
               data: {id:id},
               dataType: "json",
               success: function(data){
                 if(data.status){
                     location.reload(true);
                     alert("删除成功！");
                 } else {
                     location.reload(true);
                     alert("删除失败！");
                 }
               }
	       });
	    });
	</script>
	<#include "/common/footer.html" />
</body>
</html>