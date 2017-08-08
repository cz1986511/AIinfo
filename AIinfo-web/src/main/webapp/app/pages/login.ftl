<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <#include "/common/header.html"/>
    <meta name="description" content="">
    <meta name="author" content="">
    <title>AIinfo - 登陆</title>
</head>
<body>
    <div class="container">
	    <div class="alert alert-info" role="alert">
		    <span class="glyphicon glyphicon-heart"></span> ${wisdom}
		</div>
        <div class="row">
            <div class="col-md-4 col-md-offset-4">
                <div class="login-panel panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title">请登录</h3>
                    </div>
                    <div class="panel-body">
                        <form name="form" action="load.html" method="post">
                            <fieldset>
                                <div class="form-group">
                                    <input class="form-control" placeholder="请输入手机号" id="tel" name="tel" type="text" autofocus><font color="red">${msg}</font>
                                </div>
                                <div class="form-group">
                                    <input class="form-control" placeholder="请输入密码" id="password" name="password" type="password" >
                                </div>
                                <div class="checkbox">
                                    <label>
                                        <input name="remember" type="checkbox" value="Remember Me">记住我
                                    </label>
                                </div>
                                <input class="btn btn-lg btn-success btn-block" type="submit" id="btnLoad" name="btnLoad" value="登陆"></input>
                            </fieldset>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <#include "/common/footer.html" />
    <script type="text/javascript">
      $('#btnLoad').click(function(){
        if (!$('#tel').val()) {
		     alert("手机号不能为空");
			 return false;
		}
		if (!$('#password').val()) {
		     alert("密码不能为空");
			 return false;
		}
		var password = md5($('#password').val());
		$('#password').val(password);
      });
    </script>
</body>
</html>
