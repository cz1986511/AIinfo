package com.idmaker.oa.server.model.vo.common;

import lombok.Getter;

/**
 * 标准异常代码
 * code 是错误码，message是错误原因默认值
 *  code 都是APPID + 上一个值构成一个6位数，
 *  除了成功和未知错误以外都是 百位数；
 *  100-199是定义  数据错误的各种异常；
 *  200-299是定义  认证和权限验证异常；
 *  如有新的类型 300+  400+  以此类推；
 * @author chenzhuo
 * @date   2021-02-06 16:40
 */
public enum ResultCode {
    /**
     * 执行结果的返回值
     */
    SUCCESS                     (ResultCode.APP_ID +       0,          "成功"),
    FAIL_UNKNOWN                (ResultCode.APP_ID +       3000,          "未知错误"),
    FAIL_ILLEGAL_ARGUMENT       (ResultCode.APP_ID +       3101,        "参数强验证失败"),
    FAIL_DEPENDENCY_CHECK       (ResultCode.APP_ID +       3102,        "依赖信息检查失败"),
    FAIL_TARGET_NOT_EXIST       (ResultCode.APP_ID +       3103,        "目标对象不存在或者已经被删除"),
    FAIL_ILLEGAL_STATE          (ResultCode.APP_ID +       3104,        "目标状态错误"),
    FAIL_ILLEGAL_RESULT         (ResultCode.APP_ID +       3105,        "错误的返回结果"),
    FAIL_DUPLICATE_DATA         (ResultCode.APP_ID +       3106,        "数据已存在"),
    FAIL_DATA_WRONG             (ResultCode.APP_ID +       3107,        "数据错误"),
    FAIL_HTTP_REQ               (ResultCode.APP_ID +       3108,        "HTTP请求错误"),
    FAIL_CODE_CHECK             (ResultCode.APP_ID +       3109,        "参数强验证失败"),
    FAIL_PASSREG_WRONG          (ResultCode.APP_ID +       3110,        "密码正则错误"),
    FAIL_PASS_WRONG             (ResultCode.APP_ID +       3112,        "密码错误"),
    FAIL_PASS_SAME_WRONG        (ResultCode.APP_ID +       3113,        "密码一致错误"),
    FAIL_CONFIRM                (ResultCode.APP_ID +       3114,        "需二次确认"),
    FAIL_COMMIT_LOCK            (ResultCode.APP_ID +       3115,        "您点得太快了"),
    FAIL_AUTH_FAIL              (ResultCode.APP_ID +       3201,        "身份校验失败"),
    FAIL_LACK_OF_PERMISSION     (ResultCode.APP_ID +       3202,        "缺少操作所需的权限"),
    FAIL_ADDRESS_CREATE         (ResultCode.APP_ID +       3301,        "通讯录创建失败"),
    FAIL_APPLY_CREATE           (ResultCode.APP_ID +       3302,        "创建申请失败"),
    FAIL_CONTRACT_CREATE        (ResultCode.APP_ID +       3303,        "创建合同会签失败"),
    FAIL_FILE_CIRCULATE_CREATE  (ResultCode.APP_ID +       3304,        "创建文件传阅失败"),
    FAIL_SIGNATURE_CREATE       (ResultCode.APP_ID +       3305,        "上传电子签名失败"),
    FAIL_ARCHIVE_CREATE         (ResultCode.APP_ID +       3306,        "创建员工档案失败"),
    FAIL_ARCHIVE_UPDATE         (ResultCode.APP_ID +       3307,        "修改员工档案失败"),
    FAIL_APPROVE_ADOPT          (ResultCode.APP_ID +       3310,        "审批同意失败"),
    FAIL_APPROVE_REFUSE         (ResultCode.APP_ID +       3311,        "审批拒绝失败"),
    FAIL_FILE_CIRCULATE_SIGN    (ResultCode.APP_ID +       3312,        "文件传阅签注失败"),
    FAIL_CONTRACT_SIGN          (ResultCode.APP_ID +       3313,        "合同会签已撤回或已会签过"),
    FAIL_CONTRACT_REVOKE        (ResultCode.APP_ID +       3314,        "合同会签撤回失败"),
    FAIL_SQL_DATA_ERROR         (ResultCode.APP_ID +       3401,        "SQL数据错误")
    ;

    private static final int       APP_ID =2           *       1000;

    @Getter
    private int                         code;
    @Getter
    private String                      desc;

    ResultCode(int code, String desc){
        this.code=code;
        this.desc=desc;
    }

}
