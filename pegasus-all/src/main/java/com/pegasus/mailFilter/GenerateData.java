package com.pegasus.mailFilter;

import lombok.extern.slf4j.Slf4j;
import org.ansj.domain.Term;

import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * Created by enHui.Chen on 2019/11/29.
 */
@Slf4j
public class GenerateData {

    /**
     * @Author: enHui.Chen
     * @Description: 生成spam/ham训练集
     * @Data 2019/11/29
     */
    public static void generateSpamAndHamMail(String path) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(new File(path)));
        int hamCount = 0;
        int spamCount = 0;
        String line;
        while ((line = br.readLine()) != null) {
            String[] lines = line.split(" ");
            String mailType = lines[0];

            String destPath = MailConstant.BASE_PATH + mailType + "\\" + (MailConstant.SPAM.equals(mailType) ? spamCount++ : hamCount++) + ".txt";
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(destPath)));

            String mailPath = lines[1].replace("/", "\\").replace("..", MailConstant.BASE_PATH + "trec06c");
            BufferedReader tempBr = new BufferedReader(new InputStreamReader(new FileInputStream(mailPath + ".txt"), "GBK"));
            String tempLine;

            log.info("正在生成训练集的路径:{}", destPath);
            boolean writeFlag = false;
            while ((tempLine = tempBr.readLine()) != null) {
                // 过滤邮件固定传输头信息提高准确率
                if (tempLine.equals("")) {
                    writeFlag = true;
                }
                if (writeFlag) {
                    bw.write(tempLine);
                    bw.newLine();
                    bw.flush();
                }
            }
            writeFlag = false;
            bw.close();
            tempBr.close();
        }
        br.close();
    }


    /**
     * @Author: enHui.Chen
     * @Description: 生成ham/spam分词
     * @Data 2019/11/29
     */
    public static void generateSegment(String type, List<String> source) throws Exception {
        log.info("开始生成分词数据...");
        BufferedWriter bw = new BufferedWriter(new FileWriter(new File(MailConstant.SEGMENT_PATH + type + ".txt")));
        int count = 1;
        for (String s : source) {
            List<Term> terms = NativeBayesMailFilter.segment(s);
            for (Term term : terms) {
                String name = term.getName();
                if (name.length() < 2) {
                    continue;
                }
                bw.write(name);
                bw.newLine();
                bw.flush();
            }
            log.info("已生成第{}封邮件分词", count++);
        }
        bw.close();
        log.info("分词数据生成完毕...");
    }

    /**
     * @Author: enHui.Chen
     * @Description: 生成当出现某个分词时该邮件是垃圾邮件的概率
     * @Data 2019/11/29
     */
    public static void generateWordSpamMailFrequency(Map<String, Double> wordMailFrequency) throws Exception {
        log.info("正在生成当出现某个分词时该邮件是垃圾邮件的概率...");
        BufferedWriter bw = new BufferedWriter(new FileWriter(new File(MailConstant.RATE_PATH)));
        for (Map.Entry<String, Double> entry : wordMailFrequency.entrySet()) {
            bw.write(entry.getKey() + ":" + entry.getValue());
            bw.newLine();
            bw.flush();
        }
        bw.close();
        log.info("生成当出现某个分词时该邮件是垃圾邮件的概率完毕...");
    }

    /**
     * @Author: enHui.Chen
     * @Description: 生成txt文件
     * @Data 2019/11/29
     */
    public static void mvFile() throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(new File(MailConstant.BASE_PATH + "trec06c\\full\\index")));
        String line;
        while ((line = br.readLine()) != null) {
            String[] lines = line.split(" ");
            String mailPath = lines[1].replace("/", "\\").replace("..", MailConstant.BASE_PATH + "trec06c");
            new File(mailPath).renameTo(new File(mailPath + ".txt"));// java移动文件
        }
        br.close();
    }

}
