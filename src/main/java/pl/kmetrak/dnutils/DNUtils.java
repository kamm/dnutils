package pl.kmetrak.dnutils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DNUtils {
    private static final String REGEX = "(([a-zA-Z]+?)=((?:(?![a-zA-Z]+?=.*?).)*))(,|(?!(,)$)(?!(.)))";

    /**
     * Get flattened version of DN. In such version there are no field names, field values are separated with comma.
     * @param dn
     * @return flattened DN
     */
    public static String flatten(String dn) {
        Pattern p = Pattern.compile(REGEX);
        List<String> list = new ArrayList<>();
        Matcher m = p.matcher(dn);

        while (m.find()) {
            list.add(m.group(3));
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < list.size() - 1; i++) {
            sb.append(list.get(i)).append(", ");
        }
        sb.append(list.get(list.size() - 1));
        return sb.toString();
    }

    /**
     * Reverse order of fields in DN
     * @param dn
     * @return reversed DN
     */
    public static String reverseDN(String dn) {
        String[] split = splitDN(dn);
        StringBuffer sb = new StringBuffer();
        for (int i = split.length - 1; i >= 0; i--) {
            sb.append(split[i]);
            if (i != 0) {
                sb.append(",");
            }
        }

        return sb.toString();

    }

    /**
     * Splits DN into sepparate fields as strings "field=value", respecting field elemenets such as commas in field value
     * @param dn
     * @return array of fields
     */
    public static String[] splitDN(String dn) {
        Pattern p = Pattern.compile(REGEX);
        List<String> list = new ArrayList<>();
        Matcher m = p.matcher(dn);

        while (m.find()) {
            list.add(m.group(1));
        }
        return list.toArray(new String[0]);
    }

    /**
     * Get value of first field with given name or null if field doesn't exist
     * @param dn
     * @param fieldName name of field
     * @return value of first field of given name or null if field doesn't exist
     */
    public static String getField(String dn, String fieldName) {
        try {
            return getFields(dn, fieldName)[0];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * Get value of first field with given name or empty string if field doesn't exist
     * @param dn
     * @param fieldName name of field
     * @return value of first field of given name or empty string if field doesn't exist
     */
    public static String getFieldNotNull(String dn, String fieldName) {
        return getFieldsNotNull(dn, fieldName)[0];
    }

    /**
     * Get values of fields with given name as array of string or empty string array if field doesn't exist
     * @param dn
     * @param fieldName
     * @return values of fields with given name as array of string or empty string array if field doesn't exist
     */
    public static String[] getFields(String dn, String fieldName) {
        List<String> list = new LinkedList<>();
        for (String s : splitDN(dn)) {
            if (fieldName.equalsIgnoreCase(s.substring(0, s.indexOf("=")))) {
                list.add(s.substring(s.indexOf("=") + 1));
            }
        }
        return list.toArray(new String[0]);
    }

    /**
     * Get values of fields with given name as array of string or single element string array containing empty string if field doesn't exist
     * @param dn
     * @param fieldName
     * @return values of fields with given name as array of string or single element string array containing empty string if field doesn't exist
     */
    public static String[] getFieldsNotNull(String dn, String fieldName) {
        List<String> list = new LinkedList<>();
        for (String s : splitDN(dn)) {
            if (fieldName.equalsIgnoreCase(s.substring(0, s.indexOf("=")))) {
                list.add(s.substring(s.indexOf("=") + 1));
            }
        }
        if (list.size() > 0) {
            return list.toArray(new String[0]);
        } else {
            return new String[] { "" };
        }
    }

    /**
     * Get normalized dn. Every field name is converted to uppercase
     * @param dn
     * @return normalized DN
     */
    public static String normalizeDN(String dn) {
        StringBuffer sb = new StringBuffer();
        String[] dnSplit = splitDN(dn);
        for (int i = 0; i < dnSplit.length; i++) {
            sb.append(dnSplit[i].substring(0, dnSplit[i].indexOf("=")).trim().toUpperCase()).append("=").append(dnSplit[i].substring(dnSplit[i].indexOf("=") + 1).trim());
            if (i != dnSplit.length - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    /**
     * Crete array of sub-DNs. Every element indicates subsequent leafs in tree
     * @param dn
     * @return array of sub-DNs
     */
    public static String[] treeDN(String dn) {
        String[] s = splitDN(dn);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < s.length; i++) {
            StringBuffer sb = new StringBuffer();
            for (int j = 0; j <= i; j++) {
                sb.append(s[j]);
                if (j != i) {
                    sb.append(",");
                }
            }
            list.add(sb.toString());
        }
        return list.toArray(new String[0]);
    }

    /**
     * Escape values of fields in DN
     * @param dn
     * @return
     */
    public static String escapeDN(String dn){
        Pattern p = Pattern.compile(REGEX);
        Matcher m = p.matcher(dn);
        List<String> list = new ArrayList<>();
        
        while(m.find()){
            StringBuffer sb = new StringBuffer();
            sb
                .append(m.group(2))
                .append("=")
                .append(
                        escapeDNField(
                                m.group(3)
                        )
                );
            list.add(sb.toString());
            
        }
        
        return String.join(",", list);
    }

    /**
     * Escape single field
     * @param field
     * @return
     */
    public static String escapeDNField(String field) {
        try {
            return URLEncoder.encode(field, "UTF-8").replace("+", " ").replace("%", "\\").replace("\\2C", "\\,");
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError("UTF-8 is unknown");
        }
    }
}
