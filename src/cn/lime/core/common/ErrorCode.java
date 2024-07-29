package cn.lime.core.common;

/**
 * 自定义错误码
 */
public enum ErrorCode {

    SUCCESS(0, "ok"),

    DB_ERROR(50000,"数据库DDL异常"),
    QUERY_ERROR(50010, "查询数据失败"),
    ACCOUNT_EXIST(50011, "账号已经存在"),
    NICK_EXIST(50012, "昵称已经存在"),
    PHONE_EXIST(50013, "手机号已经存在"),

    PWD_ERROR(50014,"用户密码不正确"),

    PHONE_ERROR(50015,"用户手机号不一致"),
    DOCUMENT_CONTENT_NOT_CONSISTENT(50016,"纯文稿内容与数据库不一致"),

    INSERT_ERROR(50020, "插入数据失败"),
    DELETE_ERROR(50030, "删除数据失败"),
    UPDATE_ERROR(50040, "更新数据失败"),
    SOMEONE_EDITING(50041,"更新文章编辑状态失败"),

    UPDATE_AI_TIME_FAIL(50042,"更新用户AI次数失败"),
    SELECT_ERROR(50050, "查询数据失败"),
    CANNOT_FIND_BY_ID(50051,"无法根据ID查询到相应数据"),

    DB_DATA_ERROR(50500,"数据库数据异常,请联系管理员核对"),


    PARAMS_ERROR(40000, "请求参数错误"),

    INVALID_ACCESS_TOKEN(40098,"无效的ACCESS_TOKEN"),
    INVALID_REFRESH_TOKEN(40099,"无效的REFRESH_TOKEN"),
    NOT_LOGIN_ERROR(40100, "未登录"),

    LOGIN_INFO_ERROR(40101,"用户登录账号密码错误"),

    LOGIN_ROLE_ERROR(40102,"用户角色和实际情况不匹配"),

    LOGIN_METHOD_ERROR(40103,"不支持的登录方式"),
    OP_TYPE_METHOD_ERROR(40103,"不支持的操作方式"),
    LOGIN_NO_USER(40104,"不存在的账户"),

    BUSINESS_ERROR(40200,"业务逻辑错误"),

    DOCUMENT_NUMBER_LIMIT(40201,"已达可存储文档上限"),
    AUTH_FAIL(40202,"用户权限不足"),
    USER_EDITING(40203,"用户正在编辑,请稍后再进行操作"),

    DOCUMENT_FILE_UNSUPPORTED(40204,"不支持的文稿文件"),

    DOCUMENT_FILE_READ_FAIL(40205,"读取文稿异常"),

    DISCOUNT_CODE_EXPIRE(40206,"优惠折扣已过期"),
    PAY_METHOD_UNSUPPORTED(40207,"不支持的支付方式"),
    QUIT_EDIT_BEFORE_AI(40208,"用户正在编辑,请退出编辑模式后再调用AI并保证期间不更改文章"),

    USER_LACK_AI_TIMES(40209,"用户AI使用次数不足"),
    AI_TASK_RUNNING(40210,"AI任务正在运行,请稍后编辑文档"),
    DOC_CONTENT_TOO_LONG(40211,"文档内容过长"),

    CONTENT_AND_MARK_NOT_CONSISTENT(40212,"文稿中标记点和批注数量不一致"),

    FIND_TARGET_SUGGESTION_ERROR(40213,"无法在问文稿内容找定位到指定批注"),
    GENERATE_INSERT_PARAGRAPH_ERROR(40214,"生成插入段落类批注异常"),
    UNSUPPORTED_METHOD(40215,"不支持的操作方式"),
    SUGGESTION_HAS_APPLIED(40216,"该建议已被处理"),

    SENTENCE_NOT_EXIST(40217,"无法定位到该句子"),

    NO_AI_KEY_AVAILABLE(40218,"无可用AI秘钥"),

    PARSE_AI_RESULT_ERROR(40219,"解析AI JSON信息失败"),
    EXISTED_UNPAID_ORDER(40220,"用户存在未付款的订单,请先处理未付款订单"),

    INSTITUTION_APP_NOT_AUTH(40221,"当前机构未获得该APP授权"),
    DOCUMENT_VERSION_OUT_OF_DATE(40222,"当前文稿不是最新版本,请刷新"),

    OUTER_ERROR(30000,"外部接口异常"),
    WX_INTERFACE_ERROR(31000,"微信接口异常"),
    WX_PHONE_INTERFACE_ERROR(31001,"调用微信手机号接口异常"),
    WX_OPENID_INTERFACE_ERROR(31002,"调用微信OpenID接口异常"),
    OPEN_AI_INTERFACE_ERROR(32000,"OpenAI接口异常"),
    ALI_OCR_INTERFACE_ERROR(33000,"调用阿里OCR接口异常"),
    QI_NIU_OSS_INTERFACE_ERROR(34000,"调用七牛OSS接口异常"),
    STRIPE_INTERFACE_ERROR(35000,"调用STRIPE接口异常"),
    ALI_OSS_INTERFACE_ERROR(36000,"调用阿里OSS接口异常"),
    QUEER_VECTOR_INTERFACE_ERROR(37000,"调用计算向量接口异常"),
    REALTIME_CHECK_ERROR(37001,"调用实时建议接口异常"),



    USER_STATUS_ERROR(40103,"用户被禁用/已注销"),

    MERCHANT_TO_BE_APPROVED(40104,"商户未审批通过"),

    MOBILE_PHONE_MESSAGE_ERROR(41000,"发送手机验证码异常"),

    MOBILE_PHONE_MESSAGE_CODE_ERROR(41001,"手机验证码不正确"),

    WX_QUERY_USER_INFO_ERROR(42001,"查询微信用户信息为空"),


    NO_ENOUGH_MONEY(44000,"用户余额不足"),

    UN_SUPPORT_PAY_METHOD(44001,"不支持的支付方式"),

    ORDER_STATUS_ERROR(44002,"订单状态异常，您无法对非待付款状态的订单进行付款"),

    ORDER_STATUS_CANNOT_REFUND(44003,"该订单状态无法进行退款"),











    LOG_ERROR(50015,"物品操作日志留痕异常，业务逻辑已完成"),


    DATA_EXIST(40010, "数据重复"),
    NO_AUTH_ERROR(40101, "无权限"),
    NOT_FOUND_ERROR(40400, "请求数据不存在"),
    FORBIDDEN_ERROR(40300, "禁止访问"),
    UUID_INVALID(40311, "uuid无效"),
    SYSTEM_ERROR(50000, "系统内部异常"),
    OPERATION_ERROR(50001, "操作失败"),

    IO_ERROR(50020,"系统IO错误");

    /**
     * 状态码
     */
    private final int code;

    /**
     * 信息
     */
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}