package callproxy.dashboard;

import android.provider.BaseColumns;

public interface Constants extends BaseColumns {
	
	public static final int DATABASE_VERSION = 1;
	
//	public static final String DATABASE_NAME_BCM = "bCalmsg.db";
	public static final String TABLE_NAME_MSG = "bmsg";
	// Columns in the database
	public static final String NAME = "name";
	public static final String PHONENO = "phno";
	public static final String BODY = "body";
	public static final String MDATETIME = "mdatetime";

	public static final String DATABASE_NAME_ML = "mlist.db";
	public static final String TABLE_NAME_B = "black";
	// Columns in the database
	public static final String BNAME = "name";
	public static final String BPH = "phone";
	
	public static final String TABLE_NAME_W = "white";
	public static final String WNAME = "name";
	public static final String WPH = "phone";
	
//	public static final String DATABASE_NAME_BCL = "blog.db";
	public static final String TABLE_NAME_BCL = "blog";
	// Columns in the database
	public static final String BLNAME = "name";
	public static final String BLPH = "phone";
	public static final String BLDTIME = "dtime";
	
	public static final String DATABASE_NAME_PS = "privatespace.db";
	public static final String TABLE_NAME_PC = "pcontacts";
	// Columns in the database
	public static final String PNAME = "name";
	public static final String PPH = "phone";
	
	public static final String TABLE_NAME_PCL = "plog";
	public static final String TABLE_NAME_PM = "pmsg";
	public static final String TABLE_NAME_PSM = "psmsg";
	// Columns in the database
	public static final String PBODY = "body";
	public static final String PDTIME = "dtime";
	
	
	
	//Timebased Profile
	public static final String DATABASE_NAME_TBP = "tbp.db";
	public static final String TABLE_NAME_TBP = "tbprofiles";
	// Columns in the database
	public static final String PRNAME = "prname";
	public static final String PSTIME = "pstime";
	public static final String PETIME = "petime";
	public static final String PLIST = "plist";
	public static final String PDAYS = "pdays";
	public static final String STATE = "state";

public static final String DATABASE_NAME_LG = "login.db";
	public static final String TABLE_NAME_LG = "login_data";
	// Columns in the database
	
	public static final String passwd = "password";
	public static final String lid = "login_id";
	
}