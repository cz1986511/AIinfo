<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <#include "/common/header.html"/>
    <title>AIinfo - 图书池</title>
</head>
<body>
    <div id="wrapper">
	    <#if userType == 1>
			<#include "navigation1.html"/>
		<#else>
		    <#include "navigation.html"/>
		</#if>
        <div class="container-fluid" id="addBook" style="display:none">
			<h2><span class="glyphicon glyphicon-heart-empty"></span> 新增图书 <span class="glyphicon glyphicon-heart-empty"></span></h2>
			<div class="input-group">
				<span class="input-group-addon" id="sizing-addon1">书名</span>
				<input type="text" id="bookName" class="form-control" placeholder="BookName" aria-describedby="sizing-addon2">
			</div>
			<div class="input-group">
				<span class="input-group-addon" id="sizing-addon2">作者</span>
				<input type="text" id="author" class="form-control" placeholder="Author" aria-describedby="sizing-addon2">
			</div>
			<div class="input-group">
				<span class="input-group-addon" id="sizing-addon3">分类</span>
				<input type="text" id="type" class="form-control" placeholder="Type" aria-describedby="sizing-addon2">
			</div>
			<div class="input-group">
				<span class="input-group-addon" id="sizing-addon4">来源</span>
				<input type="text" id="source" class="form-control" placeholder="Source" aria-describedby="sizing-addon2">
			</div>
			<input class="btn btn-success" type="button" id="btn-save" value="保存" />
			<input class="btn btn-default" type="button" id="btn-cancel" value="取消" />
	    </div>
		<div class="container-fluid" id="booksList">
		    <div class="alert alert-warning" role="alert">
				<span class="glyphicon glyphicon-heart"></span> ${wisdom}
			</div>
			<h2><span class="glyphicon glyphicon-heart-empty"></span> 图书池 <span class="glyphicon glyphicon-heart-empty"></span></h2>
			<#if userType == 1>
				<input class="btn btn-info" type="button" id="btn-addBook" value="新增图书" />
			</#if>
			<#if books??>
			    <#list books as book>
					  <div class="alert alert-info" role="alert">
					    <span class="glyphicon glyphicon-book"></span> ${book.bookName}--<#if book.bookStatus == "02"><font color="red">锁定</font><#elseif book.bookStatus == "03"><font color="red">已借出</font><#else><font color="green">未借出</font><button type="button" class="btn btn-link" id="btn-bookBorrow" value="${book.bookId}"><strong>借阅</strong></button></#if>
					  </div>
			    </#list>
			</#if>
	    </div>
	</div>
	<script type="text/javascript">
	    $("#btn-addBook").click(function(){
          $("#addBook").css("display","")
        })
		$("#btn-cancel").click(function(){
          $("#addBook").css("display","none")
        })
		$("#btn-save").click(function(){
		  var bookName = $('#bookName').val();
		  var author = $('#author').val();
		  var type = $('#type').val();
		  var source = $('#source').val();
		  if (!bookName) {
		    alert("书名不能为空");
		    return false;
		  }
		  if (bookName.length > 60) {
		    alert("书名太长啦");
		    return false;
		  }
		  $.ajax({
	           type: "POST",
               url: "addbook.action",
               data: {bookName:bookName, author:author, type:type, source:source},
               dataType: "json",
               success: function(data){
                 if(data.status == true){
                     location.reload(true);
                     alert("成功:" + data.msg);
                 } else {
                     location.reload(true);
                     alert("失败:" + data.msg);
                 }
               }
	       });
        })
		
		$("button").click(function(){
		  var text = $(this).text();
		  if ("借阅" == text) {
		      var id = $(this).val();
		      $.ajax({
	           type: "POST",
               url: "bookborrow.action",
               data: {id:id},
               dataType: "json",
               success: function(data){
                 if(data.status == true){
                     location.reload(true);
                     alert("成功:" + data.msg);
                 } else {
                     location.reload(true);
                     alert("失败:" + data.msg);
                 }
               }
	          });
		  }
        })
	</script>
	<#include "/common/footer.html" />
</body>
</html>