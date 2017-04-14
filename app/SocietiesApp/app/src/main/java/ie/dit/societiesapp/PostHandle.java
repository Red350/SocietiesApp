package ie.dit.societiesapp;

import org.json.JSONObject;

public class PostHandle
{
    String type;

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

    }
}
