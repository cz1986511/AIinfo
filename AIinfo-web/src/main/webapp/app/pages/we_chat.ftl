<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <#include "/common/header.html"/>
    <meta name="description" content="">
    <meta name="author" content="">
    <title>AIinfo - 吐槽池</title>
	<script>
	  var ws = new WebSocket("ws://chenzhuo.pub:8080/AIinfo/websocket");
	  ws.onopen = function(){
	  };
	  ws.onmessage = function(message){
		document.getElementById("chatlog").textContent += message.data + "\n";
	  };
	  function postToServer(){
		ws.send(document.getElementById("msg").value);
		document.getElementById("msg").value = "";
	  }
	  function closeConnect(){
		ws.close();
	  }
	</script>
</head>
<body>
    <div id="wrapper">
		<#if userType == 1>
		  <#include "navigation1.html"/>
		<#else>
		  <#include "navigation.html"/>
        </#if>
            <div class="container-fluid">
                    <div class="col-lg-12">
						<h3 class="page-header">欢迎来到聊天室</h3>
						<div>
						  <textarea id="chatlog" style="width:300px;height:200px;" readonly></textarea><br/>
						</div>
						输入:<input id="msg" type="textarea" />
						<button class="btn btn-success" type="submit" id="sendButton" onClick="postToServer()">发送</button>
                    </div>
                    <!-- /.col-lg-12 -->
            </div>
            <!-- /.container-fluid -->
    </div>
    <!-- /#wrapper -->
    <#include "/common/footer.html" />
</body>
</html>