<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <#include "/common/header.html"/>
    <title>AIinfo - 书单列表页</title>
</head>
<body>
    <div id="wrapper">
		<#if userType == 1>
			<#include "navigation1.html"/>
		<#else>
		    <#include "navigation.html"/>
		</#if>
        <div class="container-fluid">
			<h2><span class="glyphicon glyphicon-heart-empty"></span> 第${bookListDate}期书单 <span class="glyphicon glyphicon-heart-empty"></span></h2>
			<h3><span class="glyphicon glyphicon-folder-open"></span> 想读</h3>
			<#if bookLists??>
			    <#list bookLists as info>
				    <#if userName == info.userName>
					  <div class="alert alert-success" role="alert">
						me<strong>想读:</strong>
						<div class="input-group">
						  <input class="form-control" type="text" id="me-unread" value="${info.unreadList}"/>
						  <div class="input-group-btn">
						    <input class="btn btn-default" type="submit" id="btn-unread" name="btn-unread" value="保存">
						  </div>
						</div>
					  </div>
					<#else>
					  <div class="alert alert-info" role="alert">
					    <span class="glyphicon glyphicon-book"></span> ${info.userName}<strong>想读:</strong>${info.unreadList}
					  </div>
					</#if>
			    </#list>
			</#if>
			<h3><span class="glyphicon glyphicon-level-up"></span> 已读</h3>
			<#if bookLists??>
			    <#list bookLists as info>
				    <#if userName == info.userName>
					  <div class="alert alert-success" role="alert">
						me<strong>已读:</strong>
						<div class="input-group">
						  <input class="form-control" type="text" id="me-read" value="${info.readList}"/>
						  <div class="input-group-btn">
						    <input class="btn btn-default" type="submit" id="btn-read" name="btn-read" value="保存">
						  </div>
						</div>
					  </div>
					<#else>
					  <div class="alert alert-info" role="alert">
					    <span class="glyphicon glyphicon-book"></span> ${info.userName}<strong>已读:</strong>${info.readList}
					  </div>
					</#if>
			    </#list>
			</#if>
	    </div>
	</div>
	<script type="text/javascript">
	  $('#btn-unread').click(function(){
	       var unread = document.getElementById("me-unread").value;
	       $.ajax({
	           type: "POST",
               url: "modify_book.action",
               data: {unread:unread},
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
		
		$('#btn-read').click(function(){
	       var read = document.getElementById("me-read").value;
	       $.ajax({
	           type: "POST",
               url: "modify_book.action",
               data: {read:read},
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