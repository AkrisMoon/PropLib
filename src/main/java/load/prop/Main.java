package load.prop;

public class Main {
    public static void main(String[] args) {
        if (args.length > 0){
            Prop obj = Prop.getInstance();
            showFields(obj);
            System.out.println();
            obj.doRefresh(obj,args[0]);
            showFields(obj);
        }
    }

    public static void showFields(Prop obj){
        System.out.println("Company name: " + obj.getMyCompanyName());
        System.out.println("Company owner: " +obj.getMyCompanyOwner());
        System.out.println("Company address: " +obj.getAddress());
        System.out.println("Company years old: " +obj.getMyCompanyOwnerYearsOld());
    }
}
