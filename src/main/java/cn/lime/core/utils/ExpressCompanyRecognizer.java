package cn.lime.core.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @ClassName: ExpressCompanyRecognizer
 * @Description: TODO 描述类的功能
 * @Author: riang
 * @Date: 2026/2/26 22:49
 */
public class ExpressCompanyRecognizer {
    // 快递公司编码规则映射表：键=快递公司名称，值=匹配正则表达式
    private static final Map<String, String> EXPRESS_RULES = new HashMap<>();

    // 静态初始化规则表（覆盖主流快递公司）
    static {
        // 顺丰速运：12位数字 或 13位数字 或 以SF开头+10-15位数字/字母
        EXPRESS_RULES.put("顺丰速运", "^(SF|sf)?\\d{10,15}$|^\\d{12,13}$");
        // 中通快递：12位数字 或 7-8位数字开头+字母
        EXPRESS_RULES.put("中通快递", "^\\d{12}$|^[0-9]{7,8}[A-Za-z]$");
        // 圆通速递：10位/12位/18位数字，或以YT开头+10位数字
        EXPRESS_RULES.put("圆通速递", "^(YT|yt)?\\d{10}$|^\\d{10,12}$|^\\d{18}$");
        // 申通快递：12位数字
        EXPRESS_RULES.put("申通快递", "^\\d{12}$");
        // 韵达快递：13位数字
        EXPRESS_RULES.put("韵达速递", "^\\d{13}$");
        // 天天快递：14位数字 或 以TT开头+10位数字
        EXPRESS_RULES.put("天天快递", "^(TT|tt)?\\d{10}$|^\\d{14}$");
        // EMS：13位数字（开头为10/11/12/13/14/15/16/17/18）或 以EA/EB/ED等开头+9位数字+CN结尾
        EXPRESS_RULES.put("EMS", "^[1-9]\\d{12}$|^[Ee][AaBbDdFg][0-9]{9}[Cc][Nn]$");
        // 京东物流：10位/12位数字
        EXPRESS_RULES.put("京东快递", "^\\d{10}$|^\\d{12}$");
        // 极兔速递：13位数字
        EXPRESS_RULES.put("极兔速递", "^\\d{13}$");
    }

    /**
     * 根据快递单号识别快递公司（返回匹配的所有可能）
     * @param expressNo 快递单号（需去除空格、特殊字符）
     * @return 匹配的快递公司列表，无匹配则返回空列表
     */
    public static List<String> recognizeExpressCompany(String expressNo) {
        List<String> result = new ArrayList<>();
        // 空值校验
        if (expressNo == null || expressNo.trim().isEmpty()) {
            return result;
        }
        // 清理单号：去除空格、制表符等空白字符
        String cleanNo = expressNo.trim().replaceAll("\\s+", "");

        // 遍历规则表，匹配单号
        for (Map.Entry<String, String> entry : EXPRESS_RULES.entrySet()) {
            String company = entry.getKey();
            String regex = entry.getValue();
            // 正则匹配
            if (Pattern.matches(regex, cleanNo)) {
                result.add(company);
            }
        }
        return result;
    }

    /**
     * 获取最可能的快递公司（返回第一个匹配项）
     * @param expressNo 快递单号
     * @return 最可能的快递公司名称，无匹配返回null
     */
    public static String getMostLikelyCompany(String expressNo) {
        List<String> companies = recognizeExpressCompany(expressNo);
        return companies.isEmpty() ? null : companies.get(0);
    }
}