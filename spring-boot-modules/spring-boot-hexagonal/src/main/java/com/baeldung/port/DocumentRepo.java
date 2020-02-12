package com.baeldung.port;


import com.ajay.documentservice.domain.Document;

public interface DocumentRepo {
	void storeDocument(Document document);
	
	Document findDocumentById(String id);
}

