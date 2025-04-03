package hello.aop.pointcut;

import hello.aop.member.MemberService;
import hello.aop.member.annotation.ClassAop;
import hello.aop.member.annotation.MethodAop;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

/**
 * 포인트컷을 사용해서 어드바이스에 매개변수 전달 가능
 * 매개변수 전달하기 위해선 포인트컷 이름과 매개변수 이름이 같아야 함
 * 추가적으로 타입이 메서드에 지정한 타입으로 제한됨
 */
@Slf4j
@SpringBootTest
@Import(ParameterTest.ParameterAspect.class)
public class ParameterTest {

    @Autowired
    MemberService memberService;

    @Test
    void success(){
        log.info("memberService Proxy={}", memberService.getClass());
        memberService.hello("helloA");
    }

    @Slf4j
    @Aspect
    static class ParameterAspect {

        @Pointcut("execution(* hello.aop.member..*.*(..))")
        private void allMember(){}

        @Around("allMember()")
        public Object logArgs1(ProceedingJoinPoint joinPoint) throws Throwable {
            Object arg1 = joinPoint.getArgs()[0];
            log.info("[logArgs1]{},  arg={}", joinPoint.getSignature(), arg1);
            return joinPoint.proceed();
        }

        @Around("allMember() && args(arg,..)")
        public Object logArgs2(ProceedingJoinPoint joinPoint, Object arg) throws Throwable {
            log.info("[logArgs2]{},  arg={}", joinPoint.getSignature(), arg);
            return joinPoint.proceed();
        }

        @Before("allMember() && args(arg,..)")
        public void logArgs3(String arg) throws Throwable {
            log.info("[logArgs3]{}", arg);
        }

        @Before("allMember() && this(obj)")
        public void thisArg(JoinPoint joinPoint, MemberService obj) throws Throwable {
            log.info("[this]{}, obj = {}", joinPoint.getSignature(), obj.getClass());
        }

        @Before("allMember() && target(obj)")
        public void targetArg(JoinPoint joinPoint, MemberService obj) throws Throwable {
            log.info("[target]{}, obj = {}", joinPoint.getSignature(), obj.getClass());
        }

        @Before("allMember() && @target(annotation)")
        public void atTargetArg(JoinPoint joinPoint, ClassAop annotation) throws Throwable {
            log.info("[@target]{}, annotation = {}", joinPoint.getSignature(), annotation);
        }

        @Before("allMember() && @within(annotation)")
        public void atWithinArg(JoinPoint joinPoint, ClassAop annotation) throws Throwable {
            log.info("[@within]{}, annotation = {}", joinPoint.getSignature(), annotation);
        }

        @Before("allMember() && @annotation(annotation)")
        public void atWithinArg(JoinPoint joinPoint, MethodAop annotation) throws Throwable {
            log.info("[@annotation]{}, annotation value = {}", joinPoint.getSignature(), annotation.value());
        }
    }
}
