package com.backend.sevenX.utills;

public class Constant {

    public static final int EXPIRATION_TIME_JWT = 3; //in months

    public interface Response {
        String STATUS = "status";
        String MESSAGE = "message";
        String DATA = "data";
    }

    public interface Messages {
        String SUCCESS = "SUCCESS";
        String ERROR = "ERROR";
        String SOMETHING_WENT_WRONG = "Something Went Wrong";
        String ENTER_REQUIRED_PARAMETERS = "Enter Required Parameters";
        String INVALID_TOKEN = "Your Session is expired. Please login again.";
        String PLEASE_LOGIN_AGAIN = "Please Login Again";
        String ENTER_REGISTERED_EMAIL = "Please Enter Registered Email";
        String PASSWORD_IS_WRONG = "Password is Wrong";
        String USER_NOT_FOUND = "User Not Found";
        String UNAUTHORISED = "You are not authorised to access. Please Login again.";
        String TRY_OTHER_USERNAME = "Users with username already exist. Try other username";
    }

    public interface EndPoints {
        String REST = "/api";
        String LOGIN = "/login";
        String SIGNUP = "/sign-up";
        String AdminSignUp = "/admin/sign-up";
        String LOGOUT = "/log-out";
        String PUBLIC = "/public";
        String ALLFAQ ="/faq";
        String ADDFAQ ="/faq/add";
        String FAQ ="/faq/{id}";
    }

    public interface ImageFolders{
        String Images = "Images";
    }

    public interface JwtConst{
       String JWT_PRIVATE_KEY = "sevenXstartUpJWT";
       String KEY_AUTHORIZATION = "Authorization";
       Integer WORKLOAD = 12;
    }

    public interface DbField{
        String DELETED_AT = "deleted_at";
    }
    public interface EntityField{
        String DELETED_AT = "deletedAt";
    }
}


