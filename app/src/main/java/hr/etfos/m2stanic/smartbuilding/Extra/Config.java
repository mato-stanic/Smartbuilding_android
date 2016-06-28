package hr.etfos.m2stanic.smartbuilding.Extra;
public class Config {

    public static boolean productionDeploy = true;

    //production urls
    public static String prodApiDeleteCron = "http://89.107.57.144:8080/smartbuilding/android/admin/apartmentLayout/deleteCron";
    public static String prodApiEditAdvanced = "http://89.107.57.144:8080/smartbuilding/android/admin/apartmentLayout/editAdvanced";
    public static String prodApiEditSimple = "http://89.107.57.144:8080/smartbuilding/android/admin/apartmentLayout/editSimple";
    public static String prodApiCronList = "http://89.107.57.144:8080/smartbuilding/android/admin/apartmentLayout/cronList";
    public static String prodApiLogin = "http://89.107.57.144:8080/smartbuilding/android/admin/login";
    public static String prodApiApartmentLayout = "http://89.107.57.144:8080/smartbuilding/android/admin/apartmentLayout";




    //testing url, for local deploy
    public static String apiDeleteCron = "http://192.168.178.33:8080/smartbuilding/android/admin/apartmentLayout/deleteCron";
    public static String apiEditAdvanced = "http://192.168.178.33:8080/smartbuilding/android/admin/apartmentLayout/editAdvanced";
    public static String apiEditSimple = "http://192.168.178.33:8080/smartbuilding/android/admin/apartmentLayout/editSimple";
    public static String apiCronList = "http://192.168.178.33:8080/smartbuilding/android/admin/apartmentLayout/cronList";
    public static String apiLogin = "http://192.168.178.33:8080/smartbuilding/android/admin/login";
    public static String apiApartmentLayout = "http://192.168.178.33:8080/smartbuilding/android/admin/apartmentLayout";



}
