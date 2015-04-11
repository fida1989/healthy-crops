package com.mobioapp.healthycrops.model;

public class PestModel {
	
	private String pestId;
	private String pestName;
	private String pestImage;
    private String pestDescription;
	
	public PestModel() {
		
	}
	
	public PestModel(String pestId, String pestName, String pestImage, String pestDescription) {
		this.pestId = pestId;
		this.pestName = pestName;
		this.pestImage = pestImage;
        this.pestDescription = pestDescription;
	}

	public String getPestId() {
		return pestId;
	}

	public void setPestId(String pestId) {
		this.pestId = pestId;
	}

	public String getPestName() {
		return pestName;
	}

	public void setPestName(String pestName) {
		this.pestName = pestName;
	}

	public String getPestImage() {
		return pestImage;
	}

	public void setPestImage(String pestImage) {
		this.pestImage = pestImage;
	}

    public String getPestDescription() {
        return pestDescription;
    }

    public void setPestDescription(String pestDescription) {
        this.pestDescription = pestDescription;
    }

	
	
    @Override
    public String toString() {
    	
    	return "Pest No:"+ getPestId()+" pestName:" + getPestName() + " pestImage:" + getPestImage() + " pestDescription:" + getPestDescription();
    }

}
