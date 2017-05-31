<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>书单列表页</title>
    <#include "/common/header.html"/>
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
			  <h2>第${bookListDate}期书单</h2>
			</div>
			  <table class="table">
			    <tr>
			      <th>姓名</th>
			      <th>想读</th>
			      <th>已读</th>
			      <th>更新时间</th>
				  <th>操作</th>
			    </tr>
			    <#if bookLists??>
			    <#list bookLists as info>
			      <tr>
				    <#if userName == info.userName>
					  <td>${info.userName}</td>
			          <td><input class="td-unread" type="text" value="${info.unreadList}" /></td>
			          <td><input class="td-read" type="text" value="${info.readList}" /></td>
			          <td>${(info.gmtModify?string("yyyy-MM-dd HH:mm:ss"))!}</td>
			          <td><input class="btn btn-lg btn-success btn-block" type="submit" id="btnModify" name="btnModify" value="保存"></input></td>
					<#else>
					  <td>${info.userName}</td>
			          <td>${info.unreadList}</td>
			          <td>${info.readList}</td>
			          <td>${(info.gmtModify?string("yyyy-MM-dd HH:mm:ss"))!}</td>
			          <td></td>
					</#if>
			      </tr>
			    </#list>
			  </#if>
			  </table>
		  </div>
	    </div>
	  </div>
	</div>
	<script type="text/javascript">
	  $('#btnModify').click(function(){
	       var bothoers = $(this).parent().parent();
	       var unread = bothoers.find(".td-unread").val();
		   var read = bothoers.find(".td-read").val();
	       $.ajax({
	           type: "POST",
               url: "modify_book.action",
               data: {unread:unread,read:read},
               dataType: "json",
               success: function(data){
                 if(data.status){
                     location.reload(true);
                     alert("修改成功！");
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