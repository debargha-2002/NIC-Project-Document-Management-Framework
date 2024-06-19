package com.debargha.NIC.Document_Management_Framework.Controller;

import com.debargha.NIC.Document_Management_Framework.Model.*;
import com.debargha.NIC.Document_Management_Framework.Repository.DocumentRepository;
import com.debargha.NIC.Document_Management_Framework.Service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
public class DocumentController {
    @Autowired
    private DocumentService documentService;
    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    public DocumentController(DocumentService documentService, DocumentRepository documentRepository) {
        this.documentService = documentService;
        this.documentRepository = documentRepository;
    }


    @PostMapping("/savedocument")
    public String saveDocument(@RequestBody ClientDocument document) {
        try {
            UUID document_id = documentService.saveDocument(document);
            return "Document saved successfully." + "\n" + "Document id: " + document_id;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    @GetMapping("/getadocument/{id}")
    public ResponseEntity<ClientDocument> getDocument(@PathVariable("id") UUID document_id){
        System.out.println("Received request for document ID: " + document_id);
        Optional<ClientDocument> document = documentService.getDocumentById(document_id);
        if (document.isPresent()) {
            return ResponseEntity.ok(document.get());
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/documentofaperson/{person_id}")
    public ResponseEntity<?> getDocumentByPersonId(@PathVariable int person_id) {
        System.out.println("Received request for documents with person ID: " + person_id);
        List<ClientDocument> clientDocuments = documentService.getDocumentsByPersonId(person_id);

        if (clientDocuments.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(clientDocuments);
        }
    }
    @GetMapping("/documentusingmobileno/{mobile_number}")
    public ResponseEntity<?> getDocumentUsingMobileNumber(@PathVariable long mobile_number) {
       // System.out.println("Received request for documents with mobile number : " + mobile_number);
        List<ClientDocument> clientDocuments = documentService.getDocumentsByMobileNumber(mobile_number);

        if (clientDocuments.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(clientDocuments);
        }
    }
    @PostMapping("/reviewdocument")
    public  ResponseEntity<Review> saveOrUpdateReview(@RequestBody Review review){
        Optional<ClientDocument> document = documentRepository.findByApplication_transaction_id(review.getApplication_transaction_id());

        if(document.isPresent()){
            review.setApplication_transaction_id(review.getApplication_transaction_id());
            Review savedReview = documentService.saveOrUpdateReview(review);

            return ResponseEntity.ok(savedReview);
        }else{
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/viewreviewlog/{applicationTransactionId}")
    public ResponseEntity<Review> getReviewByApplicationId(@PathVariable long applicationTransactionId) {
        Optional<Review> reviewOptional = documentService.viewReviewLog(applicationTransactionId);

        if(reviewOptional.isPresent()){
            return ResponseEntity.ok(reviewOptional.get());
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping("/archivedocument")
    public ResponseEntity<?> archiveDocument(@RequestBody ArchiveDocument archiveDocument) {
        Optional<ClientDocument> clientDocumentOptional = documentRepository.findByApplication_transaction_id(archiveDocument.getApplication_transaction_id());

        if (clientDocumentOptional.isPresent()) {
            ArchiveDocument savedArchiveDocument = documentService.archiveDocument(archiveDocument);
            return ResponseEntity.ok(savedArchiveDocument);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/vieweditlog/{applicationTransactionId}")
    public ResponseEntity<ArchiveDocument> getArchiveDocumentByApplicationTransactionId(@PathVariable long applicationTransactionId) {
        Optional<ArchiveDocument> archiveDocumentOptional = documentService.viewEditLog(applicationTransactionId);

        if (archiveDocumentOptional.isPresent()) {
            return ResponseEntity.ok(archiveDocumentOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/editdocumentinfo/{documentId}")
    public ResponseEntity<?> editDocumentInfo(@PathVariable UUID documentId, @RequestBody ClientDocument newDocument) {
        Optional<ClientDocument>  responseEntity = documentService.getDocumentById(documentId);

        if (responseEntity.isPresent()) {
            documentService.deleteDocumentById(documentId);

             newDocument.setDocument_id(documentId);
            ClientDocument savedDocument = documentService.updateDocument(newDocument);

            return ResponseEntity.ok(savedDocument);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping("/addwatermarktodocument")
    public ResponseEntity<?> addWatermarkToDocument(@RequestBody Watermark watermarkRequest){
        try {
            ClientDocument updatedDocument = documentService.addWatermarkToDocument(watermarkRequest.getApplication_transaction_id(),
                    watermarkRequest.getWatermark());
            return new ResponseEntity<>(updatedDocument, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/setadocumentconfidential")
    public ResponseEntity<?> addPasswordToPdf(@RequestBody PDFPassword request) {
        try {
            String base64PdfWithPassword = documentService.addPasswordToPdf(request);
            return ResponseEntity.ok("Updated base64Pdf with password:"+base64PdfWithPassword);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to add password to base64PDF: " + e.getMessage());
        }
    }



}
