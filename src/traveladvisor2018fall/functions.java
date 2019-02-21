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
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author SURESH
 */
public class functions {
    Home h=new Home();
    
    public void user_favCity(String city) {

        DBConnection dbconnection = new DBConnection();
        Connection con = dbconnection.getDBConnection();

        try {
            Statement st = con.createStatement();

            String user_favCityQuery = "Insert into user_favcity(loginid,city) values ('" + Account.loginId + "','" + city + "')";

            int res = st.executeUpdate(user_favCityQuery);
            System.out.println(city+" was added to your favourite city list\n");
           
        } catch (SQLException ex) {
            String errorMsg = "Error while updaitng your favorite city: " ;
            Logger.getLogger(functions.class.getName()).log(Level.SEVERE, errorMsg + ex.getMessage());
            user_favCity(city);
            
        }
    }

    public void user_favAtt(String attractionname) {

        DBConnection dbconnection = new DBConnection();
        Connection con = dbconnection.getDBConnection();

        try {
            Statement st = con.createStatement();

            String user_favAttQuery = "Insert into user_favAttraction (loginid,attraction) values ('" + Account.loginId + "','" + attractionname + "')";

            int res = st.executeUpdate(user_favAttQuery);
            System.out.println(attractionname+" was added to your favourite attractions list\n");
            
        } catch (SQLException ex) {
            String errorMsg = "Error while updaitng your favorite attraction: " ;
            Logger.getLogger(functions.class.getName()).log(Level.SEVERE, errorMsg + ex.getMessage());
            user_favCity(attractionname);
        }
    }

    public void attraction_questions(String attractionname) {
        Scanner input = new Scanner(System.in);

        System.out.println("ask question about this attraction");
        String question = input.nextLine();

        DBConnection dbconnection = new DBConnection();
        Connection con = dbconnection.getDBConnection();

        try {
            Statement st = con.createStatement();
            String att_QuesQuery = "Insert into attraction_questions (loginid,attractionname,question) values ('" + Account.loginId + "','" + attractionname + "', '" + question + "')";
            int res = st.executeUpdate(att_QuesQuery);
            System.out.println(" Your question was posted successfully \n");
        } catch (SQLException ex) {
            String errorMsg = "Error while updating question: " ;
            Logger.getLogger(functions.class.getName()).log(Level.SEVERE, errorMsg + ex.getMessage());
            attraction_questions(attractionname);
        }
    }

    public void attraction_answer(String attractionname) {

        Scanner input2 = new Scanner(System.in);
        try {
            DBConnection dbconnection = new DBConnection();
            Connection con = dbconnection.getDBConnection();
            Scanner input = new Scanner(System.in);

            System.out.println("answer a question about this attraction");
            Statement st = con.createStatement();
            String sqlQuery = "(select * from attraction_questions where attractionname ='" + attractionname + "')";
            ResultSet searchRes = st.executeQuery(sqlQuery);
            int i = 1;
            while (searchRes.next()) {
                String allQuestions = searchRes.getString("question");
                System.out.println("Enter " + i + " to answer : " + allQuestions);
                i++;
            }
            int index = input.nextInt();
            System.out.println("enter your answer");
            String allAnswers = input2.nextLine();
            if (index < i && index > 0) {
                searchRes.absolute(index);
                int qid = searchRes.getInt("qid");
                String sqlQuery2 = "insert into attraction_answers (qid,loginid,answer) values ('" + qid + "','" + Account.loginId + "','" + allAnswers + "')";
                int res = st.executeUpdate(sqlQuery2);
                System.out.println(" Your answer was posted successfully\n");
                 
            }

        } catch (SQLException ex) {
            String errorMsg = "Error while updating your answer: " ;
            Logger.getLogger(functions.class.getName()).log(Level.SEVERE, errorMsg + ex.getMessage());
            attraction_answer(attractionname);
        }
    }

    public void favDest() {
        Scanner input3 = new Scanner(System.in);
        System.out.println("enter\n1 To view your favourite attractions\n2 To view your favourite city");
        int sel = input3.nextInt();
        if (sel == 1) {
            DBConnection dbconnection = new DBConnection();
            Connection con = dbconnection.getDBConnection();
            try {
                Statement st = con.createStatement();
                String sqlQuery = "(select attraction from user_favattraction where loginid ='" + Account.loginId + "')";
                ResultSet searchRes = st.executeQuery(sqlQuery);
                int i = 1;
                String favDA = "";
                while (searchRes.next()) {
                    favDA = searchRes.getString("attraction");
                    System.out.println("Enter " + i + " to see the details of : " + favDA);
                    i++;
                }
                Home h=new Home();
                int index = input3.nextInt();
                 if (index < i && index > 0) {
                     
                searchRes.absolute(index);
                favDA = searchRes.getString("attraction");
                System.out.println(favDA);
                String sqlQuery2 = "select attractionname,description,city,state from attractions where attractionname='" + favDA + "' and approvedstatus='Y' ";
                     ResultSet res = st.executeQuery(sqlQuery2);
                     if(res.next()){
                        String attname = res.getString("attractionname");
                        String desc = res.getString("description");
                        String city = res.getString("city");
                        String state = res.getString("state");

                        System.out.println("======The attraction Details for the attraction "+attname+" are below======");
                        System.out.println("attractionname: " + attname + "\nadesctiption: " + desc + "\ncity: " + city + "\nState: " + state);
                        float score = h.attractionAverageScore(attname);
                        System.out.println("The average score of the attraction: " + score);
                        h.attractionReviews(attname);
                        user_functionsList(city, attname);
                     }
                         }
                 else{
                     System.out.println("Error*****You have entered wrong selection.try again*****error");
                     favDest();
                 }

            } catch (SQLException ex) {
                Logger.getLogger(functions.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if(sel==2){
             DBConnection dbconnection = new DBConnection();
            Connection con = dbconnection.getDBConnection();
            try {
                Statement st = con.createStatement();
                String sqlQuery = "(select city from user_favcity where loginid ='" + Account.loginId + "')";
                ResultSet searchRes = st.executeQuery(sqlQuery);
                int i = 1;
                String favDC = "";
                while (searchRes.next()) {
                    favDC = searchRes.getString("city");
                    System.out.println("Enter " + i + " to see the details of : " + favDC);
                    i++;
                }
                Home h=new Home();
                 int index = input3.nextInt();
                 if (index < i && index > 0) {
                searchRes.absolute(index);
                String sqlQuery2 = "select attractionname,description,city,state from attractions where city='" + favDC + "' and approvedstatus='Y' ";
                    ResultSet res = st.executeQuery(sqlQuery2);
                     while(res.next()){
                        String attname = res.getString("attractionname");
                        String desc = res.getString("description");
                        String city = res.getString("city");
                        String state = res.getString("state");
                        
                        System.out.println("======The details of attraction "+attname+ "in city are below======");
                        System.out.println("attractionname: " + attname + "\nadesctiption: " + desc + "\ncity: " + city + "\nState: " + state);
                         float score = h.attractionAverageScore(attname);
                        System.out.println("The average score of the attraction: " + score);
                        h.attractionReviews(attname);
                        user_functionsList(city, attname);
                     }
                }else{
                     System.out.println("You have made an invalid selection.Try again");
                     favDest();
                 }
                     
                
            }catch (SQLException ex) {
                Logger.getLogger(functions.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else{
            System.out.println("Error*****You have entered an invalid number.try again*****error");
            favDest();
        }

    }
    public void user_functionsList(String city,String attname){
        Scanner input=new Scanner(System.in);
        Home home = new Home();
        System.out.println("Enter\n1 To select "+ city +" as your favourite city\n2 To select " + attname + " as your favourite attraction"
                            + "\n3.To ask a question\n4.To answer a question\n5 To post a score\n6 To post a review\n7 To return to main menu");
                    
        int sel = input.nextInt();
        if (sel == 1) {
            user_favCity(city);
            user_functionsList(city,attname);
        } else if (sel == 2) {
            user_favAtt(attname);
            user_functionsList(city,attname);
        } else if (sel == 3) {
            attraction_questions(attname);
            user_functionsList(city, attname);
        } else if (sel == 4) {
            attraction_answer(attname);
            user_functionsList(city,attname);
        } else if(sel == 5) {
            home.postAttractionScore(attname);
            user_functionsList(city,attname);
        } else if( sel == 6) {
            home.postReviewToAttraction(attname);
            user_functionsList(city,attname);
        }else if(sel==7){
            home.userHome();
        }
        else {
            System.out.println("Invalid input, please inter the valid input");
            user_functionsList(city, attname);
        }
        
        
    }

}
