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
public class Home {

    Scanner input = new Scanner(System.in);
    DBConnection dbconnection = new DBConnection();

    public void userHome() {
        System.out.println("****Welcome to Travel Advisor page****\n");

        userNotifications();

        System.out.println("\n======================================================");
        System.out.println("Attraction you may like are below, enter 2 view them");
        String userTags = getProfileTags();
        showYouMayLikeAtt(userTags);
        System.out.println("======================================================\n");

        
        System.out.println("==========Main menu==========\nEnter\n 1.To add an attraction\n 2.To view the attractions you may like \n"
                + " 3.To search for attractions\n"
                + " 4.To view your favourite destinations\n 5.To read unread notifications");

        try {
            int option = input.nextInt();
            if (option == 1) {
                addAttraction();
            } else if (option == 2) {
                searchAttByTags(userTags, true);
            } else if (option == 3) {
                searchAttraction();
            } else if (option == 4) {
                functions f = new functions();
                f.favDest();
            } else if (option == 5) {
                readNotifications();
                userHome();
            } else {
                System.out.println("Wrong entry");
                userHome();
            }
        } catch (InputMismatchException e) {
            System.out.println("*****Please enter valid input*****");
            userHome();
        }

    }

    //admin home page
    public void adminHome() {
        System.out.println("enter 1 to change the status for the attraction");
        int num = input.nextInt();
        if (num == 1) {
            Connection con = dbconnection.getDBConnection();
            try {
                Statement st = con.createStatement();
                String statusQuery = "select * from attractions where approvedstatus = 'N'";
                ResultSet searchRes = st.executeQuery(statusQuery);

                int i = 1;
                while (searchRes.next()) {
                    String attName = searchRes.getString("attractionname");
                    System.out.println("Enter " + i + " to update the status of the attraction : " + attName);
                    i++;
                }
                int index = input.nextInt();
                if (index < i && index > 0) {
                    searchRes.absolute(index);
                    String attName = searchRes.getString("attractionname");
                    System.out.println("Enter\n1.To approve the attraction" + attName + "\n2.To reject the attraction");
                    int sel = input.nextInt();
                    if (sel == 1) {
                        String sqlQuery2 = "update attractions set approvedstatus = 'Y' where attractionname = '" + attName + "'";
                        //UPDATE attractions SET approvedstatus ='Y' where attractionname='uhcl'
                        int res = st.executeUpdate(sqlQuery2);

                        if (res == 1) {
                            System.out.println("Attraction status updated sucessfully");
                        } else {
                            System.out.println("Atraction status update failed");
                        }

                    } else if (sel == 2) {
                        String sqlQuery2 = "update attractions set approvedstatus = 'R' where attractionname = '" + attName + "'";
                        //UPDATE attractions SET approvedstatus ='Y' where attractionname='uhcl'
                        int res = st.executeUpdate(sqlQuery2);

                        if (res == 1) {
                            System.out.println("Attraction status updated sucessfully");
                        } else {
                            System.out.println("Atraction status update failed");
                        }
                    } else {
                        System.out.println("you have entered wrong selection");
                    }

                }
            } catch (SQLException ex) {
                Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void userNotifications() {
        try {
            Connection con = dbconnection.getDBConnection();

            Statement st = con.createStatement();
            String notificationsQuery = "SELECT * FROM `attraction_answers` a join attraction_questions q on a.qid = q.qid where a.readstatus = 'N' and q.loginid = '" + Account.loginId + "'";
            ResultSet rSet = st.executeQuery(notificationsQuery);
            int rowcount = 0;
            if (rSet.last()) {
                rowcount = rSet.getRow();
                rSet.beforeFirst();
            }

            System.out.println("You have " + rowcount + " unread notifications.Enter 5 to read them");

        } catch (SQLException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void readNotifications() {
        try {
            Connection con = dbconnection.getDBConnection();

            Statement st = con.createStatement();
            String notificationsQuery = "SELECT * FROM `attraction_answers` a join attraction_questions q on a.qid = q.qid where a.readstatus = 'N' and q.loginid = '" + Account.loginId + "'";
            ResultSet rSet = st.executeQuery(notificationsQuery);
            int i = 1;

            System.out.println("Below are the unread answers ");
             
            String prvQuestion = null;
            while (rSet.next()) {
                String question = rSet.getString("question");

                if (!question.equals(prvQuestion)) {
                    System.out.println("***The answers for the question : " + question + "? are below***");
                }

                String answer = rSet.getString("answer");
                System.out.println(i + "." + answer);
                prvQuestion = question;
                i++;
            }

            String nUpdateQuery = "";
            nUpdateQuery = "update attraction_answers a join attraction_questions q on a.qid = q.qid  set "
                    + "a.readstatus= 'Y' where q.loginid = '" + Account.loginId + "'";

            int uResultSet = st.executeUpdate(nUpdateQuery);

        } catch (SQLException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addAttraction() {
        Scanner in = new Scanner(System.in);

        System.out.println("Enter attraction name");
        String attractionName = in.nextLine();

        System.out.println("Enter description");
        String attractionDesc = in.nextLine();

        System.out.println("Enter attraction city");
        String attractionCity = in.nextLine();

        System.out.println("Enter attraction State");
        String attractionState = in.nextLine();

        System.out.println("please enter a numbers to select the tags from the following\n "
                + "1.History Buff\n 2.Shopping Fanatic\n 3.Beach Goer\n"
                + " 4.Urban Explorer\n 5.Nature Lover\n 6.Family Vacationer ");
        try {

            int tag = 0;
            List<Integer> tags = new ArrayList<>();
            for (int i = 0; i < 7; i++) {
                tag = in.nextInt();
                if (tag == 0) {
                    break;
                } else if (tag > 0 && tag < 7) {
                    tags.add(tag);
                } else {
                    System.out.println("you have entered invalid tag number.try again");
                    addAttraction();
                }
            }
            for (int t : tags) {
                System.out.println("selected tag: " + t);

            }

            
            Connection con = dbconnection.getDBConnection();
            try {
                //create statement
                Statement st = con.createStatement();

                //execute database query
                String addAttQuery = "Insert into attractions values ('" + attractionName + "','" + attractionDesc + "','" + attractionCity + "','" + attractionState + "','N')";

                int res = st.executeUpdate(addAttQuery);

                if (res == 1) {
                    System.out.println("The attraction " + attractionName + " added successfully");
                } else {
                    System.out.println("its failed to add attraction " + attractionName);
                }

                //add in attraction tags
                String sqlA_TQuery = "insert into attraction_tags values('" + attractionName + "','" + tag + "')";
                int tagRes = st.executeUpdate(sqlA_TQuery);
                if (tagRes == 1) {
                    System.out.println("your values are also added in attraction_tags");

                } else {
                    System.out.println("Failed to update values");
                }

            } catch (InputMismatchException e) {
                System.out.println("Error*****tag input you have entered is wrong.Enter again*****error");
                addAttraction();
            }

        } catch (SQLException ex) {
            String errorMsg = "Error while adding attraction: " ;
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, errorMsg + ex.getMessage());
            addAttraction();

        }

    }

    public void searchAttraction() {
        Scanner input1 = new Scanner(System.in);
        Scanner input2 = new Scanner(System.in);
        Scanner inputLine = new Scanner(System.in);
        Connection con = dbconnection.getDBConnection();
        String search = "";

        System.out.println("Search for an attraction based on tag or city\n 1.search by tag\n 2.search by city\n 3.To return to main menu ");
        try {
            int option = input1.nextInt();

            if (option == 1) {
                System.out.println("==========All tags present in the system are below==========\n"
                        + "History Buff, Shopping Fanatic, Beach Goer, Urban Explorer, Nature Lover, Family Vacationer");

                System.out.println("Please enter the tag");
                search = inputLine.nextLine();
                searchAttByTags(search, false);

            } else if (option == 2) {
                System.out.println("Please enter city name");
                search = inputLine.nextLine();

                try {
                    //create statement
                    Statement st = con.createStatement();
                    
                    String sqlQuery = "(select * from attractions \n"
                            + "	where city='" + search + "')";
                    ResultSet searchRes = st.executeQuery(sqlQuery);
                    int i = 1;

                    System.out.println("The attaction in the city " + search + " are below: ");
                    while (searchRes.next()) {
                        String attractionName = searchRes.getString("attractionname");
                        System.out.println("Enter " + i + " to view attraction: " + attractionName);
                        i++;
                    }
                    int index = input2.nextInt();

                    if (index < i && index > 0) {
                        searchRes.absolute(index);
                        String attname = searchRes.getString("attractionname");
                        String desc = searchRes.getString("description");
                        String city = searchRes.getString("city");
                        String state = searchRes.getString("state");

                        System.out.println("======The attraction Details are below======");
                        System.out.println("attractionname: " + attname + "\nadesctiption: " + desc + "\ncity: " + city + "\nState: " + state);

                        //Display average score;
                        float score = attractionAverageScore(attname);
                        System.out.println("The average score of the attraction: " + score);

                        //Display all of the reviews of this attraction
                        attractionReviews(attname);

                        
                        functions f = new functions();
                        f.user_functionsList(city, attname);

                    } else {
                        System.out.println("There are no attractions for the number you entered");
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(TravelAdvisor2018Fall.class.getName()).log(Level.SEVERE, null, ex);
                }
            }else if(option==3){
                userHome();
            }
            else{
                System.out.println("Error*****You have entered an invalid selection.Try again*****error");
            }

        } catch (InputMismatchException e) {
            System.out.println("error*****Your input should be a valid number.try again*****error\n");
            searchAttraction();

        }

    }

    public void searchAttByTags(String search, boolean youMayLike) {
        Scanner input2 = new Scanner(System.in);

        Connection con = dbconnection.getDBConnection();
        int resultsToShow = 0;
        try {

            if (youMayLike) {
                resultsToShow = 3;
            } else {
                resultsToShow = 99999;
                search = "'" + search + "'";
            }
            //create statement
            Statement st = con.createStatement();

           
            String sqlQuery = "SELECT a.attractionname,a.description,a.city,a.state , avg(s.score) as avgscore FROM attractions a join attraction_scores s "
                    + "on a.attractionname=s.attractionname where a.approvedstatus = 'Y' and a.attractionname in (select attractionname "
                    + "from attraction_tags where tagid in (select tagid from tags where tagname in (" + search + " ) )) group by a.attractionname order by avgscore desc";

            ResultSet searchRes = st.executeQuery(sqlQuery);

            int i = 1;
            while (searchRes.next()) {
                if (i <= resultsToShow) {
                    String attName = searchRes.getString("attractionname");
                    System.out.println("Enter " + i + " to view attraction: " + attName);
                }
                i++;
            }

            if (i == 1) {
                System.out.println("There are no attractions found on the basis of tag: " + search);
                searchAttraction();
            }

            //
            try {

                int index = input2.nextInt();

                if (index < i && index > 0) {
                    searchRes.absolute(index);
                    String attname = searchRes.getString("attractionname");
                    String desc = searchRes.getString("description");
                    String city = searchRes.getString("city");
                    String state = searchRes.getString("state");

                    System.out.println("======The attraction Details are below======");
                    System.out.println("attractionname: " + attname + "\nadesctiption: " + desc + "\ncity: " + city + "\nState: " + state);

                    //Display average score;
                    float score = attractionAverageScore(attname);
                    System.out.println("The average score of the attraction: " + score);

                    //Display all of the reviews of this attraction
                    attractionReviews(attname);

                    //other attraction operations
                    functions f = new functions();
                    f.user_functionsList(city, attname);
                } else {
                    System.out.println("There are no attractions for the number you entered");
                    searchAttByTags( search,youMayLike);

                }
            } catch (InputMismatchException e) {
                System.out.println("error*****Your input should be a valid number.try again*****error\n");
                searchAttraction();
            }
        } catch (SQLException ex) {
            Logger.getLogger(TravelAdvisor2018Fall.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void showYouMayLikeAtt(String userTags) {
        try {
            Connection con = dbconnection.getDBConnection();

            //create statement
            Statement st = con.createStatement();

            
            String sqlQuery = "SELECT a.attractionname,a.description,a.city,a.state , avg(s.score) as avgscore FROM attractions a join attraction_scores s "
                    + "on a.attractionname=s.attractionname where a.approvedstatus = 'Y' and a.attractionname in (select attractionname "
                    + "from attraction_tags where tagid in (select tagid from tags where tagname in (" + userTags + " ) )) group by a.attractionname order by avgscore desc";

            ResultSet searchRes = st.executeQuery(sqlQuery);

            int i = 1;
            while (searchRes.next()) {
                if (i <= 3) {
                    String attName = searchRes.getString("attractionname");
                    System.out.println("**" + attName);
                }
                i++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getProfileTags() {
        Connection con = dbconnection.getDBConnection();
        String profileTags = "";
        try {
            Statement st = con.createStatement();
            String ptagsQuery = "select u.tagid, u.loginid, t.tagname from tags t join user_tags u on t.tagid = u.tagid where loginid='" + Account.loginId + "'";

            ResultSet result1 = st.executeQuery(ptagsQuery);

            while (result1.next()) {
                profileTags += "'" + result1.getString("tagname") + "',";
            }
            int lastIndex = profileTags.lastIndexOf(",");
            profileTags = profileTags.substring(0, lastIndex);

        } catch (SQLException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }

        return profileTags;
    }

    public void postAttractionScore(String attractionName) {
        Scanner in = new Scanner(System.in);
        int as_attractionScore = 0;
        Connection con = dbconnection.getDBConnection();
        
        try {
            Statement st = con.createStatement();
            System.out.println("Enter score from 1 to 5 for the attraction");
            
            try {
                
                as_attractionScore = in.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input, please enter the valid input value");
                
            }
            
            if(as_attractionScore <= 0 || as_attractionScore > 5) {
                System.out.println("You entered invalid score, please enter valid score");
                postAttractionScore(attractionName);
            }

            String sqlQuery2 = "Insert into attraction_scores values (  '" + Account.loginId + "','"
                    + attractionName + "' , '" + as_attractionScore + "')";
            int result2 = st.executeUpdate(sqlQuery2);
            if (result2 == 1) {
                System.out.println("The attraction score " + as_attractionScore + " added successfully\n");
            } else {
                System.out.println("its failed to add attraction score \n");
            }
        } catch (SQLException ex) {
            String errorMsg = "Error while updating the attraction score: " ;
            Logger.getLogger(TravelAdvisor2018Fall.class.getName()).log(Level.SEVERE, errorMsg + ex.getMessage());
            searchAttraction();
        }
    }

    public void postReviewToAttraction(String attractionName) {
        try {
            Scanner in = new Scanner(System.in);
            
            System.out.println("enter review this attraction");
            String review = in.nextLine();

            Connection con = dbconnection.getDBConnection();

            Statement st = con.createStatement();

            String reviewQuey = "Insert into reviews(loginid,attractionname,review) values ('" + Account.loginId + "' , '" + attractionName + "','" + review + "')";

            int res = st.executeUpdate(reviewQuey);

            if (res == 1) {
                System.out.println("Review has been posted to this attraction successfully\n");
            } else {
                System.out.println("Failed to post review\n");
            }

        } catch (SQLException ex) {
            String errorMsg = "Error while updating the attraction review: " ;
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, errorMsg + ex.getMessage());
            postReviewToAttraction(attractionName);
            
        }

    }

    public float attractionAverageScore(String attractionName) {

        float avgScore = 0;
        try {
            Connection con = dbconnection.getDBConnection();
            Statement st = con.createStatement();
            String avgScoreQuery = "Select avg(score) as averageScore from attraction_scores where attractionname = '" + attractionName + "'";
            ResultSet resultSet = st.executeQuery(avgScoreQuery);

            if (resultSet.next()) {
                avgScore = resultSet.getFloat("averageScore");
                //round it to one digit after the decimal 
                avgScore = (float) (Math.round(avgScore * 10) / 10.0);
            } else {
                System.out.println("not scored yet");
            }

        } catch (SQLException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
        return avgScore;
    }

    public void attractionReviews(String attractionName) {

        try {
            Connection con = dbconnection.getDBConnection();
            Statement st = con.createStatement();

            String reviewsQuery = "select * from reviews where attractionname = '" + attractionName + "'";

            ResultSet resultSet = st.executeQuery(reviewsQuery);
            System.out.println("The review are: ");
            int i = 1;
            while (resultSet.next()) {
                System.out.println(i + ". " + resultSet.getString(4));
                i++;
            }

        } catch (SQLException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
