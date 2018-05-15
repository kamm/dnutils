package pl.kmetrak.dnutils;

import java.nio.charset.StandardCharsets;

public class LDAPEscape {

    public static void main(String[] args) {
        System.out.println(escapeRDN("ęóąśłżźćń"));
    }

    
    public static String escapeRDN(String dn) {
        String attributeValue; 
        StringBuffer escapedDN = new StringBuffer();
        String subStr;
        int i=0;
        int index =-1;
        int nextIndex=-1;
        if(dn!=null)
        index=dn.indexOf("=");
        if(index!=-1){
            index = dn.indexOf("=");
            escapedDN.append(dn.substring(0,index+1));
            for(i=index+1;i<dn.length();i++){
                nextIndex = dn.substring(i).indexOf("=");
                if(nextIndex!=-1){
                    subStr = dn.substring(i,i+nextIndex);
                    int len=subStr.length();
                    int j=len-1;
                    int commaIndex=0;
                    char ch[] = subStr.toCharArray();
                    while(j>0){
                        if(ch[j]==','){
                            commaIndex=len-j;
                            break;
                        }
                        j--;

                    }
                    attributeValue = subStr.substring(0,commaIndex);
                    attributeValue = Rdn.escapeValue(attributeValue);
                    escapedDN.append(attributeValue);
                    escapedDN.append(subStr.substring(commaIndex));
                    escapedDN.append("=");
                    i=i+nextIndex;


                }
                else{
                    subStr = dn.substring(i);
                    subStr = Rdn.escapeValue(subStr);
                    escapedDN.append(subStr);
                    break;
                }

            }

        }else
        {
            System.out.println("Invalid DN");
        }

        dn=escapedDN.toString();

        return dn;
    }
}
