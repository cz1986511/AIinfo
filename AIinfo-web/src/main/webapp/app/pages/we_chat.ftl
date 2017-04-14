<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset=UTF-8>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>信息管理</title>
    <#include "/common/header.html"/>
	<script>
	  var ws = new WebSocket("ws://192.168.50.54:8080/AIinfo/websocket");
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
		<#include "navigation.html"/>
        <!-- Page Content -->
        <div id="page-wrapper">
            <div class="container-fluid">
                <div class="row">
                    <div class="col-lg-12">
						<h3 class="page-header">欢迎来到聊天室</h3>
						<textarea id="chatlog" readonly></textarea><br/>
						<input id="msg" type="textarea" />
						<button type="submit" id="sendButton" onClick="postToServer()">发送</button>
						<!-- <button type="submit" id="sendButton" onClick="closeConnect()">End</button> -->
                    </div>
                    <!-- /.col-lg-12 -->
                </div>
            </div>
            <!-- /.container-fluid -->
        </div>
        <!-- /#page-wrapper -->
    </div>
    <!-- /#wrapper -->
    <#include "/common/footer.html" />
</body>
</html>