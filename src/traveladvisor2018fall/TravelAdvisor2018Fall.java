/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package traveladvisor2018fall;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author SURESH
 */
public class TravelAdvisor2018Fall {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        Account account = new Account();
        System.out.println("====WELCOME TO TRAVEL ADVISOR APPLICATION====\n");
       
        account.Home();
    }
    
}
