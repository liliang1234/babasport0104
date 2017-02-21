package cn.itcast;

public class JavaBean {

	private String name;
	private String age;
	private Integer num;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	@Override
	public String toString() {
		return "JavaBean [name=" + name + ", age=" + age + ", num=" + num + "]";
	}
	
}
