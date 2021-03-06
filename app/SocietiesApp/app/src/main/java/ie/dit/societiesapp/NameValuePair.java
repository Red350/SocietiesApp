package ie.dit.societiesapp;

public class NameValuePair
{
	private String name;
	private String value;
	
	public NameValuePair(String name, String value)
	{
		this.name = name;
		this.value = value;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getValue()
	{
		return value;
	}

	public String toString() {
		return getName() + ": " + getValue();
	}
}
