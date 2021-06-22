package com.njbandou.web;

import io.jsonwebtoken.*;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.HashMap;
import java.util.Map;

public class JWTUtils {

    public static String generateToken(String secret, Map<String, Object> claims){

        String compactJws = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
        return compactJws;
    }

    public static Map getClaims(String secret, String token){

        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token);
            return claims.getBody();
        } catch (MissingClaimException e) {
            e.printStackTrace();
            // we get here if the required claim is not present
        } catch (IncorrectClaimException e) {
            e.printStackTrace();
            // we get here if the required claim has the wrong value
        }
        return null;
    }

    public static SecretKey generalKey(String stringKey) {
        byte[] encodedKey = stringKey.getBytes();
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        return key;
    }

    public static Map enCoded(String secret, String token){
        SecretKey key = generalKey(secret);
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token);
            return claims.getBody();
        } catch (MissingClaimException e) {
            e.printStackTrace();
        } catch (IncorrectClaimException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        Map map = new HashMap();
        String[] s = new String[]{"11","22","33"};
        map.put("sample1",s);
        map.put("sample","test");
        Map result = enCoded("a7493483-dd77-400b-8654-68824e8d3c1e","eyJhbGciOiJIUzI1NiJ9.eyJpZCI6IjEiLCJwaG9uZSI6IjE4MjYwMDM1MjA5IiwidGVzdFRpbWUiOiIyMDIxMDQxNjA5MzU1MCIsInBvc3R1cmVIZWFsdGgiOjg4LCJwYXJ0RXZhbHVhdGlvbkRhdGEiOlt7InBvc2l0aW9uIjowLCJyZXN1bHQiOjAsInZhbHVlIjotMX0seyJwb3NpdGlvbiI6MSwicmVzdWx0IjowLCJ2YWx1ZSI6LTF9LHsicG9zaXRpb24iOjIsInJlc3VsdCI6MCwidmFsdWUiOi0xfV0sImJvbmVTaW11bGF0aW9uIjp7InBpY3R1cmUiOiIxIiwiY3VycmVudEZyb250IjoiMiIsImN1cnJlbnRTaWRlIjoiMyJ9LCJtdXNjbGVDb25kaXRpb24iOlt7Im11c2NsZUdyb3VwIjowLCJzdGF0dXMiOjAsIm11c2NsZSI6IjEyMTIsMjIifV0sImNlbnRlckdyYXZpdHkiOnsieCI6MSwieSI6MX0sImV4ZXJjaXNlUHJlc2NyaXB0aW9uIjpbeyJhY3Rpb24iOjF9XX0.Mej9RTqT2Lk84ItczIKXqYid4oXc7q4i1Jb6q3zREJk");
        System.out.println(result);
        System.out.println(result.get("id"));
//        System.out.println(generateToken("a7493483-dd77-400b-8654-68824e8d3c1e",map));
    }
}
