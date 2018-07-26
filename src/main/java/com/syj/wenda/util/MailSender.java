package com.syj.wenda.util;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.File;
import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;

//已经注释了所涉及的发送方邮箱账号，位于login页面的接收方邮箱账号也已经注释了
@Service
public class MailSender implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(MailSender.class);
    private JavaMailSenderImpl mailSender;

    @SuppressWarnings("deprecation")
//    用来抑制警告 Warning:(26, 39) java: freemarker.template.Configuration中的Configuration()已过时
    public boolean sendWithHTMLTemplate(String to, String subject, String template, Map<String, Object> model) {

        Configuration configuration = new Configuration();
        try {
            String nick = MimeUtility.encodeText("");
            InternetAddress from = new InternetAddress("XXX发送方XXX@qq.com");
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

//            获取类加载的根路径
//            File f1 = new File(this.getClass().getResource("/").getPath());
//            System.out.println("one" + f1);
//            获取当前类加载的路径
//            File f2 = new File(this.getClass().getResource("").getPath());
//            System.out.println( "two"+ f2);
//            获取项目路径
//            File dict = new File("");
//            String f3 = dict.getCanonicalPath();
//            System.out.println("three" + f3);

            File fff = new File("");
            String projectPath = fff.getCanonicalPath();
            String aimPath = projectPath + "\\src\\main\\resources\\templates\\mails";

            configuration.setDirectoryForTemplateLoading(new File(aimPath));
            Template t = configuration.getTemplate("login_exception.ftl");
            StringWriter writer = new StringWriter();
            t.process(model, writer);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(writer.toString());
            mailSender.send(mimeMessage);
            return true;
        } catch (Exception e) {
            logger.error("发送邮件失败:" + e.getMessage());
            return false;
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        mailSender = new JavaMailSenderImpl();
        mailSender.setUsername("XXX发送方XXX@qq.com");
        mailSender.setPassword("gtzgofibzehxghdg");
        mailSender.setHost("smtp.qq.com");
        mailSender.setPort(465);
        mailSender.setProtocol("smtps");
        mailSender.setDefaultEncoding("utf8");
        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.smtp.ssl.enable", true);
        //javaMailProperties.put("mail.smtp.auth", true);
        //javaMailProperties.put("mail.smtp.from", "XXXX发送方XXX@qq.com");
        //javaMailProperties.put("mail.smtp.starttls.enable", true);
        mailSender.setJavaMailProperties(javaMailProperties);
    }
}
