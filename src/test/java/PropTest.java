import load.prop.Address;
import load.prop.Prop;
import load.prop.Property;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;


import static org.junit.Assert.fail;


public class PropTest {

    private static Prop obj1;
    private final double er = 1e-9;

    @BeforeClass
    public static void createObject() {
        Address adr = new Address();
        obj1 = Prop.getInstance();
        obj1.doRefresh(obj1,"src/main/resources/config.properties");
    }

    @Test
    public void singletonShouldWork() {
        Address adr2 = new Address();
        obj1 = Prop.getInstance();
        Prop obj2 = Prop.getInstance();
        Assert.assertSame(obj1, obj2);
    }

    @Test
    public void missingFieldShouldBeDefaultOrNull() throws IllegalAccessException {

        Field[] fields = obj1.getClass().getDeclaredFields();
        boolean isTrue = false;
        Properties prop = new Properties();
        try {
            FileInputStream fileInputStream = new FileInputStream("src/main/resources/config.properties");
            prop.load(fileInputStream);
        } catch (IOException e) {

        }
        for (Field field : fields) {
            if (field.isAnnotationPresent(Property.class)) {
                String emptyProperty = prop.getProperty(field.getAnnotation(Property.class).propertyName());
                if (emptyProperty == null) {
                    if(!field.isAccessible()){
                        field.setAccessible(true);
                    }
                    Object value = field.get(obj1).toString().replace("street : Default address, home : ", "");

                    if (field.getAnnotation(Property.class).def().equals("null")) {
                        isTrue = value==null;
                        if(isTrue != true)
                        {
                            fail();
                        }
                    }
                     else {
                       isTrue = value.toString().equals(field.getAnnotation(Property.class).def());
                        if(isTrue != true)
                        {
                            fail();
                        }
                    }

                }
                else {
                    isTrue = true;
                }
            }

        }
        Assert.assertTrue(isTrue);

    }

    @Test(expected = ClassCastException.class)
    public void propMustBeTreadSafety() throws InterruptedException,ClassCastException {
        Address adr2 = new Address();
        obj1 = Prop.getInstance();
        Thread t1 = new Thread((Runnable) obj1);
        Prop obj2 = Prop.getInstance();
        Thread t2 = new Thread((Runnable) obj2);
        t1.start();
        t2.start();
        t1.join(10000);
        t2.join(10000);
    }
}
