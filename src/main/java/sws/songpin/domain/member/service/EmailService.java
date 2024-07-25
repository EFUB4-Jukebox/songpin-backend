package sws.songpin.domain.member.service;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import sws.songpin.domain.member.dto.request.EmailRequestDto;
import jakarta.mail.internet.MimeMessage;
import sws.songpin.global.auth.RedisService;
import sws.songpin.global.exception.CustomException;
import sws.songpin.global.exception.ErrorCode;


import javax.xml.catalog.Catalog;
import java.time.Duration;
import java.util.HashMap;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class EmailService {

    @Value("${mail.username}")
    private String fromMail;
    @Value("${mail.properties.url}")
    private String passwordUrl;
    @Value("${mail.templates.img.logo}")
    private String logoPath;

    private final JavaMailSender javaMailSender;
    private final MemberService memberService;
    private final RedisService redisService;
    private final SpringTemplateEngine templateEngine;

    public void sendPasswordEmail(EmailRequestDto requestDto){

        String email = requestDto.email();

        //요청받은 이메일로 가입한 회원이 탈퇴하거나 없는 경우 예외 처리



        String uuid = UUID.randomUUID().toString().replaceAll("-", "");



        String title = "[송핀] 비밀번호 재설정 링크 전송"; //이메일 제목
        String link = passwordUrl + "/" + uuid;

        HashMap<String,Object> map = new HashMap<>();
        map.put("link", link);

        Context context = new Context();
        context.setVariables(map); //템플릿에 전달할 데이터
        String content = templateEngine.process("passwordResetMail.html", context);

        //Redis 에 (UUID,Email) 쌍 저장
        if(!redisService.setValuesWithTimeoutIfAbsent("password_uuid:"+uuid, email, Duration.ofMinutes(10))){
            throw new CustomException(ErrorCode.ERROR);
        }

        //메일 전송
        try{
            MimeMessage mailMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true, "UTF-8");
            helper.setFrom(fromMail); //이메일 발신 주소
            helper.setTo(email);  //이메일 송신 주소
            helper.setSubject(title);  //이메일 제목
            helper.setText(content, true);
            helper.addInline("logo", new ClassPathResource(logoPath));

            javaMailSender.send(mailMessage);
        } catch (MessagingException e){
            throw new CustomException(ErrorCode.EMAIL_ERROR);
        }


    }

}
