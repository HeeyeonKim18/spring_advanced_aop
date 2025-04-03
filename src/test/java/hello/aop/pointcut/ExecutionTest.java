package hello.aop.pointcut;

import hello.aop.member.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.*;

@Slf4j
public class ExecutionTest {

    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    Method helloMethod;

    @BeforeEach
    public void init() throws NoSuchMethodException {
        helloMethod = MemberServiceImpl.class.getMethod("hello", String.class);
    }

    @Test
    void printMethod() {
        // public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
        log.info("helloMethod={}", helloMethod);
    }

    @Test
    void exactMatch(){
        // public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
        pointcut.setExpression("execution(public String hello.aop.member.MemberServiceImpl.hello(String))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void allMatch(){
        pointcut.setExpression("execution(* *(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void nameMatch(){
        pointcut.setExpression("execution(* hello(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void nameMatchStar1(){
        pointcut.setExpression("execution(* hel*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void nameMatchStar2(){
        pointcut.setExpression("execution(* *el*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }


    @Test
    void nameMatchFalse(){
        pointcut.setExpression("execution(* nono(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }

    @Test
    void packageExactMatch1(){
        pointcut.setExpression("execution(* hello.aop.member.MemberServiceImpl.hello(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void packageExactMatch2(){
        pointcut.setExpression("execution(* hello.aop.member.*.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void packageMatchFalse(){
        pointcut.setExpression("execution(* hello.aop.*.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }

    @Test
    void packageMatchSubPackage1(){
        pointcut.setExpression("execution(* hello.aop.member..*.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void packageMatchSubPackage2(){
        pointcut.setExpression("execution(* hello.aop..*.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void typeExactMatch(){
        pointcut.setExpression("execution(* hello.aop.member.MemberServiceImpl.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void typeMatchSuperType(){
        // 부모 타입을 선언해도 그 자식 타입은 매칭 가능
        pointcut.setExpression("execution(* hello.aop.member.MemberService.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void typeMatchInternal() throws NoSuchMethodException {
        pointcut.setExpression("execution(* hello.aop.member.MemberServiceImpl.*(..))");
        Method internalMethod = MemberServiceImpl.class.getMethod("internal", String.class);
        assertThat(pointcut.matches(internalMethod, MemberServiceImpl.class)).isTrue();
    }


    @Test
    void typeMatchNoSuperTypeMethodFalse() throws NoSuchMethodException {
        // 부모 타입에 선언한 메서드가 아니기에 매칭 안됨
        pointcut.setExpression("execution(* hello.aop.member.MemberService.*(..))");
        Method internalMethod = MemberServiceImpl.class.getMethod("internal", String.class);
        assertThat(pointcut.matches(internalMethod, MemberServiceImpl.class)).isFalse();
    }

    @Test
    void argsMatch(){
        pointcut.setExpression("execution(* *(String))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void argsMatchNoArgs(){
        // 파라미터가 없어야 매칭됨
        pointcut.setExpression("execution(* *())");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }

    @Test
    void argsMatchStar(){
        // 하나의 파라미터, 모든 타입 허용
        pointcut.setExpression("execution(* *(*))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void argsMatchAll(){
        // 모든 파라미터, 모든 타입 허용
        pointcut.setExpression("execution(* *(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void argsMatchStartingWithString(){
        // 스트링으로 시작하고, 모든 파라미터, 모든 타입 허용
        pointcut.setExpression("execution(* *(String, ..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }
}
