package com.krooj.docuserv.domain;

import com.krooj.docuserv.DocuservUnitTest;
import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Test cases for the document domain object
 *
 * @author Michael Kuredjian
 */
public class TestPhysicalDocument extends DocuservUnitTest{



    @Test
    public void testCreateDocument_success() throws Exception {
        //Prepare


        //Expect

        //Replay

        //Execute
        PhysicalDocument document = new PhysicalDocument(DOCUMENT_ID, DOCUMENT_PATH);

        //Assert
        Assert.assertNotNull(document);
        Assert.assertEquals(DOCUMENT_ID, document.getId());

        //Verify
    }

    @Test
    public void testCreateDocument_fail_malformedId_trailingSlash() throws Exception {
        //Prepare


        //Expect

        //Replay

        //Execute
        try{
            new PhysicalDocument("test.bar/", DOCUMENT_PATH);
        }catch(DocuservDomainException e){
            Assert.assertEquals("Document id must be alpha-numeric with a '.' extension and no more than 20 characters", e.getMessage());
            Assert.assertNull(e.getCause());
        }


        //Verify
    }

    @Test
    public void testCreateDocument_fail_malformedId_null() throws Exception {
        //Prepare


        //Expect

        //Replay

        //Execute
        try{
            new PhysicalDocument(null, DOCUMENT_PATH);
        }catch(DocuservDomainException e){
            Assert.assertEquals("id may not be empty or null", e.getMessage());
            Assert.assertNull(e.getCause());
        }


        //Verify
    }

    @Test
    public void testCreateDocument_fail_malformedId_empty() throws Exception {
        //Prepare


        //Expect

        //Replay

        //Execute
        try{
            new PhysicalDocument(" ", DOCUMENT_PATH);
        }catch(DocuservDomainException e){
            Assert.assertEquals("id may not be empty or null", e.getMessage());
            Assert.assertNull(e.getCause());
        }


        //Verify
    }

    @Test
    public void testCreateDocument_fail_malformedPath_null() throws Exception {
        //Prepare


        //Expect

        //Replay

        //Execute
        try{
            new PhysicalDocument(DOCUMENT_ID, null);
        }catch(DocuservDomainException e){
            Assert.assertEquals("physicalLocation may not be empty or null", e.getMessage());
            Assert.assertNull(e.getCause());
        }


        //Verify
    }

    @Test
    public void testCreateDocument_fail_malformedPath_empty() throws Exception {
        //Prepare


        //Expect

        //Replay

        //Execute
        try{
            new PhysicalDocument(DOCUMENT_ID, " ");
        }catch(DocuservDomainException e){
            Assert.assertEquals("physicalLocation may not be empty or null", e.getMessage());
            Assert.assertNull(e.getCause());
        }


        //Verify
    }

    @Test
    public void testDocumentEquals_success_expectedEquals() throws Exception{

        Document documentA = new PhysicalDocument(DOCUMENT_ID,DOCUMENT_PATH);
        Document documentB = new PhysicalDocument(DOCUMENT_ID,DOCUMENT_PATH);

        Assert.assertTrue(documentA.equals(documentB));
        Assert.assertTrue(documentB.equals(documentA));
    }

    @Test
    public void testDocumentEquals_success_expectedNonEquals() throws Exception{

        Document documentA = new PhysicalDocument("bar.txt",DOCUMENT_PATH);
        Document documentB = new PhysicalDocument(DOCUMENT_ID,DOCUMENT_PATH);

        Assert.assertFalse(documentA.equals(documentB));
        Assert.assertFalse(documentB.equals(documentA));
    }

    @Test
    public void testDocumentHashcode_collision() throws Exception{
        Document documentA = new PhysicalDocument(DOCUMENT_ID,DOCUMENT_PATH);
        Document documentB = new PhysicalDocument(DOCUMENT_ID,DOCUMENT_PATH);
        Set<Document> documents = new LinkedHashSet<>();
        documents.add(documentA);
        Assert.assertTrue(documents.contains(documentB));
    }

    @Test
    public void testDocumentHashcode_noCollision() throws Exception{
        Document documentA = new PhysicalDocument("bar.txt",DOCUMENT_PATH);
        Document documentB = new PhysicalDocument(DOCUMENT_ID,DOCUMENT_PATH);
        Set<Document> documents = new LinkedHashSet<>();
        documents.add(documentA);
        Assert.assertFalse(documents.contains(documentB));
    }
}
