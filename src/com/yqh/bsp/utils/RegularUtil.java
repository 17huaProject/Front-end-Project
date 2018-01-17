package com.yqh.bsp.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularUtil {
	
	public static int idcardVerify(String idcard) {
		if (idcard.length() != 18 && idcard.length() != 15)
			return 1;
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
		String year = null;
		char c = idcard.charAt(idcard.length()-1);
        if(c!='1' && c!='2' && c!='3' && c!='4' && c!='5' && c!='6' && c!='7' && c!='8' && c!='9' && c!='0' && c!='x' && c!='X')
            return 1;
		try {
			StringBuffer ymd = new StringBuffer();
			if (idcard.length()==15) {
				Pattern pattern = Pattern.compile("\\d{15}"); 
				Matcher idMatcher = pattern.matcher(idcard);
				if (!idMatcher.matches())
					return 1;  
				year = "19" +idcard.substring(6,8);
				ymd.append(year).append(idcard.substring(8, 12));
			}else{
				Pattern pattern = Pattern.compile("\\d{17}(\\d|X|x)"); 
				Matcher idMatcher = pattern.matcher(idcard);
				if (!idMatcher.matches())
					return 1;  
				year = idcard.substring(6,10);
				ymd.append(idcard.substring(6,14));
				if (idcard.toUpperCase().charAt(17) != idcardVerifyCode(idcard.substring(0, 17))) {
					return 1;
				}
			}
			sf.setLenient(false);
			sf.parse(ymd.toString());
		}
		catch (java.text.ParseException parseexception) {
			return 1;  //日期格式有误
		}
		
	    Calendar cal = Calendar.getInstance();  
	    int iCurrYear = cal.get(Calendar.YEAR);  
	    if ((iCurrYear - Integer.valueOf(year)<18))
	    	return 2;  //未满18岁
		return 0;
	}
	
	public static char idcardVerifyCode(String id17) {
		char pszSrc[] = id17.toCharArray();
		int iS = 0;
		int iW[] = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };
		char szVerCode[] = new char[] { '1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2' };
		int i;
		for (i = 0; i < 17; i++) {
			iS += (int) (pszSrc[i] - '0') * iW[i];
		}
		int iY = iS % 11;
		return szVerCode[iY];
	} 
	
	public static boolean isEmail(String mail) {
		String emailAddressPattern = "\\b(^['_A-Za-z0-9-]+(\\.['_A-Za-z0-9-]+)*@([A-Za-z0-9-])+(\\.[A-Za-z0-9-]+)*((\\.[A-Za-z0-9]{2,})|(\\.[A-Za-z0-9]{2,}\\.[A-Za-z0-9]{2,}))$)\\b";
		boolean flag = false;
		flag = Pattern.matches(emailAddressPattern, mail);
		return flag;
	}
	
    public static boolean isPhoneNumber(String number) {
    	if(number == null || number.length()!= 11) {
    		return false;
    	} else {
    		return number.matches("^(13[0-9]|15[0-9]|18[0-9]|17[0-9]|19[0-9]|147|145)[0-9]{8}$");  //14是上网卡号码段
    	}
    }
    
    public static String sectionHide(String value){
    	if (value.length()==0) return "";
    	return value.substring(0,3) + "********" + value.substring(value.length()-4);
    }
    
    public static boolean isNumber(int len,String value) {
    	if (value.length() != len) {
    		return false;
    	}
    	try{ 
    		   Integer.valueOf(value);//转换成数字，如果不是数字，则会抛出异常，进入catch中 

    		}catch(Exception ex){ 
    			return false;
    		}
    	return true;
    }
    
    public static void main(String[] args) {
		System.out.println(idcardVerify("330802198109231654"));
	}
    
}
