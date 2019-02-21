/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package traveladvisor2018fall;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author SURESH
 */
public class Account {

    public static String loginId;

    Scanner in = new Scanner(System.in);
    DBConnection dbconnection = new DBConnection();

    public void Home() {

        System.out.println("Please enter \n1 to login \n2 to register \n0 to exit from the application");
        Scanner in = new Scanner(System.in);
        
        try {
            int option = in.nextInt();
            if (option == 1) {
                //login
                Login();

            } else if (option == 2) {
                //register
                Register();

            } else if (option == 0) {
                System.out.println("Exiting from the application \nThanks for using Travel Advisor");
                System.exit(0);

            } else {
                //entered wrong selection
                System.out.println("\nyou made invalid selection");
                Home();
            }
        } catch (InputMismatchException e) {
            System.out.println("*****Please enter valid input*****");
            Home();
        }

    }

    public void Login() {
        System.out.println("Please enter your login Id");
        loginId = in.next();

        //Admin login
        if (loginId.equals("admin")) {
            System.out.println("Please enter your password");
            String password = in.next();
            if (password.equals("admin")) {
                System.out.println("You logged in sucessfully");
                Home home = new Home();
                home.adminHome();
            } else {
                System.out.println("Wrong password for the admin user");

            }
        } else if (loginId != "admin") {

            Connection con = dbconnection.getDBConnection();

            try {
                //create statement
                Statement st = con.createStatement();

                //execute database query
                String sqlQuery = "select * from login where loginid = '" + loginId + "'";

                ResultSet result = st.executeQuery(sqlQuery);
                System.out.println("Please enter your password");
                String password = in.next();

                if (result.next()) {
                    String rsPassword = result.getString("password");
                    //System.out.println("the password of the user " + loginId + " is: " + rsPassword);

                    if (rsPassword.equals(password)) {
                        System.out.println("You have logged in sucessfully\n");

                        Home home = new Home();
                        home.userHome();

                        //Do whatever needs to be displayed after login
                    } else {
                        //wrong credentials
                        System.out.println("You entered invalid credetials");
                        Login();
                    }
                } else {
                    //wrong credentials
                    System.out.println("You entered invalid credetials");
                    Login();
                }

            } catch (SQLException ex) {
                Logger.getLogger(TravelAdvisor2018Fall.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        //make a database connection and get login values

    }

    /**
     * This method is for registering user in tho the system
     */
    public void Register() {
        System.out.println("Please enter your first name");
        String fName = in.next();

        System.out.println("Please enter your last name");
        String lName = in.next();

        System.out.println("Please enter your login Id");
        String loginId = in.next();

        if (!Validations.loginIdValidations(loginId)) {
            System.out.println("\nerror****    loginid requirements are not met    ****error\nYour loginid must between 3 to 10 charecters must contain Atleast 1 letter and atleast 1 digit\n\n please choose valid loginid\n");
            Register();
        }

        System.out.println("Please enter your password");
        String password = in.next();

        if (!Validations.PasswordValidations(loginId, password)) {
            System.out.println("loginid and password are same\nplease choose another password");
            Register();
        }

        System.out.println("please enter a numbers to select the tags from the following\n "
                + "1.History Buff\n 2.Shopping Fanatic\n 3.Beach Goer\n"
                + " 4.Urban Explorer\n 5.Nature Lover\n 6.Family Vacationer \n0 to done with tags");
        

            List<Integer> tags = new ArrayList<>();
            for (int i = 0; i < 7; i++) {
                int tag = in.nextInt();
                if (tag == 0) {
                    break;
                } else if (tag > 0 && tag < 7) {
                    tags.add(tag);
                } else {
                    System.out.println("you have entered invalid tag number.try again");
                    Register();
                }
            }

            //For testing
            for (int t : tags) {
                System.out.println("selected tag: " + t);

            }

            //Make a database connection
            Connection con = dbconnection.getDBConnection();

            try {
                //create statement
                Statement st = con.createStatement();
                //execute database query
                String sqlRQuery = "insert into login values('" + fName + "','" + lName + "','" + loginId + "', '" + password + "')";

                String sqlTagQuery = "";
                int tagRes = 0;
                int res = st.executeUpdate(sqlRQuery);

                if (res == 1) {
                    
                    for (int t : tags) {
                        System.out.println("selected tag: " + t);
                        sqlTagQuery = "insert into user_tags values('" + loginId + "','" + t + "')";
                        tagRes = st.executeUpdate(sqlTagQuery);
                    }

                    if (tagRes == 1) {
                        System.out.println("your account has been created successfully");
                        Login();
                    } else {
                        System.out.println("Failed to update Tags");
                    }

                } else {
                    System.out.println("your regestration failed please try again");
                }
            

        } catch (SQLException ex) {
            String errorMsg = "Error while registering: " ;
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, errorMsg + ex.getMessage());
            Register();
        }catch (InputMismatchException  e) {
                System.out.println("error*****you have entered wrong input. try again*****error");
                Register();
            }
            
    }

}
