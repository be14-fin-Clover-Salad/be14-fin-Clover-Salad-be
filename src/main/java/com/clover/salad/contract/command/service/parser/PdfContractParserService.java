package com.clover.salad.contract.command.service.parser;

import com.clover.salad.contract.command.dto.ContractUploadRequestDTO;
import com.clover.salad.contract.document.entity.DocumentOrigin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@RequiredArgsConstructor
@Service
@Slf4j
public class PdfContractParserService {

	private final PdfParsingStrategyRouter router;

	public ContractUploadRequestDTO parsePdf(File pdfFile, DocumentOrigin origin) {
		try (PDDocument document = PDDocument.load(pdfFile)) {
			PDFTextStripper stripper = new PDFTextStripper();
			stripper.setSortByPosition(true); // 좌표 기준 줄 정렬

			String fullText = stripper.getText(document);
			log.info("전체 텍스트 추출 결과:\n{}", fullText);

			int templateId = origin.getDocumentTemplate().getId();
			PdfContractParsingStrategy strategy = router.getStrategy(templateId);

			// 전체 텍스트만 넘김
			return strategy.parseAll(fullText, "", "", origin);

		} catch (IOException e) {
			log.error(" PDF 분석 실패", e);
			throw new RuntimeException("PDF 분석 실패", e);
		}
	}
}
