package com.paymentonboard.dto;

import java.util.Map;

public class Mail {

    private String from;
    private String to;
    private String cC1;
    private String cC2;
    private String cC3;
    private String cc;
    

    public String getCc() {
		return cc;
	}

	public void setCc(String cc) {
		this.cc = cc;
	}

	private String subject;
    private String content;
    private Map Model;
    

    public Mail() {
    }

    public Mail(String from, String to, String subject, String content) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.content = content;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

   
	public Map getModel() {
		return Model;
	}

	public void setModel(Map model) {
		Model = model;
	}

	@Override
	public String toString() {
		return "Mail [from=" + from + ", to=" + to + ", subject=" + subject + ", content=" + content + ", Model="
				+ Model + "]";
	}

	public String getcC1() {
		return cC1;
	}

	public void setcC1(String cC1) {
		this.cC1 = cC1;
	}

	public String getcC2() {
		return cC2;
	}

	public void setcC2(String cC2) {
		this.cC2 = cC2;
	}

	public String getcC3() {
		return cC3;
	}

	public void setcC3(String cC3) {
		this.cC3 = cC3;
	}


}