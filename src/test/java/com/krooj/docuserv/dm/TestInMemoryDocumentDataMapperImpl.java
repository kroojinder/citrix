package com.krooj.docuserv.dm;

import java.io.InputStream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.krooj.docuserv.DocuservUnitTest;
import com.krooj.docuserv.domain.InMemoryDocument;
import com.krooj.docuserv.domain.TestInMemoryDocument;

/**
 * Test cases for the {@link com.krooj.docuserv.dm.InMemoryDocumentDataMapperImpl}
 *
 */
public class TestInMemoryDocumentDataMapperImpl extends DocuservUnitTest{

	private InMemoryDocumentDataMapperImpl inMemoryDocumentDataMapper;

	@Before
	public void setUp(){
		inMemoryDocumentDataMapper = new InMemoryDocumentDataMapperImpl();
	}

	@Test
	public void testCreateDocument_success() throws Exception {
		//Prepare
		InMemoryDocument document = TestInMemoryDocument.createDocument();
		InputStream inputStream = getClass().getResourceAsStream(TEST_DOCUMENT);

		//Expect

		//Replay

		//Execute
		inMemoryDocumentDataMapper.createDocument(document, inputStream);

		//Assert
		Assert.assertNotNull(inMemoryDocumentDataMapper.retrieveDocumentById(document.getId()));

		//Verify
	}



	@Test
	public void testCreateDocument_fail_nullDocument() throws Exception {
		//Prepare
		InMemoryDocument document = null;
		InputStream inputStream = getClass().getResourceAsStream(TEST_DOCUMENT);

		//Expect

		//Replay

		//Execute
		try{
			inMemoryDocumentDataMapper.createDocument(document, inputStream);
		}catch (DocumentDMException e){
			Assert.assertEquals("document may not be null", e.getMessage());
			Assert.assertNull(e.getCause());
		}

		//Assert

		//Verify
	}

	@Test
	public void testCreateDocument_fail_nullDocumentContent() throws Exception {
		//Prepare
		InMemoryDocument document = TestInMemoryDocument.createDocument();
		InputStream inputStream = null;

		//Expect

		//Replay

		//Execute
		try{
			inMemoryDocumentDataMapper.createDocument(document, inputStream);
		}catch (DocumentDMException e){
			Assert.assertEquals("documentInputStream may not be null", e.getMessage());
			Assert.assertNull(e.getCause());
		}

		//Assert

		//Verify
	}

	@Test
	public void testUpdateDocument_success() throws Exception {
		//Prepare
		InMemoryDocument document = TestInMemoryDocument.createDocument();
		InputStream inputStream = getClass().getResourceAsStream(TEST_DOCUMENT);
		inMemoryDocumentDataMapper.createDocument(document, inputStream);

		//Expect

		//Replay

		//Execute
		inMemoryDocumentDataMapper.updateDocument(document.getId(), inputStream);

		//Assert
		Assert.assertNotNull(inMemoryDocumentDataMapper.retrieveDocumentById(document.getId()));

		//Verify
	}

	@Test
	public void testUpdateDocument_fail_documentNotInStore() throws Exception {
		//Prepare
		InMemoryDocument document = TestInMemoryDocument.createDocument();
		InputStream inputStream = getClass().getResourceAsStream(TEST_DOCUMENT);

		//Expect

		//Replay

		//Execute
		try{
			inMemoryDocumentDataMapper.updateDocument(document.getId(), inputStream);
		}catch(DocumentDMException e){
			Assert.assertEquals("Document identified by documentId: "+document.getId()+" does not exist in store", e.getMessage());
			Assert.assertNull(e.getCause());
		}

		//Assert

		//Verify
	}

	@Test
	public void testUpdateDocument_fail_nullDocumentId() throws Exception {
		//Prepare
		InputStream inputStream = getClass().getResourceAsStream(TEST_DOCUMENT);

		//Expect

		//Replay

		//Execute
		try{
			inMemoryDocumentDataMapper.updateDocument(null, inputStream);
		}catch (DocumentDMException e){
			Assert.assertEquals("documentId may not be null", e.getMessage());
			Assert.assertNull(e.getCause());
		}

		//Assert

		//Verify
	}

	@Test
	public void testUpdateDocument_fail_nullDocumentContent() throws Exception {
		//Prepare
		InMemoryDocument document = TestInMemoryDocument.createDocument();
		InputStream inputStream = null;

		//Expect

		//Replay

		//Execute
		try{
			inMemoryDocumentDataMapper.updateDocument(document.getId(), inputStream);
		}catch (DocumentDMException e){
			Assert.assertEquals("documentInputStream may not be null", e.getMessage());
			Assert.assertNull(e.getCause());
		}

		//Assert

		//Verify
	}
}
