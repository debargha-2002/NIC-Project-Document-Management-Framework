package com.debargha.NIC.Document_Management_Framework.Service;

import com.debargha.NIC.Document_Management_Framework.Model.ArchiveDocument;
import com.debargha.NIC.Document_Management_Framework.Model.ClientDocument;
import com.debargha.NIC.Document_Management_Framework.Model.PDFPassword;
import com.debargha.NIC.Document_Management_Framework.Model.Review;
import com.debargha.NIC.Document_Management_Framework.Repository.ArchiveRepository;
import com.debargha.NIC.Document_Management_Framework.Repository.DocumentRepository;
import com.debargha.NIC.Document_Management_Framework.Repository.ReviewRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;
import org.apache.pdfbox.util.Matrix;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class DocumentService {
    @Autowired
    private final DocumentRepository documentRepository;
    private final MongoTemplate mongoTemplate;
    @Autowired
    private ReviewRepository reviewRepository;
    private static final Logger LOGGER = Logger.getLogger(DocumentService.class.getName());

    @Autowired
    public DocumentService(DocumentRepository documentRepository, MongoTemplate mongoTemplate) {
        this.documentRepository = documentRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Autowired
    public ArchiveRepository archiveRepository;

    public UUID saveDocument(ClientDocument document) {
        document.setDocument_id(UUID.randomUUID());
        document.setCreated_on(LocalDateTime.now());
        document.setExpiry_on(LocalDateTime.now().plusYears(1));
        documentRepository.save(document);
        return document.getDocument_id();
    }
    public Optional<ClientDocument> getDocumentById(UUID documentId) {
        System.out.println("searching for document id: " + documentId);
        return documentRepository.findById(documentId);

    }
    public List<ClientDocument> getDocumentsByPersonId(int personId) {
        System.out.println("searching for document with personId: " + personId);
        return documentRepository.findByPersonId(personId);
    }
    public List<ClientDocument> getDocumentsByMobileNumber(long mobile_number) {
        System.out.println("searching for document with mobile number: " + mobile_number);
        return documentRepository.findByMobileNumber(mobile_number);
    }


    // save or update review
    public Review saveOrUpdateReview(Review review) {
        Optional<Review> existingReview = reviewRepository.findByApplication_transaction_id(review.getApplication_transaction_id());
        if (existingReview.isPresent()) {
            //update review
            Review existing = existingReview.get();

           // Query query = new Query(where("application_transaction_id").is(existing.getApplication_transaction_id()));
            Update update = new Update().set("review", review.getReview());
            update.set("review_date",LocalDateTime.now());

            mongoTemplate.updateFirst(query(where("application_transaction_id").is(existing.getApplication_transaction_id())), update,Review.class);
            return mongoTemplate.findOne(query(where("application_transaction_id").is(existing.getApplication_transaction_id())), Review.class);
          //  return reviewRepository.save(existing);
        }
        //create new review
        review.setReview_date(LocalDateTime.now());
        return reviewRepository.save(review);
    }
    //get review logs
    public Optional<Review> viewReviewLog(long application_transaction_id) {

        return reviewRepository.findByApplication_transaction_id(application_transaction_id);
//        Optional<Review> optionalReview = reviewRepository.findByApplication_transaction_id(application_transaction_id);
//        if (optionalReview.isPresent()) {
//            return optionalReview; // Return the found review
//        } else {
//            // Return a default Review with a message
//            return Optional.of(new Review((long)-1, "No review found for application transaction id: " + application_transaction_id));
//        }
    }

    //archive documents
    public ArchiveDocument archiveDocument(ArchiveDocument archiveDocument) {

        Optional<ArchiveDocument> existingArchive = archiveRepository.findByApplication_transaction_id(archiveDocument.getApplication_transaction_id());
        Optional<ClientDocument> archivedDocument = documentRepository.findByApplication_transaction_id(archiveDocument.getApplication_transaction_id());

        if (existingArchive.isPresent()) {
            ArchiveDocument archivedDoc = existingArchive.get();
            archivedDoc.setArchive_comments(archiveDocument.getArchive_comments());
            return archiveRepository.save(archivedDoc);
        }

        archivedDocument.ifPresent(document -> documentRepository.deleteById(document.getDocument_id()));
        archiveDocument.setArchive_date(LocalDateTime.now());
        return archiveRepository.save(archiveDocument);
    }
    //view archive logs

    public Optional<ArchiveDocument> viewEditLog(long application_transaction_id) {
        return archiveRepository.findByApplication_transaction_id(application_transaction_id);
    }

    //update document
    public ClientDocument updateDocument(ClientDocument document) {
        document.setCreated_on(LocalDateTime.now());
        document.setExpiry_on(LocalDateTime.now().plusYears(1));
        return documentRepository.save(document);

    }
    //delete document
    public void deleteDocumentById(UUID documentId) {
        documentRepository.deleteById(documentId);
    }

    //view update logs
     //check if required


    //add watermark
    public ClientDocument addWatermarkToDocument(long application_transaction_id, String watermark) throws IOException {
        Optional<ClientDocument> existingDocument = documentRepository.findByApplication_transaction_id(application_transaction_id);

        if (existingDocument.isEmpty()) {
            throw new IOException("Document not found");
        }

        ClientDocument clientDocument = existingDocument.get();

        byte[] pdfBytes = Base64.getDecoder().decode(clientDocument.getDocument().getActual_document_base_64());

        PDDocument document = PDDocument.load(new ByteArrayInputStream(pdfBytes));

        //loop to add watermark to each page
        for(PDPage page : document.getPages()){
            PDRectangle pageSize = page.getMediaBox();
            float pageWidth = pageSize.getWidth();
            float pageHeight = pageSize.getHeight();
            PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true);

            contentStream.beginText();
            contentStream.setFont(PDType1Font.COURIER_BOLD_OBLIQUE, 100);
            contentStream.setNonStrokingColor(0.5f,0.5f,0.5f);
            // Set opacity (alpha)
            PDExtendedGraphicsState graphicsState = new PDExtendedGraphicsState();
            graphicsState.setNonStrokingAlphaConstant(0.4f);
            contentStream.setGraphicsStateParameters(graphicsState);
             float textWidth = PDType1Font.COURIER_BOLD_OBLIQUE.getStringWidth(watermark)/1000*70;
             float textHeight = PDType1Font.COURIER_BOLD_OBLIQUE.getFontDescriptor().getCapHeight() / 1000*400;
             //System.out.println(textWidth);
            // System.out.println(pageWidth);
            // Calculate starting position for diagonal watermark (bottom to top, middle of page)
            float x = (pageWidth-textWidth)/ 2;
            float y = (pageHeight-textHeight)/ 2;
            double angle = Math.toRadians(45); // Calculate angle of rotation
            //contentStream.setTextMatrix(Matrix.getTranslateInstance(x,200));
            contentStream.setTextMatrix(Matrix.getRotateInstance(angle,x,y));


//            contentStream.setTextMatrix(1, 0, 0, 1, startX, startY);
//            contentStream.setTextRotation(angle, pageWidth / 2, pageHeight / 2); // Rotate around the center

           // contentStream.setTextMatrix(Matrix.getRotateInstance(Math.toRadians(45), 200, 400));  // adjust the position and angle as required
            //contentStream.newLineAtOffset(x, y);
            contentStream.showText(watermark);
            contentStream.endText();
            contentStream.close();
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        document.save(outputStream);
        document.close();

        String base64WatermarkedPdf = Base64.getEncoder().encodeToString(outputStream.toByteArray());

        clientDocument.getDocument().setActual_document_base_64(base64WatermarkedPdf);

        documentRepository.deleteById(existingDocument.get().getDocument_id());
        documentRepository.save(clientDocument);

        return clientDocument;

    }

    // add password
    public String addPasswordToPdf(PDFPassword request) throws IOException {
        Optional<ClientDocument> existingDocument = documentRepository.findByApplication_transaction_id(request.getApplication_transaction_id());

        if (existingDocument.isEmpty()) {
            throw new IOException("Document not found");
        }

        ClientDocument clientDocument = existingDocument.get();

        byte[] pdfBytes = Base64.getDecoder().decode(clientDocument.getDocument().getActual_document_base_64());

        PDDocument document = PDDocument.load(new ByteArrayInputStream(pdfBytes));

        // Set the password protection
        AccessPermission accessPermission = new AccessPermission();
        StandardProtectionPolicy protectionPolicy = new StandardProtectionPolicy(
                request.getPassword(), request.getPassword(), accessPermission);

        // Customize the protection policy if necessary
        protectionPolicy.setEncryptionKeyLength(128);  // 128-bit key length
        protectionPolicy.setPermissions(accessPermission);
        document.protect(protectionPolicy);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        document.save(outputStream);
        document.close();

        String base64PdfWithPassword = Base64.getEncoder().encodeToString(outputStream.toByteArray());

        // Update the ClientDocument with the new Base64 content
        clientDocument.getDocument().setActual_document_base_64(base64PdfWithPassword);

        // Save the updated ClientDocument
        documentRepository.save(clientDocument);

        return base64PdfWithPassword;
    }

}

