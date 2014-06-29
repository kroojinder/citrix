package com.krooj.docuserv.servlet;

import com.krooj.docuserv.service.DocumentService;
import com.krooj.docuserv.service.DocuservServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;

/**
 * This servlet provides a very basic RESTful interface to a document store without
 * consideration for Hypermedia, persistence, redirection, or other niceties.
 *
 * @author Michael Kuredjian
 */
@WebServlet(name = "document-servlet", urlPatterns = {"/storage/documents/*", "/storage/documents"}, asyncSupported = true)
public class DocumentServlet extends HttpServlet {

    private final static Logger LOGGER = LogManager.getLogger(DocumentServlet.class.getName());
    @Inject
    private DocumentService documentService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        try {
            final String documentId = extractDocumentId(request.getRequestURI());
            LOGGER.info("Got GET request for document: " + documentId + " from host: " + request.getRemoteHost());
            final AsyncContext asyncContext = request.startAsync();
            DocumentServletWriteListener documentServletWriteListener = new DocumentServletWriteListener(asyncContext, response.getOutputStream(), documentService, documentId);
            response.getOutputStream().setWriteListener(documentServletWriteListener);

        } catch (DocumentServletException e) {
            LOGGER.error(e);
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        try {
            String documentId = extractDocumentId(request.getRequestURI());
            LOGGER.info("Got DELETE request for document: " + documentId + " from host: " + request.getRemoteHost());

            getDocumentService().deleteDocument(documentId);

            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (DocuservServiceException | DocumentServletException e) {
            LOGGER.error(e);
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }

    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        try {
            Part filePart = request.getPart("document");
            String documentId = getFileName(filePart);
            LOGGER.info("Got PUT request for document: " + documentId + " from host: " + request.getRemoteHost());

            getDocumentService().updateDocument(documentId, filePart.getInputStream());

            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (DocuservServiceException e) {
            LOGGER.error(e);
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    /**
     * This method will support file upload functionality.
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Create path components to save the file

            final String documentId = request.getHeader("x-documentid");
            LOGGER.info("Got POST request for document: " + documentId + " from host: " + request.getRemoteHost());

            AsyncContext asyncContext = request.startAsync();
            DocumentServletReadListener documentServletReadListener = new DocumentServletReadListener(asyncContext, request.getInputStream(), response, documentService, documentId);
            request.getInputStream().setReadListener(documentServletReadListener);


    }

    private String extractDocumentId(String requestUrl) throws DocumentServletException {
        if (requestUrl == null || requestUrl.length() == 0 || requestUrl.endsWith("/")) {
            throw new DocumentServletException("Malformed document request");
        }
        return requestUrl.substring(requestUrl.lastIndexOf("/") + 1, requestUrl.length());
    }

    private String getFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        LOGGER.info("content-disposition header= " + contentDisp);
        String[] tokens = contentDisp.split(";");
        for (String token : tokens) {
            if (token.trim().startsWith("filename")) {
                return token.substring(token.indexOf("=") + 2, token.length() - 1);
            }
        }
        return "";
    }

    public DocumentService getDocumentService() {
        return documentService;
    }

    public void setDocumentService(DocumentService documentService) {
        this.documentService = documentService;
    }
}
