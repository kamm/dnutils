package pl.kmetrak.dnutils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DNUtils {
    private static final String REGEX = "(([a-zA-Z]+?)=((?:(?![a-zA-Z]+?=.*?).)*))(,|(?!(,)$)(?!(.)))";

    public static String flatten(String dn){
        Pattern p = Pattern.compile(REGEX);
        List<String> list = new ArrayList<>();
        Matcher m = p.matcher(dn);
        
        while(m.find()){
            list.add(m.group(3));
        }
        StringBuffer sb = new StringBuffer();
        for(int i=0;i<list.size()-1;i++){
            sb.append(list.get(i)).append(", ");
        }
        sb.append(list.get(list.size()-1));
        return sb.toString();
                
    }
    
    public static String reverseDN(String dn){
        String [] split = splitDN(dn);
        StringBuffer sb = new StringBuffer();
        for(int i=split.length-1;i>=0;i--){
            sb.append(split[i]);
            if(i!=0){
                sb.append(",");
            }
        }
        
        return sb.toString();
                
    }
    
    public static String [] splitDN(String dn){
        Pattern p = Pattern.compile(REGEX);
        List<String> list = new ArrayList<>();
        Matcher m = p.matcher(dn);
        
        while(m.find()){
            list.add(m.group(1));
        }
        return list.toArray(new String[0]);
    }
    
    public static String getField(String dn, String fieldName){
        try{
            return getFields(dn, fieldName)[0];
        }catch(ArrayIndexOutOfBoundsException e){
            return null;
        }
    }
    
    public static String [] getFields(String dn, String fieldName){
        List<String> list = new LinkedList<>();
        for(String s:splitDN(dn)){
            if(fieldName.equalsIgnoreCase(s.substring(0, s.indexOf("=")))){
                list.add(s.substring(s.indexOf("=")+1));
            }
        }
        return list.toArray(new String[0]);
    }

    
    public static String normalizeDN(String dn){
        StringBuffer sb = new StringBuffer();
        String [] dnSplit = splitDN(dn);
        for(int i=0;i<dnSplit.length;i++){
            sb.append(dnSplit[i].substring(0, dnSplit[i].indexOf("=")).trim().toUpperCase());
            sb.append("=");
            sb.append(dnSplit[i].substring(dnSplit[i].indexOf("=")+1).trim());
            if(i!=dnSplit.length-1){
                sb.append(",");
            }
        }
        return sb.toString();
    }
    
    public static String [] treeDN(String dn){
        String [] s = splitDN(dn);
        List<String> list = new ArrayList<>();
        for(int i=0;i<s.length;i++){
            StringBuffer sb = new StringBuffer();
            for(int j=0;j<=i;j++){
                sb.append(s[j]);
                if(j!=i){
                    sb.append(",");
                }
            }
            list.add(sb.toString());
        }
        return list.toArray(new String[0]);
    }
}
