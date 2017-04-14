package ie.dit.societiesapp;

import org.json.JSONObject;

public class PostHandle
{
    private String type;
    private String member_id;
    private String code;
    private String message;
    private String session_id;

    PostHandle()
    {
    }//end constuctor

    //Constructor accepts "Login" or "Register"
    PostHandle(String type)
    {
        this.type = type;
    }//end constuctor

    public void objParse(JSONObject jObject)
    {

    }//end METHOD objParse
}
