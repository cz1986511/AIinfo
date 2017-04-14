<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>监控平台</title>
    <#include "/common/header.html"/>
</head>
<body>
    <div class="container">
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
                                    <input class="form-control" placeholder="请输入手机号" id="tel" name="tel" type="text" autofocus><font color="red">${errorMsg}</font>
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
      });
    </script>
</body>
</html>
