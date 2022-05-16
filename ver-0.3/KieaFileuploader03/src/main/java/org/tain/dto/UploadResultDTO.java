package org.tain.dto;

import java.io.Serializable;
import java.net.URLEncoder;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UploadResultDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String fileName;
	private String uuid;
	private String folderPath;
	
	public String getImageURL() {
		try {
			//return URLEncoder.encode(this.folderPath + "/" + this.uuid + "_" + this.fileName, "utf-8");
			return URLEncoder.encode(this.folderPath + "/" + "s_" + this.uuid + "_" +  this.fileName, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}
