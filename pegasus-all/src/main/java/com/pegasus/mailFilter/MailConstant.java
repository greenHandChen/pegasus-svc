package com.pegasus.mailFilter;

/**
 * Created by enHui.Chen on 2019/11/29.
 */
public class MailConstant {
    public static final String HAM = "ham";
    public static final String SPAM = "spam";
    public static final String BASE_PATH = "E:\\pegasus\\mailData\\";
    public static final String HAM_PATH = "E:\\pegasus\\mailData\\" + MailConstant.HAM;// 健康邮件
    public static final String SPAM_PATH = "E:\\pegasus\\mailData\\" + MailConstant.SPAM;// 垃圾邮件
    public static final String SEGMENT_PATH = "E:\\pegasus\\mailData\\segment\\";// 分词数据路径
    public static final String RATE_PATH = "E:\\pegasus\\mailData\\rate\\rate.txt";// 生成当出现某个分词时该邮件是垃圾邮件的概率的路径
    public static final String CUSTOM_PATH = "E:\\pegasus\\mailData\\custom";//
    public static final boolean IS_INIT_MAIL_DATA = false;// 初始化邮件
    public static final boolean IS_INIT_SEGMENT = false;// 初始化分词后的数据
    public static final boolean IS_INIT_SEGMENT_FREQUENCY = false;// 初始化当出现某个分词时该邮件是垃圾邮件的概率

    public static final int COUNT_MAIL = 15000;// 测试邮件/健康邮件/垃圾邮件的个数

}
