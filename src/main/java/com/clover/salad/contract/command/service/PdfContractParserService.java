package com.clover.salad.contract.command.service;

import com.clover.salad.contract.command.dto.*;
import com.clover.salad.contract.document.entity.DocumentOrigin;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
public class PdfContractParserService {

	public ContractUploadRequestDTO parsePdf(File pdfFile, DocumentOrigin documentOrigin) {
		try (PDDocument document = PDDocument.load(pdfFile)) {
			PDFTextStripper stripper = new PDFTextStripper();
			String text = stripper.getText(document).trim();

			log.info("PDF 추출 텍스트:\n{}", text);

			// 줄 단위로 split
			String[] lines = text.split("\\r?\\n");
			List<String> values = new ArrayList<>();

			Set<String> labelSet = Set.of(
				"고객정보", "계약정보",
				"주소", "연락처", "계약시작일", "결제정보이메일",
				"이름", "이메일", "생년월일", "계약종료일", "계약금액",
				"은행명", "본인과의 관계계좌번호"
			);

			// 실제 값들만 추출
			for (String line : lines) {
				String trimmed = line.trim();
				if (!trimmed.isBlank() && !labelSet.contains(trimmed)) {
					values.add(trimmed);
				}
			}

			if (values.size() < 12) {
				log.warn("추출된 값이 부족합니다. 현재 개수: {}", values.size());
				throw new IllegalArgumentException("PDF에 필요한 데이터가 부족합니다.");
			}

			CustomerDTO customer = new CustomerDTO();
			ContractDTO contract = new ContractDTO();

			try {
				customer.setName(values.get(0));
				customer.setBirthdate(values.get(1));
				customer.setAddress(values.get(2));
				customer.setPhone(values.get(3));
				customer.setEmail(values.get(4));
				customer.setCustomerType("고객");

				String[] dates = values.get(5).split("\\s+");
				if (dates.length == 2) {
					contract.setStartDate(LocalDate.parse(dates[0], DateTimeFormatter.ISO_DATE));
					contract.setEndDate(LocalDate.parse(dates[1], DateTimeFormatter.ISO_DATE));
				} else {
					log.warn("계약 시작일/종료일 파싱 실패: {}", values.get(5));
				}

				contract.setAmount(Integer.parseInt(values.get(6)));
				contract.setBankAccount(values.get(7));
				contract.setBankName(values.get(8));
				contract.setRelationship(values.get(9));
				contract.setPaymentEmail(values.get(10));
				contract.setDepositOwner(values.get(11));
				contract.setPaymentDay(25); // 고정

			} catch (Exception e) {
				log.error("DTO 매핑 중 오류: {}", e.getMessage(), e);
				throw new RuntimeException("PDF 데이터 매핑 오류", e);
			}

			log.info("[CustomerDTO] {}", customer);
			log.info("[ContractDTO] {}", contract);

			ContractUploadRequestDTO result = new ContractUploadRequestDTO();
			result.setCustomer(customer);
			result.setContract(contract);
			result.setDocumentOrigin(documentOrigin);
			return result;

		} catch (Exception e) {
			throw new RuntimeException("PDF 분석 실패: " + e.getMessage(), e);
		}
	}
}
