package com.danlu.web.base;

public class CommonBase
{
    // 丹露经销商系统管理员ID
    public static String ACLID = "e1cea524042d4d168158378a50e2d132";
    // 微信密码
    public static String SECRET = "7d2ff0588993e3e14e9e87dea0580434";
    // 微信appId
    public static String APPID = "wxe6544f8f04a080bb";
    // 新订单消息模板ID
    public static String ORDERTEMPLATID = "EpFBzPzTAekeG3E72vCsYerKOA0j-GAyKILvQKlJ2DY";
    // 销售统计消息模板ID
    public static String SALLTEMPLATID = "HCXZE9J3Nv8kb0gp_BxLiaHzbYPRPEM0G_ISmNLxsY0";
    // 模板消息参数值颜色
    public static String COLOR = "#173177";

    // 获取用户微信openID地址
    public static String OAUTH2URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
    // 获取微信access_token地址
    public static String ACCESSTOKENURL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=SECRET";
    // 获取用户微信信息地址
    public static String USERINFOURL = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
    // 发送微信模板消息地址
    public static String SENDMSGURL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";

    // 丹露用户登录验证地址
    public static String USERURL = "http://uc.danlu.com/uc/V1/users/";
    // 丹露用户获取企业信息地址
    public static String COMPANYURL = "http://uc.danlu.com/uc/V1/dlcompany/get_companyinfo";

}
