package load.prop;

public class Address {
    private String street;
    private String home;

    public String getStreet() {
        return street;
    }

    public String getHome() {
        return home;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setHome(String home) {
        this.home = home;
    }

    @Override
    public String toString() {
        return "street : " + street +", home : " + home;
    }
}

