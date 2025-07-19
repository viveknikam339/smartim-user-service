package com.smartim.userservice.contants;

/**
 * All constants used in this service are defined here
 */
public class UserConstants {

    private UserConstants(){
        // restrict instantiation
    }

    public static final String STATUS_201="201";
    public static final String MESSAGE_201="Record has been created";
    public static final String STATUS_200="200";
    public static final String MESSAGE_200="Request processed successfully";
    public static final String PASSWORD_RESET_SUCCESSFULLY="Password Reset Successfully";
    public static final String RESET_PASSWORD="RESET";
    public static final String FORGOT_PASSWORD="FORGOT";
    public static final String USER_NOT_FOUND="User Not Found.";
    public static final String ENTERED_WRONG_PASSWORD="You have entered wrong old password.";
    public static final String STATUS_400="400";
    public static final String MESSAGE_400="Bad request, Please try again with valid request";
    public static final String STATUS_500="500";
    public static final String MESSAGE_500="An error occurred. Please try again or contact Dev team.";

}
