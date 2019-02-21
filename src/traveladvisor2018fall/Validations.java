/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package traveladvisor2018fall;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author SURESH
 */
public class Validations {

    public static boolean PasswordValidations(String loginId, String password) {
        if (loginId.equals(password)) {
            return false;
        }

        return true;
    }

    public static boolean loginIdValidations(String loginId) {
        //look into regex for login validations
        /*
            . any character
            ? 0 or 1 time    
            + one ore more times
            * 0 or more times
        */
        
        //Regex for at least one letter and one digit
        String regex1 = "[a-zA-Z]+[0-9]+.*|[0-9]+[a-zA-Z]+.*";
        
        if(loginId.length() < 3 || loginId.length() > 10) {
            return false;
        } 
        
        if(Pattern.matches(regex1, loginId)) {
            return true;
        } else {
            return false;
        }
    }
}
