package load.prop;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;



public class Prop {

    private static final Logger logger = Logger.getLogger(Prop.class);

    //   PATH_TO_PROPERTIES = src/main/resources/config.properties
    @Property(propertyName = "com.mycompany.name")
    private static String myCompanyName;
    @Property(propertyName = "com.mycompany.Owner", def = "Default owner")
    private static String myCompanyOwner;
    @Property(propertyName = "com.mycompany.address", def = "Default address")
    private static Address address;
    @Property(propertyName = "com.mycompany.years.old", def = "0")
    private static Integer myCompanyOwnerYearsOld;
    Properties prop = new Properties();

    private static Prop instance;

    private Prop(String myCompanyName, String myCompanyOwner, Address address, int myCompanyOwnerYearsOld) {
        this.myCompanyName = myCompanyName;
        this.myCompanyOwner = myCompanyOwner;
        this.address = address;
        this.myCompanyOwnerYearsOld = myCompanyOwnerYearsOld;
    }

    public static Prop getInstance() {
        // если ранее не был создан единственный объект
        if (instance == null) {
            address = new Address();
            address.setHome("Cupertino");
            address.setStreet("California");
            instance = new Prop("Apple", "Tim",  address, 44);
        }
        return instance;
    }

    public static String getMyCompanyName() {
        return myCompanyName;
    }

    public static String getMyCompanyOwner() {
        return myCompanyOwner;
    }

    public static Address getAddress() {
        return address;
    }

    public static Integer getMyCompanyOwnerYearsOld() {
        return myCompanyOwnerYearsOld;
    }

    public Field propNotFound(Field field, Object object)
    {
        String type = field.getType().toString().replace("class java.lang.", "").replace("class load.prop.", "");
        try {
            if (field.getAnnotation(Property.class).def().equals("null"))
            {
                logger.log(Level.WARN,"\n" + "No field found in *.properties file with name " + field.getName()+" and value: "+ field.getAnnotation(Property.class).propertyName() + ".Default value for a field " + field.getName()+" not found, field initialized to null");
                field.set(object, null);
            }
            else {
                logger.log(Level.WARN,"No field found in *.properties file with name " + field.getName()+" and value: "+ field.getAnnotation(Property.class).propertyName() + ", the field is initialized with the default value: "+ field.getAnnotation(Property.class).def()+".");
                try {
                    if (type.equals("String")) {
                        field.set(object, field.getAnnotation(Property.class).def());
                    } else if (type.equals("Integer")) {
                        field.set(object, Integer.parseInt(field.getAnnotation(Property.class).def()));
                    } else if (type.equals("Double")) {
                        field.set(object, Double.parseDouble(field.getAnnotation(Property.class).def()));
                    } else if (type.equals("Address")) {
                        field.setAccessible(true);
                        Address address = new Address();
                        address.setHome(field.getAnnotation(Property.class).def());
                        address.setStreet(field.getAnnotation(Property.class).def());
                        field.set(object, address);
                    }
                } catch (IllegalAccessException e) {
                    logger.log(Level.ERROR, e);
                }
            }
        }catch (Exception e){
            logger.log(Level.ERROR, e);
        }
        return field;
    }

    public synchronized void doRefresh(Object object,String path) {
        FileInputStream fileInputStream;

        try {
            fileInputStream = new FileInputStream(path);
            prop.load(fileInputStream);
        } catch (IOException e) {
            logger.log(Level.ERROR, "Error in the program: file " + path + " not found. " + e);
            e.printStackTrace();
        }
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {

            if (field.isAnnotationPresent(Property.class)) {
                String type = field.getType().toString().replace("class java.lang.", "").replace("class load.prop.", "");
                String emptyProperty = prop.getProperty(field.getAnnotation(Property.class).propertyName());
                try{
                    if (emptyProperty == null) {
                       propNotFound(field,object);

                    } else {
                        try {
                        if (type.equals("String")) {

                                field.set(object, prop.getProperty(field.getAnnotation(Property.class).propertyName()));

                        } else if (type.equals("Integer")) {
                            field.set(object, Integer.parseInt(prop.getProperty(field.getAnnotation(Property.class).propertyName())));
                        } else if (type.equals("Double")) {
                            field.set(object, Double.parseDouble(prop.getProperty(field.getAnnotation(Property.class).propertyName())));
                        } else if (type.equals("Address")) {
                            String stringAddress = prop.getProperty(field.getAnnotation(Property.class).propertyName()).replaceAll("[//{//}]", "");

                            Map<String, String> myMap = new HashMap();

                            boolean accessible = field.isAccessible();
                            field.setAccessible(true);

                            String[] pairs = stringAddress.split(",");
                            for (int i = 0; i < pairs.length; i++) {
                                String pair = pairs[i];
                                String[] keyValue = pair.split(":");
                                myMap.put(keyValue[0].trim(), String.valueOf(keyValue[1]).trim());
                            }
                            Address address = new Address();
                            address.setHome(myMap.get("home"));
                            address.setStreet(myMap.get("street"));
                            field.set(object, address);

                        }
                        } catch (IllegalAccessException e) {
                            logger.log(Level.ERROR, e);
                        }

                    }
                } catch ( IllegalArgumentException | NullPointerException e) {
                    e.printStackTrace();
                    propNotFound(field,object);

                    }
            }

        }

    }


}
