package callproxy.dashboard;

public class Data {
	String name;
	String phno;
	String body;
	String dtime;
	//boolean isMgr;
	
	public Data(String name,String phno,String dtime) {
		//super();
		this.name = name;
		this.phno = phno;
		this.dtime = dtime;
		//this.isMgr = isMgr;
	}
	
	public Data(String name,String phno, String body, String dtime) {
		//super();
		this.name = name;
		this.phno = phno;
		this.body = body;
		this.dtime = dtime;
		//this.isMgr = isMgr;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getPhno() {
		return phno;
	}

	public void setPhno(String phno) {
		this.phno = phno;
	}

	public String getDtime() {
		return dtime;
	}

	public void setDtime(String dtime) {
		this.dtime = dtime;
	}
	
}


