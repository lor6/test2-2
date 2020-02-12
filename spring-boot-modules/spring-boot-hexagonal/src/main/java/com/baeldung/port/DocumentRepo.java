package com.baeldung.port;

import com.baeldung.domain.Document;

public interface DocumentRepo {
	void storeDocument(Document document);
	
	Document findDocumentById(String id);
}

