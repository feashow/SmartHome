package com.example.tzxing.smarthome;

public class DataProcess
{
    public String txt_liv_temperature;
    public String txt_liv_light;
    public String txt_liv_ir;
    public String txt_bed_temperature;
    public String txt_bed_light;
    public String txt_bed_ir;
    public String txt_kit_temperature;
    public String txt_kit_smoke;
    public String txt_cur_light;
    public String txt_cur_shock;
    public String txt_cur_state;
    public String kit_alert;
    public String liv_alert;
    public String bed_alert;
    public String cur_alert;
    public String balcony_alert;
    public String txt_balcony_temperature;
    public String txt_balcony_light;
    public String txt_balcony_humidity;

    public DataProcess(String s)
    {
        kit_alert = s.substring(17, 18);
        txt_kit_temperature = s.substring(19, 23);
        txt_kit_smoke = s.substring(24, 25);
        liv_alert = s.substring(27, 28);
        txt_liv_temperature = s.substring(29, 33);
        txt_liv_light = s.substring(34, 37);
        txt_liv_ir = s.substring(38, 40);
        bed_alert = s.substring(42, 43);
        txt_bed_temperature = s.substring(44, 48);
        txt_bed_light = s.substring(49, 52);
        txt_bed_ir = s.substring(53, 55);
        cur_alert = s.substring(57, 58);
        txt_cur_light = s.substring(59, 62);
        txt_cur_shock = s.substring(63, 65);
        txt_cur_state = s.substring(66, 67);
        balcony_alert = s.substring(71, 72);
        txt_balcony_temperature = s.substring(73, 77);
        txt_balcony_light = s.substring(78, 81);
        txt_balcony_humidity = s.substring(82, 85);


    }

}
