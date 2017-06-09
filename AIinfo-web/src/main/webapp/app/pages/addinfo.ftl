<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <#include "/common/header.html"/>
    <meta name="description" content="">
    <meta name="author" content="">
    <title>AIinfo - 增加机器信息</title>
</head>
<body>
    <div id="wrapper">
        <#include "navigation1.html"/>
        <!-- Page Content -->
        <div id="page-wrapper">
            <div class="container-fluid">
                <div class="row">
                    <div class="col-lg-12">
                        <h1 class="page-header">欢迎</h1>
                    </div>
                    <!-- /.col-lg-12 -->
                    <form id="addinfo" action="addinfoaction.html" method="post">
                      <table>
                        <tr>
                          <th>Name:</th>
                          <th>IP:</th>
                          <th>Port:</th>
                          <th>Desc:</th>
                          <th>操作</th>
                        </tr>
                        <tr>
                          <td><input class="input-name" id="name" type="text"></input></td>
                          <td><input class="input-ip" id="ip" type="text"></input></td>
                          <td><input class="input-port" id="port" type="text"></input></td>
                          <td><input class="input-desc" id="desc" type="text"></input></td>
                          <td><input class="btn-addinfo" type="button" value="添加"></input></td>
                        </tr>
                      </table>
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
      $(document).on("click",".btn-addinfo",function(){
	       var bothoers=$(this).parent().parent();
	       var name=bothoers.find(".input-name").val();
	       var ip=bothoers.find(".input-ip").val();
	       var port=bothoers.find(".input-port").val();
	       var desc=bothoers.find(".input-desc").val();
	       $.ajax({
	           type: "GET",
               url: "addinfoaction.html",
               data: {name:name, ip:ip, port:port, desc:desc},
               dataType: "json",
               success: function(data){
                 if(data.status == true){
                     location.reload(true);
                     alert("添加成功！");
                 } else {
                     location.reload(true);
                     alert("添加失败！");
                 }
               }
	       });
	    });
    </script>
    <#include "/common/footer.html" />
</body>
</html>