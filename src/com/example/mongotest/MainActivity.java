package com.example.mongotest;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Build;
import java.net.UnknownHostException;

import javax.xml.datatype.Duration;

import com.mongodb.*;
  
public class MainActivity extends Activity
{
	static EditText username;
	static EditText password;
	Button signup;
	Button login;
	static boolean sign=false;
	public void logHimIn()
	{
		Intent myIntent = new Intent(MainActivity.this, LogdoneActivity.class);
		MainActivity.this.startActivity(myIntent);
	}
	public static void putInDb(Context now) throws UnknownHostException
	{
	        
	        // Create seed data
	        
	        final BasicDBObject[] seedData = createSeedData();
	        
	        // Standard URI format: mongodb://[dbuser:dbpassword@]host:port/dbname
	       
	        MongoClientURI uri  = new MongoClientURI("mongodb://admin:blood1@ds035240.mongolab.com:35240/userdata"); 
	        MongoClient client = new MongoClient(uri);
	        DB db = client.getDB(uri.getDatabase());	   
	        DBCollection usercreds = db.getCollection("usercreds");        
	        usercreds.insert(seedData);
	 }
	public static int checkInDb(Context now, String usern) throws UnknownHostException
	{
		        // Standard URI format: mongodb://[dbuser:dbpassword@]host:port/dbname
		       int exist=0;
		    MongoClientURI uri  = new MongoClientURI("mongodb://admin:blood1@ds035240.mongolab.com:35240/userdata"); 
		    MongoClient client = new MongoClient(uri);
		    DB db = client.getDB(uri.getDatabase());	   
		    DBCollection usercreds = db.getCollection("usercreds");        
	        DBCursor docs = usercreds.find();
	        while(docs.hasNext())
	        {
	            DBObject doc = docs.next();
	            String u=doc.get("username")+"";
	            if(u.equals(usern))
	            exist=1;
	        }
	        return exist;
	            
	}
	public static void putInDb2(Context now,String usern, String passw) throws UnknownHostException
	{
		        // Standard URI format: mongodb://[dbuser:dbpassword@]host:port/dbname
		       
		    MongoClientURI uri  = new MongoClientURI("mongodb://admin:blood1@ds035240.mongolab.com:35240/userdata"); 
		    MongoClient client = new MongoClient(uri);
		    DB db = client.getDB(uri.getDatabase());	   
		    DBCollection usercreds = db.getCollection("usercreds");        
	        DBCursor docs = usercreds.find();
	        String usr="test1";
            String pas="test2";
	        while(docs.hasNext())
	        {
	            DBObject doc = docs.next();
	            String u=doc.get("username")+"";
	            String p=doc.get("password")+"";	           
	            if(u.equals(usern))
	            {	  
	            	if(p.equals(passw))
	            	{	            		
	            		usr=usern;
	            		pas=passw;
	            		sign=true;   	
	            	}
	            	else
	            	{
	            		Toast message=Toast.makeText(now, "Wrong username/password combination1", Toast.LENGTH_LONG);
						message.show();
						return;
	            	}
	            }
	        }
	            if(usr=="test1")
	            {
	            	Toast message=Toast.makeText(now, "Wrong username/password combination2", Toast.LENGTH_LONG);
					message.show();
					return;
	            }
	        client.close();
	 }
	public static BasicDBObject[] createSeedData()
    {
        
        BasicDBObject user = new BasicDBObject();
        String usern=username.getText().toString();
        String passw=password.getText().toString();
        user.put("username", usern);
        user.put("password", passw);
        final BasicDBObject[] seedData = {user};
        
        return seedData;
    }
    

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 
		
		final Context now=getApplicationContext();
		setContentView(R.layout.main1);
		username=(EditText)findViewById(R.id.uname);
		password=(EditText)findViewById(R.id.pass);
		login=(Button)findViewById(R.id.login);
		signup=(Button)findViewById(R.id.signup);
		login.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				try
				{
					Log.wtf("DB Connection","Putting in db");
					String usern=username.getText().toString();
			        String passw=password.getText().toString();
			        if(usern.length()==0 || passw.length()==0)
			        {
			        	Toast message2=Toast.makeText(now, "Please enter valid values!", Toast.LENGTH_LONG);
						message2.show();
			        	return;
			        }
					Log.wtf("DB Connection","Putting in db");
					putInDb2(now,usern,passw);					
					Log.wtf("DB Connection","Done");
					if(sign==true)
					{
						Toast message=Toast.makeText(now, "Logging you in", Toast.LENGTH_LONG);
						message.show();
						logHimIn();
					}
				} catch (UnknownHostException e)
				{
					// TODO Auto-generated catch block
					Log.e("Unknown host", "issue connecting to db");
					e.printStackTrace();
				}
			}
		});
		signup.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				try
				{
					Log.wtf("DB Connection","Putting in db");
					String usern=username.getText().toString();
			        String passw=password.getText().toString();
			        if(usern.length()==0 || passw.length()==0)
			        {
			        	Toast message2=Toast.makeText(now, "Please enter valid values!", Toast.LENGTH_LONG);
						message2.show();
			        	return;
			        }
			        if(checkInDb(now,usern)==1)
			        {
			        	Toast message=Toast.makeText(now,"Username taken, please choose another username",Toast.LENGTH_SHORT);
			        	message.show();
			        	return;
			        }
			        Toast message=Toast.makeText(now, "Signing you up..Just a moment", Toast.LENGTH_LONG);
					message.show();
					putInDb(now);
					Log.wtf("DB Connection","Done");
					logHimIn();
				} catch (UnknownHostException e)
				{
					// TODO Auto-generated catch block
					Log.e("Unknown host", "issue connecting to db");
					e.printStackTrace();
				}
			}
		});
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings)
		{
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


}
