package callproxy.dashboard;

public class BData {

	String phone;
	String name;
	String mdatetime;
	
	public BData(String phone, String name) {
		// super();
		this.phone = phone;
		this.name = name;
	}

	public BData(String phone, String name, String mdatetime) {
		// super();
		this.phone = phone;
		this.name = name;
		this.mdatetime = mdatetime;
		// this.isMgr = isMgr;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getMdateTime() {
		return mdatetime;
	}

	public void setMdateTime(String mdatetime) {
		this.mdatetime = mdatetime;
	}

}
