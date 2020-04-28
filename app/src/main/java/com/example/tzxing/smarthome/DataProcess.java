package com.example.tzxing.smarthome;

//处理接收的数据，数据结构参见相关文档
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
    public String txt_balcony_temperature;
    public String txt_balcony_light;
    public String txt_balcony_humidity;

    public DataProcess(String s) {
        if(s.indexOf('L')!=-1) {
            int begin=s.indexOf('L');
            int end=0;
            for(int i=begin+1; i<s.length(); i++) {
                if(Character.isUpperCase(s.charAt(i))) {
                    end=i;
                    break;
                }else if(s.charAt(i)==';')
                    end=i;
            }
            String liv=s.substring(begin,end);
            if(liv.indexOf('t')!=-1)
                txt_liv_temperature=liv.substring(liv.indexOf('t')+1,liv.indexOf('t')+5);
            if(liv.indexOf('l')!=-1)
                txt_liv_light=liv.substring(liv.indexOf('l')+1,liv.indexOf('l')+4);
            if(liv.indexOf('i')!=-1)
                txt_liv_ir=liv.substring(liv.indexOf('i')+1,liv.indexOf('i')+3);
            if(liv.indexOf('w')!=-1)
                liv_alert=liv.substring(liv.indexOf('w')+1,liv.indexOf('w')+2);
        }

        if(s.indexOf('B')!=-1) {
            int begin=s.indexOf('B');
            int end=0;
            for(int i=begin+1; i<s.length(); i++) {
                if(Character.isUpperCase(s.charAt(i))) {
                    end=i;
                    break;
                }else if(s.charAt(i)==';')
                    end=i;
            }
            String bed=s.substring(begin,end);
            if(bed.indexOf('t')!=-1)
                txt_bed_temperature=bed.substring(bed.indexOf('t')+1,bed.indexOf('t')+5);
            if(bed.indexOf('l')!=-1)
                txt_bed_light=bed.substring(bed.indexOf('l')+1,bed.indexOf('l')+4);
            if(bed.indexOf('i')!=-1)
                txt_bed_ir=bed.substring(bed.indexOf('i')+1,bed.indexOf('i')+3);
            if(bed.indexOf('w')!=-1)
                bed_alert=bed.substring(bed.indexOf('w')+1,bed.indexOf('w')+2);
        }

        if(s.indexOf('K')!=-1) {
            int begin=s.indexOf('K');
            int end=0;
            for(int i=begin+1; i<s.length(); i++) {
                if(Character.isUpperCase(s.charAt(i))) {
                    end=i;
                    break;
                }else if(s.charAt(i)==';')
                    end=i;
            }
            String kit=s.substring(begin,end);
            if(kit.indexOf('t')!=-1)
                txt_kit_temperature=kit.substring(kit.indexOf('t')+1,kit.indexOf('t')+5);
            if(kit.indexOf('s')!=-1)
                txt_kit_smoke=kit.substring(kit.indexOf('s')+1,kit.indexOf('s')+2);
            if(kit.indexOf('w')!=-1)
                kit_alert=kit.substring(kit.indexOf('w')+1,kit.indexOf('w')+2);
        }

        if(s.indexOf('C')!=-1) {
            int begin=s.indexOf('C');
            int end=0;
            for(int i=begin+1; i<s.length(); i++) {
                if(Character.isUpperCase(s.charAt(i))) {
                    end=i;
                    break;
                }else if(s.charAt(i)==';')
                    end=i;
            }
            String cur=s.substring(begin,end);
            if(cur.indexOf('v')!=-1)
                txt_cur_shock=cur.substring(cur.indexOf('v')+1,cur.indexOf('v')+3);
            if(cur.indexOf('l')!=-1)
                txt_cur_light=cur.substring(cur.indexOf('l')+1,cur.indexOf('l')+4);
            if(cur.indexOf('c')!=-1)
                txt_cur_state=cur.substring(cur.indexOf('c')+1,cur.indexOf('c')+2);
            if(cur.indexOf('w')!=-1)
                cur_alert=cur.substring(cur.indexOf('w')+1,cur.indexOf('w')+2);
        }


        if(s.indexOf('T')!=-1) {
            int begin=s.indexOf('T');
            int end=0;
            for(int i=begin+1; i<s.length(); i++) {
                if(Character.isUpperCase(s.charAt(i))) {
                    end=i;
                    break;
                }else if(s.charAt(i)==';')
                    end=i;
            }
            String bal=s.substring(begin,end);
            if(bal.indexOf('t')!=-1)
                txt_balcony_temperature=bal.substring(bal.indexOf('t')+1,bal.indexOf('t')+5);
            if(bal.indexOf('l')!=-1)
                txt_balcony_light=bal.substring(bal.indexOf('l')+1,bal.indexOf('l')+4);
            if(bal.indexOf('h')!=-1)
                txt_balcony_humidity=bal.substring(bal.indexOf('h')+1,bal.indexOf('h')+4);
        }


    }

}