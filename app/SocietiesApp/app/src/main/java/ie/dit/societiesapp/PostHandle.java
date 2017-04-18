package ie.dit.societiesapp;

import org.json.JSONException;
import org.json.JSONObject;

public class PostHandle
{
    private char type;
    private String member_id = "";
    private int code;
    private String message = "";
    private String session_id = "";

    PostHandle()
    {
    }//end constuctor

    //Constructor accepts "Login" or "Register"
    PostHandle(char type)
    {
        this.type = type;
    }//end constuctor

    public void objParse(JSONObject jObject) throws JSONException
    {
        code =  jObject.getInt("return_code");

        if (code == 0)
        {
            if (type == 'L')
            {
                message = jObject.getString("return_msg");
                session_id = jObject.getString("session_id");
                member_id = jObject.getString("member_id");
            }//end if
            else if (type == 'R')
            {
                message = jObject.getString("return_msg");
            }//end else
        }//end if
        else
        {
            message = jObject.getString("return_msg");
        }//end else
    }//end METHOD objParse

    public String getMessage()
    {
        return message;
    }//end GETTER

    public String getMemberId()
    {
        return member_id;
    }//end GETTER

    public String getSessionId()
    {
        return session_id;
    }//end GETTER
}
