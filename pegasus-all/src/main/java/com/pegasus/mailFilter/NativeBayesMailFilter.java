package com.pegasus.mailFilter;

import lombok.extern.slf4j.Slf4j;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.recognition.impl.StopRecognition;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: enHui.Chen
 * @Description: 分词：
 * 0.读取邮件数据/生成spam/ham训练集
 * 1.对spam/ham邮件进行分词,并生成相应数据
 * 2.统计各个词在spam/ham邮件出现的频次
 * 3.计算词在spam邮件中出现的概率
 * 4.统计联合概率
 * @Data 2019/11/28
 */
@Slf4j
public class NativeBayesMailFilter {

    public static void main(String[] args) throws Exception {
        double filterRate = 0D;// 垃圾邮件拦截率
        double mailCount = 0D;// 邮件总数
        double spamMailCount = 0D;// 垃圾邮件数
        double hamMailCount = 0D;// 和谐邮件数

        long start = System.currentTimeMillis();
        long current = System.currentTimeMillis();

        // 0.读取邮件数据/生成spam/ham训练集
        if (MailConstant.IS_INIT_MAIL_DATA) {
            GenerateData.generateSpamAndHamMail(MailConstant.BASE_PATH + "trec06c\\full\\index");
            log.info("生成spam/ham训练集:{}s", (-current + (current = System.currentTimeMillis())) / 1000d);
        }

        // 1.对spam/ham邮件进行分词,并生成相应数据
        if (MailConstant.IS_INIT_SEGMENT) {
            GenerateData.generateSegment(MailConstant.HAM, NativeBayesMailFilter.readMail(MailConstant.HAM_PATH, "UTF-8"));
            GenerateData.generateSegment(MailConstant.SPAM, NativeBayesMailFilter.readMail(MailConstant.SPAM_PATH, "UTF-8"));
            log.info("生成分词所需时间:{}s", (-current + (current = System.currentTimeMillis())) / 1000d);
        }

        if (MailConstant.IS_INIT_SEGMENT && MailConstant.IS_INIT_MAIL_DATA) {
            return;
        }

        Map<String, Double> wordSpamMailRate;
        if (MailConstant.IS_INIT_SEGMENT_FREQUENCY) {
            // 2.对垃圾/健康邮件进行分词,统计各个词在垃圾/健康邮件出现的频次
            Map<String, Double> spamMailRate = countWordMailFrequency(false, MailConstant.SPAM);// P(w|spam)
            Map<String, Double> hamMailRate = countWordMailFrequency(false, MailConstant.HAM);// P(w|ham)
            log.info("统计各个词在垃圾/健康邮件出现的频次:{}s", (-current + (current = System.currentTimeMillis())) / 1000d);

            // 3.计算当出现某个分词时该邮件是垃圾邮件的概率
            wordSpamMailRate = countWordSpamMailFrequency(spamMailRate, hamMailRate);
            log.info("计算当出现某个分词时该邮件是垃圾邮件的概率:{}s", (-current + (current = System.currentTimeMillis())) / 1000d);

            // 生成当出现某个分词时该邮件是垃圾邮件的概率
            GenerateData.generateWordSpamMailFrequency(wordSpamMailRate);
        } else {
            wordSpamMailRate = readWordSpamMailFrequency();
        }

        // 4.通过联合概率计算邮件是垃圾邮件的概率
        List<String> testMails = readMail(MailConstant.SPAM_PATH,"UTF-8");// 测试数据
        log.info("读取测试数据:{}s", (-current + (current = System.currentTimeMillis())) / 1000d);
        for (String testMail : testMails) {
            double spamRate = isSpamMail(testMail, wordSpamMailRate);
            log.info("垃圾邮件概率:{}", spamRate);
            if (spamRate > 0.5) {
                spamMailCount++;
            } else {
                hamMailCount++;
            }
        }

        mailCount = (double) testMails.size();
        filterRate = spamMailCount / mailCount;

        log.info("垃圾邮件拦截率:{}", filterRate);
        log.info("判断邮件是spam/ham邮件的总时间:{}s", (System.currentTimeMillis() - start) / 1000d);
    }

    /**
     * @Author: enHui.Chen
     * @Description: 读取邮件数据
     * @Data 2019/11/29
     */
    public static List<String> readMail(String path, String charsetName) throws Exception {
        int count = 0;
        File file = new File(path);
        File[] files = file.isDirectory() ? file.listFiles() : new File[]{file};
        if (files == null) {
            return new ArrayList<>();
        }

        List<String> mails = new ArrayList<>();
        StringBuilder data;
        BufferedReader br;

        for (File mail : files) {
            log.info("当前路径{}:完成读取次数:{}", path, count);
            if (count == MailConstant.COUNT_MAIL) {
                break;
            }
            data = new StringBuilder();
            br = new BufferedReader(new InputStreamReader(new FileInputStream(mail), charsetName));
            String line;
            while ((line = br.readLine()) != null) {
                data.append(line);
            }
            mails.add(data.toString());
            count++;
            br.close();
        }
        return mails;
    }

    /**
     * @Author: enHui.Chen
     * @Description: 读取邮件数据并且合并
     * @Data 2019/11/29
     */
    public static String readMailMerge(String path) throws Exception {
        int count = 0;
        File file = new File(path);
        File[] files = file.isDirectory() ? file.listFiles() : new File[]{file};
        if (files == null) {
            return "";
        }

        StringBuilder data = new StringBuilder();
        BufferedReader br;
        for (File mail : files) {
            if (count == MailConstant.COUNT_MAIL) {
                break;
            }
            br = new BufferedReader(new FileReader(mail));
            String line;
            while ((line = br.readLine()) != null) {
                data.append(line);
            }
            count++;
            br.close();
        }
        return data.toString();
    }

    /**
     * @Author: enHui.Chen
     * @Description: 读取当出现某个分词时该邮件是垃圾邮件的概率
     * @Data 2019/11/29
     */
    public static Map<String, Double> readWordSpamMailFrequency() throws Exception {
        Map<String, Double> wordSpamMailFrequency = new HashMap<>();
        BufferedReader br = new BufferedReader(new FileReader(new File(MailConstant.RATE_PATH)));
        String line;
        while ((line = br.readLine()) != null) {
            String[] lines = line.split(":");
            wordSpamMailFrequency.put(lines[0], Double.valueOf(lines[lines.length - 1]));
        }
        return wordSpamMailFrequency;
    }

    /**
     * @Author: enHui.Chen
     * @Description: 分词
     * @Data 2019/11/29
     */
    public static List<Term> segment(String source) {
        // TODO 临时设置停用词
        StopRecognition filter = new StopRecognition();
        filter.insertStopWords(",", ".", "", " ", "。", "!"); //过滤单词
        Result result = ToAnalysis.parse(source).recognition(filter);
        return result.getTerms();
    }


    /**
     * @Author: enHui.Chen
     * @Description: 对垃圾/健康邮件进行分词,统计各个词在垃圾/健康邮件出现的频次
     * @Data 2019/11/29
     */
    public static Map<String, Double> countWordMailFrequency(boolean isNeedSegment, String type, String... source) throws Exception {
        List<String> words;
        if (isNeedSegment) {
            List<Term> terms = segment(source[0]);
            words = terms.stream().map(Term::getName).collect(Collectors.toList());
        } else {
            BufferedReader br = new BufferedReader(new FileReader(new File(MailConstant.SEGMENT_PATH + type + ".txt")));
            String line;
            words = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                words.add(line);
            }
            br.close();
        }

        Map<String, Double> wordMail = new HashMap<>();
        if (CollectionUtils.isEmpty(words)) {
            return wordMail;
        }
        words.forEach(w -> {
            Double count = wordMail.get(w);
            wordMail.put(w, count == null ? 1 : ++count);
        });
        Map<String, Double> wordMailFrequency = new HashMap<>();
        wordMail.forEach((word, count) -> wordMailFrequency.put(word, count / wordMail.size()));
        return wordMailFrequency;
    }

    /**
     * @Author: enHui.Chen
     * @Description: 计算当出现某个分词时该邮件是垃圾邮件的概率
     * @Data 2019/11/29
     */
    public static Map<String, Double> countWordSpamMailFrequency(Map<String, Double> spamMailRate, Map<String, Double> hamMailRate) {
        // P(spam|w)= P(w|spam)*P(spam)/(P(s|spam)*P(spam)+P(w|ham)*P(ham))(一般来说P(ham) = P(spam) = 0.5)
        Map<String, Double> wordSpamRate = new HashMap<>();
        spamMailRate.forEach((word, rate) -> {
            Double wordHam = hamMailRate.get(word);
            if (wordHam != null) {
                wordSpamRate.put(word, rate / (rate + wordHam));
            } else {
                // 如果该词不在ham的邮件中时,默认为1/ham.size()-Laplace校准
                wordSpamRate.put(word, rate / (rate + 1D / hamMailRate.size()));
            }
        });
        return wordSpamRate;
    }

    /**
     * @Author: enHui.Chen
     * @Description: 通过联合概率计算邮件是垃圾邮件的概率
     * @Data 2019/11/29
     */
    public static double isSpamMail(String source, Map<String, Double> wordSpamMailRate) {
        // P(Spam|t1,t2,t3……tn)=（P1*P2*……PN）/(P1*P2*……PN+(1-P1)*(1-P2)*……(1-PN)) :联合概率公式
        double unitSpam = 1D;
        double unitHam = 1D;
        List<Term> terms = segment(source);
        for (Term term : terms) {
            Double rate = wordSpamMailRate.get(term.getName());
            if (rate != null) {
                unitSpam *= rate;
                unitHam *= 1D - rate;
            }
        }
        // 当unitSpam,unitHam联合概率趋向0时,则是垃圾邮件的概率为0.5
        double probability;
        if (unitSpam == 0 && unitHam == 0) {
            probability = 0.5D;
        } else {
            probability = unitSpam / (unitSpam + unitHam);
        }
        return probability;
    }
}
