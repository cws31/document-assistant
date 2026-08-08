package com.rag.documentprocessingpipeline.parsing.analysis.strategy.image;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.poi.xwpf.usermodel.XWPFPictureData;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Component;

import com.rag.documentprocessingpipeline.parsing.analysis.context.AnalysisContext;
import com.rag.documentprocessingpipeline.parsing.exception.ParsingException;
import com.rag.documentprocessingpipeline.parsing.model.FileType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DocxImageExtractionStrategy
        implements ImageExtractionStrategy {

    @Override
    public boolean supports(
            AnalysisContext context) {

        return context != null
                && context.getFileType() == FileType.DOCX;
    }

    @Override
    public List<BufferedImage> extract(
            AnalysisContext context) {

        if (context == null) {
            throw new ParsingException(
                    "AnalysisContext cannot be null.");
        }

        Object sourceDocument = context.getSourceDocument();

        if (!(sourceDocument instanceof XWPFDocument document)) {

            throw new ParsingException(
                    "Invalid source document for DOCX image extraction.");
        }

        List<BufferedImage> images = new ArrayList<>();

        List<XWPFPictureData> pictures = document.getAllPictures();

        for (XWPFPictureData picture : pictures) {

            if (picture == null) {
                continue;
            }

            try {

                byte[] imageBytes = picture.getData();

                if (imageBytes == null
                        || imageBytes.length == 0) {

                    log.debug(
                            "Skipping empty DOCX image. documentId={}",
                            context.getDocument().getId());

                    continue;
                }

                BufferedImage image = ImageIO.read(
                        new ByteArrayInputStream(
                                imageBytes));

                if (image == null) {

                    log.warn(
                            "Unable to decode DOCX image. " +
                                    "documentId={}, fileName={}",
                            context.getDocument().getId(),
                            picture.getFileName());

                    continue;
                }

                images.add(image);

                log.debug(
                        "DOCX image extracted. " +
                                "documentId={}, fileName={}, width={}, height={}",
                        context.getDocument().getId(),
                        picture.getFileName(),
                        image.getWidth(),
                        image.getHeight());

            } catch (IOException ex) {

                log.warn(
                        "Failed to decode DOCX image. " +
                                "documentId={}, fileName={}",
                        context.getDocument().getId(),
                        picture.getFileName(),
                        ex);
            }
        }

        log.info(
                "DOCX image extraction completed. " +
                        "documentId={}, imageCount={}",
                context.getDocument().getId(),
                images.size());

        return images;
    }
}