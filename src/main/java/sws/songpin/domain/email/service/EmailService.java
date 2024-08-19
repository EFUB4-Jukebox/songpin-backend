package sws.songpin.domain.email.service;

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
import sws.songpin.domain.email.dto.EmailRequestDto;
import jakarta.mail.internet.MimeMessage;
import sws.songpin.domain.email.dto.ReportRequestDto;
import sws.songpin.domain.member.service.MemberService;
import sws.songpin.global.auth.RedisService;
import sws.songpin.global.exception.CustomException;
import sws.songpin.global.exception.ErrorCode;


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

        String toMail = requestDto.email();

        //요청받은 이메일로 가입한 회원이 탈퇴하거나 없는 경우 예외 처리
        memberService.getActiveMemberByEmail(toMail);

        String uuid = UUID.randomUUID().toString().replaceAll("-", "");

        String title = "[송핀] 비밀번호 재설정 링크 전송"; //이메일 제목
        String link = passwordUrl + "/" + uuid;

        HashMap<String,Object> map = new HashMap<>();
        map.put("link", link);

        Context context = new Context();
        context.setVariables(map); //템플릿에 전달할 데이터
        String content = templateEngine.process("passwordResetMail.html", context);

        //Redis 에 (UUID,Email) 쌍 저장
        if(!redisService.setValuesWithTimeoutIfAbsent("password_uuid:"+uuid, toMail, Duration.ofMinutes(10))){
            throw new CustomException(ErrorCode.ERROR);
        }

        //메일 전송
        sendEmail(toMail,title,content);

    }

    public void sendReportEmail(ReportRequestDto requestDto){
        Long reporterId = requestDto.reporterId();
        Long reportedId = requestDto.reportedId();
        String reportedHandle = memberService.getActiveMemberById(reportedId).getHandle();
        String reportType = requestDto.reportType().toString();
        String reason = requestDto.reason();

        //신고자 ID 와 신고 대상 ID 가 동일한 경우
        if(reporterId.equals(reportedId)){
            throw new CustomException(ErrorCode.REPORT_BAD_REQUEST);
        }

        String title = "[유저 신고] " + reporterId + " → " + reportedId; //이메일 제목

        HashMap<String,Object> map = new HashMap<>();
        map.put("reporterId", reporterId);
        map.put("reportedId", reportedId);
        map.put("reportedUrl", "https://www.songpin.kr/users/"+reportedHandle);
        map.put("reportType", reportType);
        map.put("reason", reason);

        Context context = new Context();
        context.setVariables(map); //템플릿에 전달할 데이터
        String content = templateEngine.process("reportMail.html", context);

        //메일 전송
        sendEmail(fromMail,title,content);
    }

    private void sendEmail(String toMail, String title, String content){
        try{
            MimeMessage mailMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true, "UTF-8");
            helper.setFrom(fromMail); //이메일 발신 주소
            helper.setTo(toMail);  //이메일 송신 주소
            helper.setSubject(title);  //이메일 제목
            helper.setText(content, true);
            helper.addInline("logo", new ClassPathResource(logoPath));

            javaMailSender.send(mailMessage);
        } catch (MessagingException e){
            throw new CustomException(ErrorCode.EMAIL_ERROR);
        }
    }

}
