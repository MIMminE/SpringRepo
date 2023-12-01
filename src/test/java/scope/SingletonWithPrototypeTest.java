package scope;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import static org.assertj.core.api.Assertions.*;

public class SingletonWithPrototypeTest {

    @Test
    void singletonClientUsePrototype(){
        ApplicationContext ac =
                new AnnotationConfigApplicationContext(ClientBean.class, PrototypeBean.class);

        ClientBean clientBean1 = ac.getBean(ClientBean.class);
        int count1 = clientBean1.logic();
        assertThat(count1).isEqualTo(1);

        ClientBean clientBean2 = ac.getBean(ClientBean.class);
        int count2 = clientBean2.logic();
        assertThat(count2).isEqualTo(2);

    }

    @Scope("singleton")
    static class ClientBean{
        //private final PrototypeBean prototypeBean;
        private ObjectProvider<PrototypeBean> prototypeBeantProvider;

        public ClientBean(ObjectProvider<PrototypeBean> prototypeBeantProvider) {
            this.prototypeBeantProvider = prototypeBeantProvider;
        }

        public int logic(){
            PrototypeBean prototypeBean = prototypeBeantProvider.getObject();
            prototypeBean.addCount();
            return prototypeBean.getCount();
        }
    }

    @Scope("prototype")
    static class PrototypeBean{
        private int count = 0;

        public void addCount(){
            count++;
        }

         public int getCount() {
             return count;
         }

         @PostConstruct
         public void init() {
             System.out.println("PrototypeBean.init " + this);
         }

         @PreDestroy
         public void destroy() {
            System.out.println("PrototypeBean.destroy");
         }
    }
}