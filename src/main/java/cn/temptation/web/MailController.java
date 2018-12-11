package cn.temptation.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * 邮件控制器
 */
@Controller
@RequestMapping("/mail")
public class MailController {
    @Autowired
    private JavaMailSender mailSender;

    @RequestMapping("/view")
    public String view() {
        return "mail";
    }

    @RequestMapping("/sendMail")
    @ResponseBody
    public Map<String, Object> sendMail(@RequestParam("mailTo") String mailTo,
                                        @RequestParam("subject") String subject,
                                        @RequestParam("content") String content) {
        Map<String, Object> result = new HashMap<>();
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("905231404@qq.com"); // 必须和配置的spring.mail.username内容相同
            message.setTo(mailTo);
            message.setSubject(subject);
            message.setText(content);

            mailSender.send(message);
            result.put("success", true);
        } catch (Exception ex) {
            ex.printStackTrace();
            result.put("success", false);
        } finally {
            return result;
        }
    }
}