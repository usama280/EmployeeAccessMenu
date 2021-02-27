public class WorkOrder {
	private String workNum;
	private String jobCode;
	private String compCode;
	
	public WorkOrder(String workNum, String jobCode, String compCode) {
		this.workNum = workNum;
		this.jobCode = jobCode;
		this.compCode = compCode;
	}

	
	public String getWorkNum() {
		return workNum;
	}


	public String getJobCode() {
		return jobCode;
	}


	public String getCompCode() {
		return compCode;
	}

	@Override
	public String toString() {
		return "WorkOrder [workNum=" + workNum + ", jobCode=" + jobCode + ", compCode=" + compCode + "]";
	}
	
	
}
