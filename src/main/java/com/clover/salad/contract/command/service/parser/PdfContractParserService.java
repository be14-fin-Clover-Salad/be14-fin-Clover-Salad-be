package com.clover.salad.contract.command.service.parser;

import com.clover.salad.contract.command.dto.ContractUploadRequestDTO;
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

	public ContractUploadRequestDTO parsePdf(File pdfFile) {
		try (PDDocument document = PDDocument.load(pdfFile)) {
			PDFTextStripper stripper = new PDFTextStripper();
			stripper.setSortByPosition(true); // 좌표 기준 줄 정렬

			String fullText = stripper.getText(document);
			log.info("전체 텍스트 추출 결과:\n{}", fullText);

			int templateId = 1;
			PdfContractParsingStrategy strategy = router.getStrategy(templateId);

			// origin 없이 파싱
			return strategy.parseAll(fullText, "", "", null);

		} catch (IOException e) {
			log.error("PDF 분석 실패", e);
			throw new RuntimeException("PDF 분석 실패", e);
		}
	}
}
