package com.backend.sevenX.utills;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.*;
import java.util.regex.Pattern;

public class General {

    public static String hashPassword(String password_plaintext) {
        String salt = BCrypt.gensalt(Constant.JwtConst.WORKLOAD);
        String hashed_password = BCrypt.hashpw(password_plaintext, salt);
        return(hashed_password);
    }

    public static boolean checkPassword(String password_plaintext, String stored_hash) {
        boolean password_verified = false;

        if(null == stored_hash)
            throw new IllegalArgumentException("Invalid hash provided for comparison");

        password_verified = BCrypt.checkpw(password_plaintext, stored_hash);

        return(password_verified);
    }

    public static String createSignUpCode() {
        // syntax we would like to generate is DIA123456-A1B34
        String val = "";

        // char (1), random A-Z
        int ranChar = 65 + (new Random()).nextInt(90 - 65);
        char ch = (char) ranChar;
        val += ch;

        // numbers (6), random 0-9
        // char or numbers (7), random 0-9 A-Z
        for (int i = 0; i < 7; ) {
            int ranAny = 48 + (new Random()).nextInt(90 - 65);

            if (!(57 < ranAny && ranAny <= 65)) {
                char c = (char) ranAny;
                val += c;
                i++;
            }

        }

        return val;
    }

    public static boolean isValidEmail(String email)
    {
        String emailRegex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    public static ModelMapper getStrictMapper() {
        ModelMapper strictMapper = new ModelMapper();
        strictMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return strictMapper;
    }

}
