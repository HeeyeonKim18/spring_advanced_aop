package hello.aop.internalcall;

import hello.aop.internalcall.aop.CallLogAspect;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@Import(CallLogAspect.class)
@SpringBootTest
class CallServiceV0Test {
    @Autowired CallServiceV0 callServiceV0;

    /**
     * this는 실제 대상 객체(target)의 인스턴스를 뜻한다.
     * 결과적으로 자기 자신의 내부 메서드를 호출하는 프록시를 거치지 않으며 어드바이스 적용 x
     */
    @Test
    void external(){
        callServiceV0.external();
    }

    @Test
    void internal(){
        callServiceV0.internal();
    }

}