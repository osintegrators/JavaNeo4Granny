package com.osintegrators.example;

import java.util.UUID;


public class Address {


	private String uuid;
	private String name;
	private String address;
	private String phone;
	private String email;
	private String dob;
	private String last_present;	
	

	public Address(String name, String address, String phoneNumber,
			String email, String dob, String lastPresent) {
		
		this.name = name;
		this.address = address;
		this.phone = phoneNumber;
		this.email = email;
		this.dob = dob;
		this.last_present = lastPresent;
	}

	public void copyFrom( final Address other )
	{
		this.name = other.name;
		this.address = other.address;
		this.phone = other.phone;
		this.email = other.email;
		this.dob = other.dob;
		this.last_present = other.last_present;
	}
	
	public Address() {
		this.uuid = UUID.randomUUID().toString();
	}

	public Address( String uuid )
	{
		this.uuid = uuid;
	}
	
	public String toString()
	{
		return "uuid: " + uuid + ", name: " + name + ", email: " + email + ", phone: " + phone + ", address: " + address + ", dob: " + dob + ", last_present: " + last_present;
	}
	
	
	public String getUuid() {
		return uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phoneNumber) {
		this.phone = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getLast_present() {
		return last_present;
	}

	public void setLast_present(String lastPresent) {
		this.last_present = lastPresent;
	}
	
}
