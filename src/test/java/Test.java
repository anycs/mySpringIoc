import com.space.bean.User;
import com.space.core.ClassPathXmlApplicationContext;

/**
 * Created by lucifel on 19-7-31.
 */
public class Test {


    @org.junit.Test

    public void test(){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("bean.xml");
        User user = (User) context.getBean("user");
        System.out.println(user);
    }
}
