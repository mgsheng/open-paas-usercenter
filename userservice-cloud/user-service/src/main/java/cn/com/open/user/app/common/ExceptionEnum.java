package cn.com.open.user.app.common;

/**
 * 描述：
 * author：mason(ma)
 * 日期：16/3/11.
 */
public enum ExceptionEnum {
    SysException("1000000","系统异常"),
    CommonParmeterError("1000001", "公共参数缺失错误"),
    E1000001("1000001","App验证失败"),
    E1000002("1000001","签名验证失败"),
    E1000101("1000101","file is not exist."),
    E1000102("1000102","file is exist."),
    ParameterError("100103","请求参数错误"),
    DatabaseError("100104","数据库操作错误"),
    FileTypeError("100105", "文件类型异常"),
    FileExceedError("100106", "文件过大异常"),
    URLRedirectError("100107", "Url跳转异常"),
    UpdateError("100201", "更新错误"),
    InsertError("100202", "添加错误"),
    DeleteError("100203", "删除错误"),
    DataFormatError("100204", "数据格式不正确"),
    Unkow("999999","未知错误")
    ;
    String code;
    String message;

    ExceptionEnum() {
    }

    ExceptionEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name();
    }

    public String getDisplay() {
        return message;
    }

    public String getMemo() {
        return message;
    }

    public Enum<?> getDefault() {
        return null;
    }
}
