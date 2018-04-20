package com.gac.net.easy.cookie;
import com.gac.net.easy.EasyOkHttp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

import static okhttp3.internal.Util.UTC;

public class CookiesManager implements CookieJar {

       PersistentCookieStore cookieStore;
        public CookiesManager(){
            if(EasyOkHttp.mApplication != null)
                cookieStore = new PersistentCookieStore(EasyOkHttp.mApplication);
        }
        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
           if(cookies != null && cookies.size() > 0){
               for(Cookie cookie : cookies){
                   cookieStore.add(url,cookie);
               }
           }

        }


//: JSESSIONID=8B8B7E4D8A9674EC37B68690AA5F81D9;
    public String toString(Cookie cookie) {
        if(cookie == null){
            return "";
        }
        StringBuilder result = new StringBuilder();
        result.append(cookie.name());
        result.append('=');
        result.append(cookie.value());

        if (cookie.persistent()) {
            if (cookie.expiresAt() == Long.MIN_VALUE) {
                result.append("; max-age=0");
            } else {
                DateFormat rfc1123 = new SimpleDateFormat("EEE, dd-MMM-yyyy HH:mm:ss 'GMT'", Locale.US);
                rfc1123.setLenient(false);
                rfc1123.setTimeZone(UTC);

                result.append("; Expires=").append(rfc1123.format(cookie.expiresAt()));
            }
        }

        if (!cookie.hostOnly()) {
            result.append("; domain=").append(cookie.domain());
        }

        result.append("; Path=").append(cookie.path());

        if (cookie.secure()) {
            result.append("; secure");
        }

        if (cookie.hostOnly()) {
            result.append("; HttpOnly");
        }

        return result.toString();
    }

        @Override
        public List<Cookie> loadForRequest(HttpUrl url) {
            return  cookieStore.get(url);
        }

        public static String DATEFORMAT = "yyyy-MM-dd HH:mm:ss";
        public static String getDateStr(Date date){
             return new SimpleDateFormat(DATEFORMAT).format(date);
        }
        public static Date calendar2Date(Calendar calendar){
            return calendar.getTime();
        }

}