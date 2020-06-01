package org.tain.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("storage")
public class StorageProperties {

	//rivate String location = "upload-dir"; // Users/kangmac/STS_workspace/KieaDemo-3/upload-dir
	private String location = "/Users/kangmac/upload-dir";
	
	public String getLocation() {
		return this.location;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
}